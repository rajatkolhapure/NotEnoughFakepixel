package org.ginafro.notenoughfakepixel.config.pages;

import cc.polyfrost.oneconfig.config.annotations.HUD;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.score.ScoreOverlay;

public class ScoreOverlayPage {

    private transient static final String HUD = "HUD";

    @HUD(name = "Score Overlay" , category = HUD, subcategory = "Score & Secrets")
    public static ScoreOverlay scoreOverlay = new ScoreOverlay();

}
