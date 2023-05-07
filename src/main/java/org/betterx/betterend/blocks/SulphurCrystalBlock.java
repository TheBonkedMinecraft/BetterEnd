package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BaseAttachedBlock;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.bclib.interfaces.tools.AddMineablePickaxe;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

@SuppressWarnings("deprecation")
public class SulphurCrystalBlock extends BaseAttachedBlock implements AddMineablePickaxe, RenderLayerProvider, SimpleWaterloggedBlock, LiquidBlockContainer {
    private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(Direction.class);
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 2);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public SulphurCrystalBlock() {
        super(FabricBlockSettings.of(Material.STONE)
                                 .materialColor(MaterialColor.COLOR_YELLOW)
                                 .sound(SoundType.GLASS)
                                 .requiresCorrectToolForDrops()
                                 .noCollission());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(AGE, WATERLOGGED);
    }

    @Override
    public BCLRenderLayer getRenderLayer() {
        return BCLRenderLayer.CUTOUT;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return state.getValue(AGE) < 2 ? Collections.emptyList() : Lists.newArrayList(new ItemStack(
                EndItems.CRYSTALLINE_SULPHUR,
                MHelper.randRange(1, 3, MHelper.RANDOM_SOURCE)
        ));
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
        return !state.getValue(WATERLOGGED);
    }

    @Override
    public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
        return !state.getValue(WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        if (state != null) {
            LevelReader worldView = ctx.getLevel();
            BlockPos blockPos = ctx.getClickedPos();
            boolean water = worldView.getFluidState(blockPos).getType() == Fluids.WATER;
            return state.setValue(WATERLOGGED, water);
        }
        return null;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
        return BOUNDING_SHAPES.get(state.getValue(FACING));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction.getOpposite());
        return world.getBlockState(blockPos).is(EndBlocks.BRIMSTONE);
    }

    static {
        BOUNDING_SHAPES.put(Direction.UP, Shapes.box(0.125, 0.0, 0.125, 0.875F, 0.5, 0.875F));
        BOUNDING_SHAPES.put(Direction.DOWN, Shapes.box(0.125, 0.5, 0.125, 0.875F, 1.0, 0.875F));
        BOUNDING_SHAPES.put(Direction.NORTH, Shapes.box(0.125, 0.125, 0.5, 0.875F, 0.875F, 1.0));
        BOUNDING_SHAPES.put(Direction.SOUTH, Shapes.box(0.125, 0.125, 0.0, 0.875F, 0.875F, 0.5));
        BOUNDING_SHAPES.put(Direction.WEST, Shapes.box(0.5, 0.125, 0.125, 1.0, 0.875F, 0.875F));
        BOUNDING_SHAPES.put(Direction.EAST, Shapes.box(0.0, 0.125, 0.125, 0.5, 0.875F, 0.875F));
    }
}
