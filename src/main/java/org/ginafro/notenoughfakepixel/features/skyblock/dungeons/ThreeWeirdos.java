package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThreeWeirdos {

    /* VERIFIED ANSWERS

    "The reward is not in my chest!",
    "At least one of them is lying, and the reward is not in ",
    --
    "My chest has the reward and I'm telling the truth!",
    --
    --
    --

    * */

    private String[] answers = {
            "The reward is not in my chest!",
            "At least one of them is lying, and the reward is not in ",
            "My chest doesn't have the reward we are all telling the truth.",
            "My chest has the reward and I'm telling the truth!",
            "The reward isn't in any of our chests.",
            "Both of them are telling the truth. Also, "
    };

    private Pattern threeWeirdosPattern = Pattern.compile("(§r§e\\[NPC] §r§c)(.+)(§r§f:)(.+)");

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onChatRecieve(ClientChatReceivedEvent e){
       if (!Configuration.threeWeirdos) return;
       if (mc.thePlayer == null) return;
       if (!ScoreboardUtils.currentGamemode.isSkyblock());
       if (!ScoreboardUtils.currentLocation.isDungeon()) return;

       if(e.message.getFormattedText().startsWith("§c")) return;

       Matcher matcher = threeWeirdosPattern.matcher(e.message.getFormattedText());
       if(matcher.find()){
           String name = matcher.group(2);
           String message = matcher.group(4);
           for (String answer : answers) {
               if (message.contains(answer)) {
                   Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§a[Riddle Solver] §r§c" + name + "§r§f has the blessing!"));
                   e.setCanceled(true);
                   return;
               }
           }
       }
    }

}
