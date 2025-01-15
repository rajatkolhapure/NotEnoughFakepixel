package org.ginafro.notenoughfakepixel.config.pages;

import cc.polyfrost.oneconfig.config.annotations.HUD;
import org.ginafro.notenoughfakepixel.features.skyblock.mining.MiningOverlay;

public class MiningOverlayPage {

    private transient static final String HUD = "HUD";

    @HUD(name = "Commission overlay" , category = HUD, subcategory = "Mining Overlay")
    public static MiningOverlay miningOverlay = new MiningOverlay();
}
