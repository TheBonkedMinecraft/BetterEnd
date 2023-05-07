package org.betterx.betterend.commands;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.poi.BCLPoiType;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.betterend.registry.EndBiomes;
import org.betterx.betterend.registry.EndPoiTypes;
import org.betterx.betterend.world.biome.EndBiome;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3d;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import java.util.*;

public class CommandRegistry {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(CommandRegistry::register);
    }

    private static void register(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext commandBuildContext,
            Commands.CommandSelection commandSelection
    ) {
        dispatcher.register(
                Commands.literal("be")
                        .requires(source -> source.hasPermission(Commands.LEVEL_OWNERS))
                        .then(Commands.literal("locate_portal")
                                      .requires(source -> source.hasPermission(Commands.LEVEL_OWNERS))
                                      .executes(ctx -> find_poi(ctx, EndPoiTypes.ETERNAL_PORTAL_INACTIVE))
                        )
                        .then(Commands.literal("tpnext")
                                      .requires(source -> source.hasPermission(Commands.LEVEL_OWNERS))
                                      .executes(ctx -> teleportToNextBiome(ctx))
                        )
        );
    }

    private static final Map<Holder<Biome>, BlockState> biomeMap = new HashMap<>();
    private static final int biomeMapIdx = 0;
    private static final BlockState[] states = {
            Blocks.RED_STAINED_GLASS.defaultBlockState(),
            Blocks.BLUE_STAINED_GLASS.defaultBlockState(),
            Blocks.YELLOW_STAINED_GLASS.defaultBlockState(),
            Blocks.LIME_STAINED_GLASS.defaultBlockState(),
            Blocks.PINK_STAINED_GLASS.defaultBlockState(),
            Blocks.GREEN_STAINED_GLASS.defaultBlockState(),
            Blocks.WHITE_STAINED_GLASS.defaultBlockState(),
            Blocks.BLACK_STAINED_GLASS.defaultBlockState(),
            Blocks.ORANGE_STAINED_GLASS.defaultBlockState(),
            Blocks.LIGHT_BLUE_STAINED_GLASS.defaultBlockState()
    };
    private static final BlockState[] states2 = {
            Blocks.RED_CONCRETE.defaultBlockState(),
            Blocks.BLUE_CONCRETE.defaultBlockState(),
            Blocks.YELLOW_CONCRETE.defaultBlockState(),
            Blocks.LIME_CONCRETE.defaultBlockState(),
            Blocks.PINK_CONCRETE.defaultBlockState(),
            Blocks.GREEN_CONCRETE.defaultBlockState(),
            Blocks.WHITE_CONCRETE.defaultBlockState(),
            Blocks.BLACK_CONCRETE.defaultBlockState(),
            Blocks.ORANGE_CONCRETE.defaultBlockState(),
            Blocks.LIGHT_BLUE_CONCRETE.defaultBlockState()
    };


    private static int find_poi(CommandContext<CommandSourceStack> ctx, BCLPoiType poi) throws CommandSyntaxException {
        final CommandSourceStack source = ctx.getSource();
        final ServerPlayer player = source.getPlayerOrException();
        Vec3 pos = source.getPosition();
        final ServerLevel level = source.getLevel();
        MutableBlockPos mPos = new BlockPos(pos).mutable();
        System.out.println("Searching POI: " + poi.key);
        Optional<BlockPos> found = poi.findPoiAround(level, mPos, false, level.getWorldBorder());
        System.out.println("Found at: " + found.orElse(null));
        if (found.isPresent()) {
            BlocksHelper.setWithoutUpdate(level, found.get(), Blocks.YELLOW_CONCRETE);
            BlocksHelper.setWithoutUpdate(level, mPos, Blocks.LIGHT_BLUE_CONCRETE);
        } else {
            BlocksHelper.setWithoutUpdate(level, mPos, Blocks.RED_CONCRETE);
        }
        return Command.SINGLE_SUCCESS;
    }


    private static int biomeIndex = 0;
    private static final int MAX_SEARCH_RADIUS = 6400 * 2;
    private static final int SAMPLE_RESOLUTION_HORIZONTAL = 32;
    private static final int SAMPLE_RESOLUTION_VERTICAL = 64;
    private static final DynamicCommandExceptionType ERROR_BIOME_NOT_FOUND = new DynamicCommandExceptionType(
            (object) -> {
                return Component.literal("The next biome (" + object + ") was not found.");
            });

    private static int teleportToNextBiome(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final CommandSourceStack source = ctx.getSource();
        List<EndBiome> biomes = EndBiomes.ALL_BE_BIOMES;

        if (biomeIndex < 0 || biomeIndex >= biomes.size()) {
            source.sendFailure(Component.literal("Failed to find the next Biome...")
                                        .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
            return 0;
        }
        final BCLBiome biome = biomes.get(biomeIndex);
        source.sendSuccess(Component.literal("Locating Biome " + biome)
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN)), false);
        biomeIndex = (biomeIndex + 1) % biomes.size();

        final BlockPos currentPosition = new BlockPos(source.getPosition());
        final BlockPos biomePosition = source.getLevel()
                                             .findClosestBiome3d(
                                                     b -> b.unwrapKey().orElseThrow().location().equals(biome.getID()),
                                                     currentPosition,
                                                     MAX_SEARCH_RADIUS,
                                                     SAMPLE_RESOLUTION_HORIZONTAL,
                                                     SAMPLE_RESOLUTION_VERTICAL
                                             )
                                             .getFirst();
        final String biomeName = biome.toString();

        if (biomePosition == null) {
            throw ERROR_BIOME_NOT_FOUND.create(biomeName);
        } else {
            final ServerPlayer player = source.getPlayerOrException();
            BlockState state;
            BlockPos target;
            double yPos = source.getPosition().y();
            boolean didWrap = false;
            do {
                target = new BlockPos(biomePosition.getX(), yPos, biomePosition.getZ());
                state = player.level.getBlockState(target);
                yPos--;
                if (yPos <= player.level.getMinBuildHeight() + 1) {
                    if (didWrap) break;
                    yPos = 127;
                    didWrap = true;
                }
            } while (!state.isAir() && yPos > player.level.getMinBuildHeight() && yPos < player.level.getMaxBuildHeight());
            Vector3d targetPlayerPos = new Vector3d(target.getX() + 0.5, target.getY() - 1, target.getZ() + 0.5);

            player.connection.teleport(
                    targetPlayerPos.x,
                    targetPlayerPos.y,
                    targetPlayerPos.z,
                    0,
                    0,
                    Collections.EMPTY_SET
            );
            ResourceOrTagLocationArgument.Result result = new ResourceOrTagLocationArgument.Result() {
                @Override
                public Either<ResourceKey, TagKey> unwrap() {
                    return Either.left(biome.getBiomeKey());
                }

                @Override
                public Optional<ResourceOrTagLocationArgument.Result> cast(ResourceKey resourceKey) {
                    return Optional.empty();
                }

                @Override
                public String asPrintable() {
                    return biomeName;
                }

                @Override
                public boolean test(Object o) {
                    return false;
                }
            };
            ResourceKey<Biome> a = biome.getBiomeKey();
            Holder<Biome> h = BuiltinRegistries.BIOME.getHolder(a).orElseThrow();
            return LocateCommand.showLocateResult(
                    source,
                    result,
                    currentPosition,
                    new Pair<>(biomePosition, h),
                    "commands.locatebiome.success",
                    false
            );
        }
    }
}

