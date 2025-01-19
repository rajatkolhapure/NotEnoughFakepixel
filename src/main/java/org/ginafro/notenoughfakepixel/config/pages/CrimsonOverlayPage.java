package org.ginafro.notenoughfakepixel.config.pages;

import cc.polyfrost.oneconfig.config.annotations.HUD;
import org.ginafro.notenoughfakepixel.features.skyblock.crimson.CrimsonOverlay;
import org.ginafro.notenoughfakepixel.features.skyblock.mining.MiningOverlay;

public class CrimsonOverlayPage {

    private transient static final String HUD = "HUD";

    @HUD(name = "Commission overlay" , category = HUD, subcategory = "Crimson Overlay")
    public static CrimsonOverlay crimsonOverlay = new CrimsonOverlay();
}
