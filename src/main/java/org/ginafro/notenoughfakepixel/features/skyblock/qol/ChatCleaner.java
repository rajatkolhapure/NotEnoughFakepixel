package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

import java.util.regex.Pattern;

public class ChatCleaner {

    private Pattern sellingRankPattern = Pattern.compile("(?<rank>\\[[A-Za-z0-9_+]+\\] )?(?<username>\\w+:) (?<message>.*\\bselling\\b.*\\brank(s)?\\b.*)");
    private Pattern watchdogPattern = Pattern.compile("§4\\[WATCHDOG ANNOUNCEMENT]\n");
    private Pattern infoPattern = Pattern.compile("§b\\[PLAYER INFORMATION]\n");
    private Pattern friendJoinPattern = Pattern.compile("§aFriend > ");

    @SubscribeEvent
    public void onChatRecieve(ClientChatReceivedEvent event){
        if (Minecraft.getMinecraft().thePlayer == null) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;

        cancelMessage(Configuration.disableSellingRanks, event, sellingRankPattern);
        cancelMessage(Configuration.disableWatchdogInfo, event, watchdogPattern);
        cancelMessage(Configuration.disableWatchdogInfo, event, infoPattern);
        cancelMessage(Configuration.disableFriendJoin, event, friendJoinPattern);
    }

    private void cancelMessage(boolean option, ClientChatReceivedEvent e, Pattern pattern){
        if (!option) return;
        String message = e.message.getUnformattedText();
        if (pattern.matcher(message).matches()){
            e.setCanceled(true);
        }
    }

}
