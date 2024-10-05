package org.ginafro.notenoughfakepixel.features.other;


import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;

public class Fullbright {

    private float oldGamma;
    @SubscribeEvent
    public void onConfig(ConfigChangedEvent.OnConfigChangedEvent e){
        changeBrightness();
    }

    public void changeBrightness(){
        oldGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
        if(!Configuration.fullbright){
            return;
        }
        Minecraft.getMinecraft().gameSettings.gammaSetting = Configuration.gamma;
    }

}
