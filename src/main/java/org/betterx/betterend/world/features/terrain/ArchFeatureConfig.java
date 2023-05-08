package org.betterx.betterend.world.features.terrain;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.biome.BiomeSource;

import java.util.function.Function;

public class ArchFeatureConfig implements FeatureConfiguration {
    public static final Codec<ArchFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    BlockStateProvider.CODEC.fieldOf("states").forGetter(o -> o.block),
                    SurfaceFunction.CODEC.fieldOf("surface_function").forGetter(o -> o.surfaceFunction)
            )
            .apply(instance, ArchFeatureConfig::new));


    public final BlockStateProvider block;
    public final SurfaceFunction surfaceFunction;

    public ArchFeatureConfig(Block block, SurfaceFunction surfaceFunction) {
        this(SimpleStateProvider.simple(block), surfaceFunction);
    }

    public ArchFeatureConfig(BlockStateProvider block, SurfaceFunction surfaceFunction) {
        this.block = block;
        this.surfaceFunction = surfaceFunction;
    }

    public enum SurfaceFunction implements StringRepresentable {

        UMBRA_VALLEY("umbra_valley", pos -> Blocks.END_STONE.defaultBlockState());
        public static final Codec<SurfaceFunction> CODEC = StringRepresentable.fromEnum(SurfaceFunction::values);
        private final Function<BlockPos, BlockState> surfaceFunction;
        private final String name;

        SurfaceFunction(String name, Function<BlockPos, BlockState> surfaceFunction) {
            this.name = name;
            this.surfaceFunction = surfaceFunction;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        public BlockState apply(BlockPos pos) {
            return this.surfaceFunction.apply(pos);
        }
    }
}
