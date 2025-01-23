package org.ginafro.notenoughfakepixel.config.pages;

import cc.polyfrost.oneconfig.config.annotations.HUD;
import org.ginafro.notenoughfakepixel.features.skyblock.crimson.AshfangOverlay;

public class AshfangOverlayPage {

    private transient static final String HUD = "HUD";

    @HUD(name = "Crimson overlay" , category = HUD, subcategory = "Crimson Overlay")
    public static AshfangOverlay ashfangOverlay = new AshfangOverlay();
}
