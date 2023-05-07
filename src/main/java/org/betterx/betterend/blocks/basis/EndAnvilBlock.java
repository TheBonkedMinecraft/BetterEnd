package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.blocks.LeveledAnvilBlock;
import org.betterx.betterend.complexmaterials.MetalMaterial;

import net.minecraft.world.level.material.MaterialColor;

public class EndAnvilBlock extends LeveledAnvilBlock {
    protected MetalMaterial metalMaterial;

    public EndAnvilBlock(MaterialColor color, int level) {
        super(color, level);
    }

    public EndAnvilBlock(MetalMaterial metalMaterial, MaterialColor color, int level) {
        super(color, level);
        this.metalMaterial = metalMaterial;
    }
}
