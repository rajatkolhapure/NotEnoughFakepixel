package org.ginafro.notenoughfakepixel.features.skyblock.crimson;
import cc.polyfrost.oneconfig.hud.TextHud;
import net.minecraft.client.Minecraft;
import org.ginafro.notenoughfakepixel.Configuration;

import java.util.List;

public class AshfangOverlay extends TextHud {

    private static char[] c = new char[]{'k', 'M'};

    public AshfangOverlay() {
        super(true);
    }

    @Override
    protected void getLines(List<String> lines, boolean example) {
        if (Crimson.checkEssentials()) return;
        if (Configuration.ashfangOverlay) {lines.add("\u00a77Ashfang HP: \u00a7r" + formatAshfangHP(AshfangHelper.getAshfangHP()));}
        if (Configuration.ashfangOverlay) {lines.add("\u00a77Blazing souls: \u00a7r" + AshfangHelper.getBlazingSoulCounter() + " / "+AshfangHelper.getHitsNeeded());}
    }

    @Override
    protected boolean shouldShow() {
        if (!super.shouldShow()) {
            return false;
        }
        return (Configuration.ashfangOverlay && Crimson.checkAshfangArea(new int[]{Minecraft.getMinecraft().thePlayer.getPosition().getX(), Minecraft.getMinecraft().thePlayer.getPosition().getY(), Minecraft.getMinecraft().thePlayer.getPosition().getZ()}));
    }

    private static String formatAshfangHP(double hp) {
        // Get the percentage of HP remaining
        double percentage = hp / 50_000_000;

        // Determine the prefix based on the percentage
        String prefix;
        if (percentage > 0.5) {
            prefix = "§a"; // Green
        } else if (percentage > 0.1) {
            prefix = "§e"; // Yellow
        } else {
            prefix = "§c"; // Red
        }

        // Format the HP using coolFormat
        String formattedHP = coolFormat(hp, 0);

        // Add the prefix to the formatted HP
        return prefix + formattedHP;
    }


    private static String coolFormat(double n, int iteration) {
        double d = ((long) n / 100) / 10.0;
        boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
        return (d < 1000? //this determines the class, i.e. 'k', 'm' etc
                ((d > 99.9 || isRound || (!isRound && d > 9.99)? //this decides whether to trim the decimals
                        (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
                ) + "" + c[iteration])
                : coolFormat(d, iteration+1));

    }
}
