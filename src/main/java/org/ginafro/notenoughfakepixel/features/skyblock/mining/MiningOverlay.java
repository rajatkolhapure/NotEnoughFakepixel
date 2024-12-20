package org.ginafro.notenoughfakepixel.features.skyblock.mining;

import cc.polyfrost.oneconfig.hud.TextHud;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.utils.TablistParser;
import org.ginafro.notenoughfakepixel.variables.Location;

import java.util.List;

public class MiningOverlay extends TextHud {

    private static int LINE_HEIGHT = 11;
    private static float MINIMUM_WIDTH = 20;

    public MiningOverlay() {
        super(true);
    }

    @Override
    protected void getLines(List<String> lines, boolean example) {
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (!ScoreboardUtils.currentLocation.equals(Location.DWARVEN)) return;

        if (Configuration.abilityCooldown) {lines.add("\u00a77Ability Cooldown: \u00a7r" + AbilityNotifier.cdSecondsRemaining());}
        if (Configuration.mithrilPowder) {lines.add(formatMithrilPowder(TablistParser.mithilPowder));}
        if (Configuration.drillFuel) {lines.add(DrillFuelParsing.getString());}
        for(String commission : TablistParser.commissions){
            lines.add(formatCommission(commission));
        }
    }

    @Override
    protected float getWidth(float scale, boolean example) {
        float var = Math.max(getLongestCommision()*6, MINIMUM_WIDTH);
        return var * scale;
    }

    @Override
    protected float getHeight(float scale, boolean example) {
        int LINE_HEIGHT = 11;
        int variable = Configuration.drillFuel ? LINE_HEIGHT : 0;
        variable = Configuration.mithrilPowder ? variable + LINE_HEIGHT : variable;
        variable = Configuration.abilityCooldown ? variable + LINE_HEIGHT : variable;
        return ( variable + (TablistParser.commissions.size()*LINE_HEIGHT)) * scale;
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
        return longest < 20 ? 20 : longest;
    }
}
