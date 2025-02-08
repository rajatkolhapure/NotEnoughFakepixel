package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import lombok.Getter;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

public class DungeonManager {
    private static boolean isFinalStage = false;
    public static boolean checkEssentials() {
        return ScoreboardUtils.currentGamemode.isSkyblock() && ScoreboardUtils.currentLocation.isDungeon();
    }

    public static boolean checkEssentialsF7() {
        return checkEssentials() && (ScoreboardUtils.currentFloor.name().equals("F7") || ScoreboardUtils.currentFloor.name().equals("M7"));
    }

    public static boolean isFinalStage() {
        return isFinalStage;
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!DungeonManager.checkEssentials()) return;
        if (event.message.getUnformattedText().contains("> EXTRA STATS <")) {
            isFinalStage = true;
        }
    }

    @SubscribeEvent()
    public void onWorldUnload(WorldEvent.Unload event) {
        if (!DungeonManager.checkEssentials()) return;
        isFinalStage = false;
    }

    @SubscribeEvent()
    public void onWorldLoad(WorldEvent.Load event) {
        if (!DungeonManager.checkEssentials()) return;
        isFinalStage = false;
    }
}
