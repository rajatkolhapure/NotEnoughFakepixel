package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.hud.TextHud;
import net.minecraft.util.EnumChatFormatting;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.utils.TablistParser;
import org.ginafro.notenoughfakepixel.variables.DungeonFloor;

import java.util.List;

import static cc.polyfrost.oneconfig.libs.universal.UGraphics.getCharWidth;

public class SecretOverlay extends TextHud {

    public SecretOverlay() {
        super(true,
                0,
                0,
                1, true, true,
                4f, 5, 5,
                new OneColor(0, 0, 0, 150),
                false, 2,
                new OneColor(0, 0, 0, 127));
    }

    private String centerText(String text, float maxWidth) {
        float lineWidth = getLineWidth(EnumChatFormatting.getTextWithoutFormattingCodes(text).trim(), scale);
        float charWidth = getCharWidth(' ') * scale;
        int spaces = (int) ((maxWidth - lineWidth) / charWidth);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < spaces / 2; i++) {
            builder.append(" ");
        }
        return builder + text;
    }

    protected float getWidth(List<String> lines) {
        if (lines == null) return 0;
        float width = 0;
        for (String line : lines) {
            width = Math.max(width, getLineWidth(EnumChatFormatting.getTextWithoutFormattingCodes(line).trim(), scale));
        }
        return width;
    }

    @Override
    protected boolean shouldShow() {
        if (!super.shouldShow()) return false;
        if (!ScoreboardUtils.currentLocation.isDungeon()) return false;

        return Configuration.dungeonsSecretOverlay;
    }

    @Override
    protected void getLines(List<String> lines, boolean example) {
        String line = EnumChatFormatting.GRAY + "Secrets: " + EnumChatFormatting.WHITE + TablistParser.secretPercentage + "%";
        lines.add(centerText(getSecretDisplay(), getWidth(lines)));
    }

    private String getSecretDisplay() {
        String returnString = "\u00a77Secrets: ";
        int secretPercentage = TablistParser.secretPercentage;
        int secretNeeded = DungeonFloor.getFloor(ScoreboardUtils.currentFloor.name()).getSecretPercentage();
        if (secretPercentage == -1) returnString = returnString + "\u00a7cN/A";
        else returnString = returnString + (
                secretPercentage > secretNeeded ? "\u00a7a" : "\u00a7c"
        ) + secretPercentage + "%";

        if (secretPercentage >= secretNeeded && ScoreboardUtils.clearedPercentage >= 100) {
            returnString = returnString + " \u00a76(S+)";
        }

        return returnString;
    }



}