package org.ginafro.notenoughfakepixel.utils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;
import org.ginafro.notenoughfakepixel.variables.Gamemode;
import org.ginafro.notenoughfakepixel.variables.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardUtils {

    public static Location currentLocation = Location.NONE;
    public static Gamemode currentGamemode = Gamemode.LOBBY;
    public static boolean inDungeons = false;

    public static void parseScoreboard(){
        Minecraft mc = Minecraft.getMinecraft();

        if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.contains("fakepixel")) {
            ScoreObjective objective = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            if (objective != null) {
                String objName = ScoreboardUtils.cleanSB(objective.getDisplayName());
                currentGamemode = Gamemode.getGamemode(objName);
            }
        }

        if (currentGamemode == Gamemode.SKYBLOCK && !mc.isSingleplayer() && mc.getNetHandler() != null) {
            for (NetworkPlayerInfo playerInfo : mc.getNetHandler().getPlayerInfoMap()) {
                IChatComponent s1 = playerInfo.getDisplayName();
                if (s1 != null) {
                    String name = StringUtils.stripControlCodes(s1.getUnformattedText());
                    if (name.contains("Area")) {
                        currentGamemode = Gamemode.SKYBLOCK;
                        currentLocation = Location.getLocation(name.replace("Area: ", ""));
                    } else if (name.contains("Dungeon")) {
                        currentGamemode = Gamemode.SKYBLOCK;
                        inDungeons = true;
                    }
                }
            }
        }
    }



    public static String cleanSB(String scoreboard) {
        char[] nvString = StringUtils.stripControlCodes(scoreboard).toCharArray();
        StringBuilder cleaned = new StringBuilder();

        for (char c : nvString) {
            if ((int) c > 20 && (int) c < 127) {
                cleaned.append(c);
            }
        }

        return cleaned.toString();
    }

    public static List<String> getSidebarLines() {
        List<String> lines = new ArrayList<>();
        if (Minecraft.getMinecraft().theWorld == null) return lines;
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        if (scoreboard == null) return lines;

        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) return lines;

        Collection<Score> scores;
        try {
            scores = scoreboard.getSortedScores(objective);
        } catch (ConcurrentModificationException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }

        List<Score> list = scores.stream()
                .filter(input -> input != null && input.getPlayerName() != null && !input.getPlayerName()
                        .startsWith("#"))
                .collect(Collectors.toList());

        if (list.size() > 15) {
            scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
        } else {
            scores = list;
        }

        for (Score score : scores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
        }

        return lines;
    }

}