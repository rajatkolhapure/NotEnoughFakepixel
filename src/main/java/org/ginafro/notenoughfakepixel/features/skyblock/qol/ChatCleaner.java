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
    private Pattern middleBar = Pattern.compile("(§6|§c)[0-9]+/[0-9]+❤(.)+");

    @SubscribeEvent
    public void onChatRecieve(ClientChatReceivedEvent event){
        if (Minecraft.getMinecraft().thePlayer == null) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (middleBar.matcher(event.message.getFormattedText()).matches()) return;

        cancelMessage(Configuration.disableSellingRanks, event, sellingRankPattern);
        cancelMessage(Configuration.disableWatchdogInfo, event, watchdogPattern, true);
        cancelMessage(Configuration.disableWatchdogInfo, event, infoPattern, true);
        cancelMessage(Configuration.disableFriendJoin, event, friendJoinPattern, true);
    }

    private void cancelMessage(boolean option, ClientChatReceivedEvent e, Pattern pattern, boolean formatted){
        if (!option) return;
        String message = e.message.getUnformattedText();
        if (formatted) message = e.message.getFormattedText();
        if (pattern.matcher(message).find() || pattern.matcher(message).matches()){
            e.setCanceled(true);
        }
    }

    private void cancelMessage(boolean option, ClientChatReceivedEvent e, Pattern pattern){
        cancelMessage(option, e, pattern, false);
    }

}
