package org.ginafro.notenoughfakepixel.config.pages;

import cc.polyfrost.oneconfig.config.annotations.HUD;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.SecretOverlay;

public class SecretOverlayPage {

    private transient static final String HUD = "HUD";

    @HUD(name = "Secret Display" , category = HUD, subcategory = "Secrets")
    public static SecretOverlay secretDisplay = new SecretOverlay();

}
