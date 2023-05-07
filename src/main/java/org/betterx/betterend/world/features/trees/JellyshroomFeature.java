package org.betterx.betterend.world.features.trees;

import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.sdf.SDF;
import org.betterx.bclib.sdf.operator.*;
import org.betterx.bclib.sdf.primitive.SDFSphere;
import org.betterx.bclib.util.MHelper;
import org.betterx.bclib.util.SplineHelper;
import org.betterx.betterend.blocks.JellyshroomCapBlock;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Function;

public class JellyshroomFeature extends DefaultFeature {
    private static final Function<BlockState, Boolean> REPLACE;
    private static final List<Vector3f> ROOT;

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
        final RandomSource random = featureConfig.random();
        final BlockPos pos = featureConfig.origin();
        final WorldGenLevel world = featureConfig.level();
        if (!world.getBlockState(pos.below()).is(BlockTags.NYLIUM)) return false;

        BlockState bark = EndBlocks.JELLYSHROOM.getBark().defaultBlockState();
        BlockState membrane = EndBlocks.JELLYSHROOM_CAP_PURPLE.defaultBlockState();

        int height = MHelper.randRange(5, 8, random);
        float radius = height * MHelper.randRange(0.15F, 0.25F, random);
        List<Vector3f> spline = SplineHelper.makeSpline(0, -1, 0, 0, height, 0, 3);
        SplineHelper.offsetParts(spline, random, 0.5F, 0, 0.5F);
        SDF sdf = SplineHelper.buildSDF(spline, radius, 0.8F, (bpos) -> {
            return bark;
        });

        radius = height * MHelper.randRange(0.7F, 0.9F, random);
        if (radius < 1.5F) {
            radius = 1.5F;
        }
        final float membraneRadius = radius;
        SDF cap = makeCap(membraneRadius, random, membrane);
        final Vector3f last = spline.get(spline.size() - 1);
        cap = new SDFTranslate().setTranslate(last.x(), last.y(), last.z()).setSource(cap);
        sdf = new SDFSmoothUnion().setRadius(3F).setSourceA(sdf).setSourceB(cap);
        sdf.setReplaceFunction(REPLACE).addPostProcess((info) -> {
            if (EndBlocks.JELLYSHROOM.isTreeLog(info.getState())) {
                if (EndBlocks.JELLYSHROOM.isTreeLog(info.getStateUp()) && EndBlocks.JELLYSHROOM.isTreeLog(info.getStateDown())) {
                    return EndBlocks.JELLYSHROOM.getLog().defaultBlockState();
                }
            } else if (info.getState().is(EndBlocks.JELLYSHROOM_CAP_PURPLE)) {
                float dx = info.getPos().getX() - pos.getX() - last.x();
                float dz = info.getPos().getZ() - pos.getZ() - last.z();
                float distance = MHelper.length(dx, dz) / membraneRadius * 7F;
                int color = Mth.clamp(MHelper.floor(distance), 0, 7);
                return info.getState().setValue(JellyshroomCapBlock.COLOR, color);
            }
            return info.getState();
        }).fillRecursive(world, pos);
        radius = height * 0.5F;
        makeRoots(world, pos.offset(0, 2, 0), radius, random, bark);

        return true;
    }

    private void makeRoots(WorldGenLevel world, BlockPos pos, float radius, RandomSource random, BlockState wood) {
        int count = (int) (radius * 3.5F);
        for (int i = 0; i < count; i++) {
            float angle = (float) i / (float) count * MHelper.PI2;
            float scale = radius * MHelper.randRange(0.85F, 1.15F, random);

            List<Vector3f> branch = SplineHelper.copySpline(ROOT);
            SplineHelper.rotateSpline(branch, angle);
            SplineHelper.scale(branch, scale);
            Vector3f last = branch.get(branch.size() - 1);
            if (world.getBlockState(pos.offset(last.x(), last.y(), last.z())).is(CommonBlockTags.GEN_END_STONES)) {
                SplineHelper.fillSpline(branch, world, wood, pos, REPLACE);
            }
        }
    }

    private SDF makeCap(float radius, RandomSource random, BlockState cap) {
        SDF sphere = new SDFSphere().setRadius(radius).setBlock(cap);
        SDF sub = new SDFTranslate().setTranslate(0, -4, 0).setSource(sphere);
        sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(sub);
        sphere = new SDFScale3D().setScale(1, 0.5F, 1).setSource(sphere);
        sphere = new SDFTranslate().setTranslate(0, 1 - radius * 0.5F, 0).setSource(sphere);

        float angle = random.nextFloat() * MHelper.PI2;
        int count = (int) MHelper.randRange(radius * 0.5F, radius, random);
        if (count < 3) {
            count = 3;
        }
        sphere = new SDFFlatWave().setAngle(angle).setRaysCount(count).setIntensity(0.2F).setSource(sphere);

        return sphere;
    }

    static {
        ROOT = Lists.newArrayList(
                new Vector3f(0.1F, 0.70F, 0),
                new Vector3f(0.3F, 0.30F, 0),
                new Vector3f(0.7F, 0.05F, 0),
                new Vector3f(0.8F, -0.20F, 0)
        );
        SplineHelper.offset(ROOT, new Vector3f(0, -0.45F, 0));

        REPLACE = (state) -> {
            if (/*state.is(CommonBlockTags.END_STONES) || */state.getMaterial().equals(Material.PLANT)) {
                return true;
            }
            return state.getMaterial().isReplaceable();
        };
    }
}
