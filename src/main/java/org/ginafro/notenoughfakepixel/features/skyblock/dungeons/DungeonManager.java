package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

public class DungeonManager {
    public static boolean checkEssentials() {
        return ScoreboardUtils.currentGamemode.isSkyblock() && ScoreboardUtils.currentLocation.isDungeon();
    }

    public static boolean checkEssentialsF7() {
        return checkEssentials() && ScoreboardUtils.currentFloor.name().equals("F7");
    }
}
