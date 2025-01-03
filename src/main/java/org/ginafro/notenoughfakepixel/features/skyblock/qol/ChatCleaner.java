package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

public class ChatCleaner {

    @SubscribeEvent
    public void onChatRecieve(ClientChatReceivedEvent e){
        if (Minecraft.getMinecraft().thePlayer == null) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;

    }


}
