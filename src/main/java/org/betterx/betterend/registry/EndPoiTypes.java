package org.betterx.betterend.registry;

import org.betterx.bclib.api.v2.poi.BCLPoiType;
import org.betterx.bclib.api.v2.poi.PoiManager;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.RunedFlavolite;

import java.util.Set;

public class EndPoiTypes {
    public static final BCLPoiType ETERNAL_PORTAL_INACTIVE = PoiManager.register(
            BetterEnd.makeID("eternal_portal_inactive"),
            Set.of(EndBlocks.FLAVOLITE_RUNED_ETERNAL.defaultBlockState().setValue(RunedFlavolite.ACTIVATED, false)),
            0, 1
    );

    public static final BCLPoiType ETERNAL_PORTAL_ACTIVE = PoiManager.register(
            BetterEnd.makeID("eternal_portal_active"),
            Set.of(EndBlocks.FLAVOLITE_RUNED_ETERNAL.defaultBlockState().setValue(RunedFlavolite.ACTIVATED, true)),
            0, 1
    );

    public static void register() {

    }
}
