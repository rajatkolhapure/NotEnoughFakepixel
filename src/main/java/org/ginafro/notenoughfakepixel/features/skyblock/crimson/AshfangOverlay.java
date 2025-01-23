package org.ginafro.notenoughfakepixel.features.skyblock.crimson;
import cc.polyfrost.oneconfig.hud.TextHud;
import net.minecraft.client.Minecraft;
import org.ginafro.notenoughfakepixel.Configuration;

import java.util.List;

public class AshfangOverlay extends TextHud {

    private static int LINE_HEIGHT = 11;
    private static int MINIMUM_WIDTH = 20;
    //NumberFormat formatter = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
    private static char[] c = new char[]{'k', 'M'};

    public AshfangOverlay() {
        super(true);
    }

    @Override
    protected void getLines(List<String> lines, boolean example) {
        if (Crimson.checkEssentials()) return;
        if (Configuration.ashfangOverlay) {lines.add("\u00a77Ashfang HP: \u00a7r" + coolFormat(AshfangHelper.getAshfangHP(),0));}
        if (Configuration.ashfangOverlay) {lines.add("\u00a77Blazing souls to launch: \u00a7r" + AshfangHelper.getBlazingSoulCounter() + " / "+AshfangHelper.getHitsNeeded());}
    }

    @Override
    protected boolean shouldShow() {
        if (!super.shouldShow()) {
            return false;
        }
        return (Configuration.ashfangOverlay && Crimson.checkAshfangArea(new int[]{Minecraft.getMinecraft().thePlayer.getPosition().getX(), Minecraft.getMinecraft().thePlayer.getPosition().getY(), Minecraft.getMinecraft().thePlayer.getPosition().getZ()}));
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

    /*
    @Override
    protected float getWidth(float scale, boolean example) {
        float var = MINIMUM_WIDTH;
        return var * scale;
    }*/
}
