package org.ginafro.notenoughfakepixel.utils;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import org.ginafro.notenoughfakepixel.mixin.AccessorGuiPlayerTabOverlay;
import org.ginafro.notenoughfakepixel.variables.Area;
import org.ginafro.notenoughfakepixel.variables.Location;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class TablistParser {

    private static final Ordering<NetworkPlayerInfo> playerOrdering = Ordering.from(new PlayerComparator());
    private static long lastTime = 0;

    public static int mithilPowder = 0;
    public static List<String> commissions = new ArrayList<>();

    @SideOnly(Side.CLIENT)
    static class PlayerComparator implements Comparator<NetworkPlayerInfo> {
        private PlayerComparator() {}

        public int compare(NetworkPlayerInfo o1, NetworkPlayerInfo o2) {
            ScorePlayerTeam team1 = o1.getPlayerTeam();
            ScorePlayerTeam team2 = o2.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(
                o1.getGameType() != WorldSettings.GameType.SPECTATOR,
                o2.getGameType() != WorldSettings.GameType.SPECTATOR
            )
            .compare(
                    team1 != null ? team1.getRegisteredName() : "",
                    team2 != null ? team2.getRegisteredName() : ""
            )
            .compare(o1.getGameProfile().getName(), o2.getGameProfile().getName()).result();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e){
        if(lastTime < 20) {
            lastTime++;
            return;
        }
        if(e.phase != TickEvent.Phase.END) return;
        if(Minecraft.getMinecraft().thePlayer == null) return;
        if(!ScoreboardUtils.currentGamemode.isSkyblock()) return;

        if(Minecraft.getSystemTime() - lastTime < 20) return;

        List<NetworkPlayerInfo> players =
                playerOrdering.sortedCopy(Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap());

        List<String> result = new ArrayList<>();

        for (NetworkPlayerInfo info : players) {
            String name = Minecraft.getMinecraft().ingameGUI.getTabList()
                    .getPlayerName(info);
            result.add(name);
        }

        // Lists to store data
        List<String> serverInfo = new ArrayList<>();
        List<String> accountInfo = new ArrayList<>();

        // Flags to track sections
        boolean isServerInfo = false;
        boolean isAccountInfo = false;
        boolean foundCommisions = false;
        commissions = new ArrayList<>();

        // Regex to remove Minecraft formatting codes
        Pattern formatPattern = Pattern.compile("§[0-9a-fklmnor]");

        // Parse the data
        for (String line : result) {
            // Check for section headers
            if (line.contains("§3§l Server Info§r")) {
                isServerInfo = true;
                isAccountInfo = false;
                continue;
            } else if (line.contains("§6§lAccount Info§r")) {
                isServerInfo = false;
                isAccountInfo = true;
                continue;
            }

            // SERVER INFO SECTION
            if (isServerInfo) {
                String cleanLine = formatPattern.matcher(line).replaceAll("").trim();

                // Parsing mithril powder
                if (cleanLine.contains("Mithril Powder: ")) {
                    mithilPowder = Integer.parseInt(cleanLine.split(" ")[2].replace(",", ""));
                }

                // Parsing commisions

                if (foundCommisions) {
                    if (cleanLine.isEmpty()) {
                        foundCommisions = false;
                    } else {
                        commissions.add(cleanLine);
                    }
                }

                if(cleanLine.contains("Commissions")) {
                    foundCommisions = true;
                }

                serverInfo.add(cleanLine);

            // ACCOUNT INFO SECTION
            } else if (isAccountInfo) {
                accountInfo.add(formatPattern.matcher(line).replaceAll("").trim());
            }
        }

        // FOOTER (COOKIE & BOOSTER)
        try {
            String[] footer = ((AccessorGuiPlayerTabOverlay) Minecraft.getMinecraft().ingameGUI.getTabList())
                    .getFooter().getFormattedText()
                    .split("\n");
        } catch (Exception ignored) {}

        lastTime = 0;
    }
}
