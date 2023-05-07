package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.blocks.BaseRotatedPillarBlock;

import net.minecraft.resources.ResourceLocation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Optional;

public class LitPillarBlock extends BaseRotatedPillarBlock {
    private static final String PATTERN = "{\"parent\":\"betterend:block/pillar_noshade\",\"textures\":{\"end\":\"betterend:block/name_top\",\"side\":\"betterend:block/name_side\"}}";

    public LitPillarBlock(Properties settings) {
        super(settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    protected Optional<String> createBlockPattern(ResourceLocation blockId) {
        String name = blockId.getPath();
        return Optional.of(PATTERN.replace("name", name));
    }
}
