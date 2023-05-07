package org.betterx.betterend.blocks;

import org.betterx.bclib.blocks.BaseBlock;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.bclib.interfaces.tools.AddMineableAxe;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class AmaranitaHymenophoreBlock extends BaseBlock implements RenderLayerProvider, AddMineableAxe {
    public AmaranitaHymenophoreBlock() {
        super(FabricBlockSettings.of(Material.WOOD).sound(SoundType.WOOD));
    }

    @Override
    public BCLRenderLayer getRenderLayer() {
        return BCLRenderLayer.CUTOUT;
    }
}
