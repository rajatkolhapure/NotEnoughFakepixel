package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

public class MuteBosses {
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event){
        if (!ScoreboardUtils.currentLocation.isDungeon()) return;
        if (!Configuration.dungeonsMuteBosses) return;
        if (event.message.getUnformattedText().contains("[BOSS]")) event.setCanceled(true);
    }
}
