package org.ginafro.notenoughfakepixel.features.skyblock.fishing;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.jetbrains.annotations.NotNull;

public class GreatCatchNotifier {

    @SubscribeEvent
    public void onChat(@NotNull ClientChatReceivedEvent e){
        Minecraft mc = Minecraft.getMinecraft();
        if(e.type != 1)return;
        if(!Configuration.greatCatch) return;
        if(e.message.getUnformattedText().contains("The Sea Emperor arises")){
            mc.ingameGUI.displayTitle(EnumChatFormatting.WHITE + "Legendary Catch!" , "The Sea Emperor" , 2 , 70,2);
        }else if(e.message.getUnformattedText().contains("The Water Hydra has come")){
            mc.ingameGUI.displayTitle(EnumChatFormatting.WHITE + "Legendary Catch!" , "Water Hydra" , 2 , 70,2);
        }else if(e.message.getUnformattedText().contains("An Abyssal Miner breaks")){
            mc.ingameGUI.displayTitle(EnumChatFormatting.WHITE + "Legendary Catch!" , "Abyssal Miner" , 2 , 70,2);
        }else if(e.message.getUnformattedText().contains("The spirit of a long lost Phantom ")){
            mc.ingameGUI.displayTitle(EnumChatFormatting.WHITE + "Legendary Catch!" , "Phantom Fisher" , 2 , 70,2);
        }else if(e.message.getUnformattedText().contains("This can't be! The manifestation of death himself!")){
            mc.ingameGUI.displayTitle(EnumChatFormatting.WHITE + "Legendary Catch!" , "Grim Reaper" , 2 , 70,2);
        }else if(e.message.getUnformattedText().contains("WOAH! A Plhlegblast appeared.")){
            mc.ingameGUI.displayTitle(EnumChatFormatting.WHITE + "Legendary Catch!" , "Plhlegblast" , 2 , 70,2);
        }else if(e.message.getUnformattedText().contains("What is this creature!?")){
            mc.ingameGUI.displayTitle(EnumChatFormatting.WHITE + "Legendary Catch!" , "Yeti" , 2 , 70,2);
        }else if(e.message.getUnformattedText().contains("A Reindrake forms from the depths.")){
            mc.ingameGUI.displayTitle(EnumChatFormatting.WHITE + "Legendary Catch!" , "Reindrake" , 2 , 70,2);
        }else if(e.message.getUnformattedText().contains("Hide no longer, a Great White Shark")){
            mc.ingameGUI.displayTitle(EnumChatFormatting.WHITE + "Legendary Catch!" , "Great White Shark" , 2 , 70,2);
        }else if(e.message.getUnformattedText().contains("You hear a massive rumble as")){
            mc.ingameGUI.displayTitle(EnumChatFormatting.WHITE + "Legendary Catch!" , "Thunder" , 2 , 70,2);
        }else if(e.message.getUnformattedText().contains("You have angered a legendary creature.")){
            mc.ingameGUI.displayTitle(EnumChatFormatting.WHITE + "Legendary Catch!" , "Lord Jawbus" , 2 , 70,2);
        }
    }

}
