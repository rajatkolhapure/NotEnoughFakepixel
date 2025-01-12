package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.utils.TablistParser;
import org.ginafro.notenoughfakepixel.variables.DungeonFloor;

public class sPlusReminder {

    private static boolean reminded = false;

    public static void reminder() {
        if (reminded) return;

        DungeonFloor floor = DungeonFloor.getFloor(ScoreboardUtils.currentFloor.name());
        if (floor == DungeonFloor.NONE || ScoreboardUtils.clearedPercentage == -1) return;

        int secretPercentage = TablistParser.secretPercentage;
        int secretNeeded = floor.getSecretPercentage();

        if (secretPercentage >= secretNeeded && ScoreboardUtils.clearedPercentage >= 100) {
            Minecraft.getMinecraft().ingameGUI.displayTitle(EnumChatFormatting.GOLD + "S+", "", 2, 100, 2);
            reminded = true;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.END ||
                Minecraft.getMinecraft().thePlayer == null ||
                Minecraft.getMinecraft().theWorld == null ||
                !Configuration.sPlusReminder) return;
        reminder();
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        reminded = false;
    }
}
