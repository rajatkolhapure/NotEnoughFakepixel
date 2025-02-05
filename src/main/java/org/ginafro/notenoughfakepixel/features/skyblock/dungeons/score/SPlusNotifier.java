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

        if (ScoreManager.getSecretPercentage() >= ScoreManager.getRequiredSecretNeeded()) {
            Minecraft.getMinecraft().ingameGUI.displayTitle(EnumChatFormatting.GOLD + "S+", "", 2, 100, 2);
            Minecraft.getMinecraft().thePlayer.sendChatMessage("S+ virtually reached, get 100% completion!");
            reminded = true;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (!DungeonManager.checkEssentials() ||
                e.phase == TickEvent.Phase.END ||
                Minecraft.getMinecraft().thePlayer == null ||
                Minecraft.getMinecraft().theWorld == null ||
                !Configuration.dungeonsSPlusNotifier) return;
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
