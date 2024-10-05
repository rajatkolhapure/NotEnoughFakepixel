package org.ginafro.notenoughfakepixel.features.mlf;


import cc.polyfrost.oneconfig.hud.BasicHud;
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.variables.Gamemode;

public class Map extends BasicHud {


    public Map(){

    }

    @Override
    protected void draw(UMatrixStack matrices, float x, float y, float s, boolean example) {
        if(Configuration.mlf && Configuration.map.isEnabled()){
            if(NotEnoughFakepixel.currentGamemode == Gamemode.MLF){
                Minecraft mc = Minecraft.getMinecraft();
                mc.getTextureManager().bindTexture(new ResourceLocation("notenoughfakepixel:mlf/map.png"));
                float scale = 64 * s;
                Gui.drawScaledCustomSizeModalRect((int)x , (int)y, 0F,0F,(int)scale,(int)scale,(int)scale,(int)scale,scale,scale );
            }
        }
    }

    @Override
    protected boolean shouldDrawBackground() {
        if(Configuration.mlf && Configuration.map.isEnabled() && NotEnoughFakepixel.currentGamemode == Gamemode.MLF){
            return true;
        }
        return false;
    }

    @Override
    protected float getWidth(float scale, boolean example) {
        return 64 * scale;
    }

    @Override
    protected float getHeight(float scale, boolean example) {
        return 64 * scale;
    }
}
