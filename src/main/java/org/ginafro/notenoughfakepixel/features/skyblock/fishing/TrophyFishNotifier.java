package org.ginafro.notenoughfakepixel.features.skyblock.fishing;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Area;

public class TrophyFishNotifier {

    @SubscribeEvent
    public void onChatRecieve(ClientChatReceivedEvent e){
        if(e.type != 1) return;
        if(Configuration.trophyFish){
            if(ScoreboardUtils.currentArea != Area.CRIMSON) {
                if(ScoreboardUtils.currentArea != Area.CRIMSON_FIELDS){
                    if(ScoreboardUtils.currentArea != Area.SCARELTON){
                        if(ScoreboardUtils.currentArea != Area.ASHFANG){
                            if(ScoreboardUtils.currentArea != Area.VOLCANO){
                                return;
                            }
                        }
                    }
                }
            }
//            if(!Objects.equals(e.message.getUnformattedText().split(" ")[0], "TROPHY")) return;
            if(e.message.getUnformattedText().toLowerCase().contains("trophy fish!")){
                String fish = e.message.getUnformattedText().replace("TROPHY FISH!", "").replace("You caught a ", "");
                Minecraft.getMinecraft().ingameGUI.displayTitle("TROPHY FISH", fish, 1,20,1);
            }
        }
    }

}
