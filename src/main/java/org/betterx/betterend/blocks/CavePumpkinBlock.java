package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BaseBlockNotFull;
import org.betterx.bclib.blocks.BlockProperties;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import java.util.Collections;
import java.util.List;

public class CavePumpkinBlock extends BaseBlockNotFull implements RenderLayerProvider {
    public static final BooleanProperty SMALL = BlockProperties.SMALL;
    private static final VoxelShape SHAPE_SMALL;
    private static final VoxelShape SHAPE_BIG;

    public CavePumpkinBlock() {
        super(FabricBlockSettings.copyOf(Blocks.PUMPKIN).luminance((state) -> state.getValue(SMALL) ? 10 : 15));
        registerDefaultState(defaultBlockState().setValue(SMALL, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(SMALL);
    }

    @Override
    public BCLRenderLayer getRenderLayer() {
        return BCLRenderLayer.CUTOUT;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(SMALL) ? SHAPE_SMALL : SHAPE_BIG;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return state.getValue(SMALL)
                ? Collections.singletonList(new ItemStack(EndBlocks.CAVE_PUMPKIN_SEED))
                : Collections
                        .singletonList(new ItemStack(this));
    }

    static {
        VoxelShape lantern = Block.box(1, 0, 1, 15, 13, 15);
        VoxelShape cap = Block.box(0, 12, 0, 16, 15, 16);
        VoxelShape top = Block.box(5, 15, 5, 11, 16, 11);
        SHAPE_BIG = Shapes.or(lantern, cap, top);

        lantern = Block.box(5, 7, 5, 11, 13, 11);
        cap = Block.box(4, 12, 4, 12, 15, 12);
        top = Block.box(6, 15, 6, 10, 16, 10);
        SHAPE_SMALL = Shapes.or(lantern, cap, top);
    }
}
