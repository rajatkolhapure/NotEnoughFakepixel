package org.ginafro.notenoughfakepixel.features.skyblock.mining;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Location;

import java.util.regex.Pattern;

public class EventsMsgSupressor {

    // message start example §r§e[NPC] §r§bDon Espresso§r§f:
    private Pattern donEspressoPattern = Pattern.compile("§r§e\\[NPC] §r§bDon Espresso§r§f:");
    //info message
    /*
        §b[PLAYER INFORMATION]
        §fWant to get the most out of the game?
        §bCheck out our shop §fand enjoy the best combination of
        §fprice and quality, get access to a variety of
        §bunique features §fand additional bonuses!
    */
    private Pattern watchdogPattern = Pattern.compile("§4\\[WATCHDOG ANNOUNCEMENT]\n");
    private Pattern infoPattern = Pattern.compile("§b\\[PLAYER INFORMATION]\n");
    private Pattern friendJoinPattern = Pattern.compile("§aFriend > ");

    @SubscribeEvent
    public void onChatRecieve(ClientChatReceivedEvent e){
        if(Minecraft.getMinecraft().thePlayer == null) return;
        checkMessageMatches(e);
    }

    private void checkMessageMatches(ClientChatReceivedEvent e) {
        checkDonEspressoMessage(e);
        checkWatchdogMessage(e);
        checkFriendJoinMessage(e);
    }

    private void checkDonEspressoMessage(ClientChatReceivedEvent e) {
        if (!Configuration.disableDonEspresso) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (ScoreboardUtils.currentLocation != Location.DWARVEN) return;
        if (donEspressoPattern.matcher(e.message.getFormattedText()).find()) {
            e.setCanceled(true);
        }
    }

    private void checkWatchdogMessage(ClientChatReceivedEvent e) {
        if (!Configuration.disableWatchdogInfo) return;
        //System.out.println("Checking message " + e.message.getFormattedText());
        if (watchdogPattern.matcher(e.message.getFormattedText()).find() ||
            infoPattern.matcher(e.message.getFormattedText()).find()) {
            e.setCanceled(true);
        }
    }

    private void checkFriendJoinMessage(ClientChatReceivedEvent e) {
        if (!Configuration.disableFriendJoin) return;
        if (friendJoinPattern.matcher(e.message.getFormattedText()).find()) {
            e.setCanceled(true);
        }
    }

}
