package org.ginafro.notenoughfakepixel.features.skyblock.crimson;

import net.minecraft.client.Minecraft;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

import java.util.ArrayList;
import java.util.List;

public class Crimson {

    private static int[][] ashfangArea = new int[][]{{-510,100,-1040}, {-450,200,-990}};
    public static boolean checkEssentials(){
        return (Minecraft.getMinecraft().thePlayer == null) ||
                (!ScoreboardUtils.currentGamemode.isSkyblock()) ||
                (!ScoreboardUtils.currentLocation.isCrimson());
    }
    public static boolean checkAshfangArea(int[] coords){
        return coords[0] >= ashfangArea[0][0] && coords[0] <= ashfangArea[1][0] &&
                coords[1] >= ashfangArea[0][1] && coords[1] <= ashfangArea[1][1] &&
                coords[2] >= ashfangArea[0][2] && coords[2] <= ashfangArea[1][2];
    }
}
