package org.ginafro.notenoughfakepixel.features.skyblock.mining;

import cc.polyfrost.oneconfig.hud.TextHud;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.utils.TablistParser;
import org.ginafro.notenoughfakepixel.variables.Location;

import java.util.List;

public class MiningOverlay extends TextHud {

    public MiningOverlay() {
        super(true);
    }

    @Override
    protected void getLines(List<String> lines, boolean example) {
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (!ScoreboardUtils.currentLocation.equals(Location.DWARVEN)) return;
        lines.add(formatMithrilPowder(TablistParser.mithilPowder));
        lines.add("");
        for(String commission : TablistParser.commissions){
            lines.add(formatCommission(commission));
        }
    }

    @Override
    protected float getWidth(float scale, boolean example) {
        return (getLongestCommision()*6) * scale;
    }

    @Override
    protected float getHeight(float scale, boolean example) {
        return (24 + (TablistParser.commissions.size()*10)) * scale;
    }

    private String formatMithrilPowder(long mithrilPowder) {
        return String.format("\u00a77Mithril Powder: \u00a72%d", mithrilPowder);
    }

    private String formatCommission(String commission) {
        // IF percentage is 0 to 33, color is §c
        // IF percentage is 34 to 79, color is §e
        // IF percentage is 80 to 100, color is §a
        Double percent = Double.parseDouble(commission.split(":")[1].replaceAll("[ %]", ""));
        String colorCode = percent <= 33 ? "\u00a7c" : percent <= 79 ? "\u00a7e" : "\u00a7a";
        return "\u00a77" + commission.split(":")[0] + ": " + colorCode + percent + "%";
    }

    private int getLongestCommision() {
        int longest = 0;
        for(String commission : TablistParser.commissions){
            if(commission.length() > longest){
                longest = commission.length();
            }
        }
        return longest;
    }
}
