package org.ginafro.notenoughfakepixel.features.skyblock.fishing;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Location;

import java.util.Objects;

public class TrophyFishNotifier {

    @SubscribeEvent
    public void onChatRecieve(ClientChatReceivedEvent e){
        if(e.type != 1) return;
        if(Configuration.trophyFish){
            if(ScoreboardUtils.currentLocation != Location.CRIMSON) {
                if(ScoreboardUtils.currentLocation != Location.CRIMSON_FIELDS){
                    if(ScoreboardUtils.currentLocation != Location.SCARELTON){
                        if(ScoreboardUtils.currentLocation != Location.ASHFANG){
                            if(ScoreboardUtils.currentLocation != Location.VOLCANO){
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
