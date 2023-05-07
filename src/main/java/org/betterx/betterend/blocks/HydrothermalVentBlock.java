package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BaseBlockNotFull;
import org.betterx.bclib.blocks.BlockProperties;
import org.betterx.bclib.interfaces.tools.AddMineablePickaxe;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.betterend.blocks.entities.BlockEntityHydrothermalVent;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class HydrothermalVentBlock extends BaseBlockNotFull implements EntityBlock, LiquidBlockContainer, SimpleWaterloggedBlock, AddMineablePickaxe {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty ACTIVATED = BlockProperties.ACTIVE;
    private static final VoxelShape SHAPE = Block.box(1, 1, 1, 15, 16, 15);

    public HydrothermalVentBlock() {
        super(FabricBlockSettings.of(Material.STONE)
                                 .sound(SoundType.STONE)
                                 .noCollission()
                                 .requiresCorrectToolForDrops());
        this.registerDefaultState(defaultBlockState().setValue(WATERLOGGED, true).setValue(ACTIVATED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, ACTIVATED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
        return SHAPE;
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        state = world.getBlockState(pos.below());
        return state.is(EndBlocks.SULPHURIC_ROCK.stone);
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            Direction facing,
            BlockState neighborState,
            LevelAccessor world,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        if (!canSurvive(state, world, pos)) {
            return Blocks.WATER.defaultBlockState();
        } else if (state.getValue(WATERLOGGED) && facing == Direction.UP && neighborState.is(Blocks.WATER)) {
            world.scheduleTick(pos, this, 20);
        }
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        LevelAccessor worldAccess = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        return this.defaultBlockState()
                   .setValue(WATERLOGGED, worldAccess.getFluidState(blockPos).getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityHydrothermalVent(pos, state);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        BlockPos up = pos.above();
        if (world.getBlockState(up).is(Blocks.WATER)) {
            BlocksHelper.setWithoutUpdate(world, up, EndBlocks.VENT_BUBBLE_COLUMN);
            world.scheduleTick(up, EndBlocks.VENT_BUBBLE_COLUMN, 5);
        }
    }

    @Override
    public void setPlacedBy(
            Level world,
            BlockPos pos,
            BlockState state,
            @Nullable LivingEntity placer,
            ItemStack itemStack
    ) {
        if (world instanceof ServerLevel && state.getValue(WATERLOGGED) && world.getBlockState(pos.above())
                                                                                .is(Blocks.WATER)) {
            tick(state, (ServerLevel) world, pos, world.random);
        }
    }

    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        super.animateTick(state, world, pos, random);
        if (!state.getValue(ACTIVATED) && random.nextBoolean()) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + 0.9 + random.nextDouble() * 0.3;
            double z = pos.getZ() + random.nextDouble();
            world.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0, 0, 0);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState blockState,
            BlockEntityType<T> blockEntityType
    ) {
        return BlockEntityHydrothermalVent::tick;
    }
}
