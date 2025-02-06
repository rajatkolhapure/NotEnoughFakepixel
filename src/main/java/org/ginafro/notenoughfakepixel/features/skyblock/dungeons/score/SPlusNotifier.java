package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.score;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.DungeonManager;

public class SPlusNotifier {

    private static boolean reminded = true;

    public static void reminder() {
        if (reminded) return;
        if (!Configuration.dungeonsSPlusNotifier && !Configuration.dungeonsSPlusMessage) return;

        if (ScoreManager.getRequiredSecretNeeded() == -1) {
            if (Configuration.dungeonsSPlusMessage) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc S+ cannot be reached, restart");
            }
            reminded = true;
            return;
        }

        if (ScoreManager.getSecretPercentage() >= ScoreManager.getRequiredSecretNeeded()) {
            if (Configuration.dungeonsSPlusNotifier) {
                Minecraft.getMinecraft().ingameGUI.displayTitle(EnumChatFormatting.GOLD + "S+", "", 2, 100, 2);
            }
            if (Configuration.dungeonsSPlusMessage) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc S+ virtually reached, get 100% completion and enter portal!");
            }
            reminded = true;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (!DungeonManager.checkEssentials() ||
                e.phase == TickEvent.Phase.END ||
                Minecraft.getMinecraft().thePlayer == null ||
                Minecraft.getMinecraft().theWorld == null) return;
        reminder();
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (DungeonManager.checkEssentials()) {
            if (event.message.getUnformattedText().equals("[NPC] Mort: Good luck.")) {
                reminded = false;
            }
        }
    }
}
