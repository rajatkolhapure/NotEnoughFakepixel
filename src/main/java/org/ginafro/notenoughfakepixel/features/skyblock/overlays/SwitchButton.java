package org.ginafro.notenoughfakepixel.features.skyblock.overlays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

public class SwitchButton extends GuiButton {

    public SwitchButton(int buttonId, int x, int y) {
        super(buttonId, x, y,200 , 20, "");
    }
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if(!this.visible) return;
        if(StorageOverlay.echest) {
            Gui.drawRect(xPosition, yPosition, xPosition + width / 2, yPosition + height, Color.black.getRGB());
            Gui.drawRect(xPosition + width / 2 , yPosition , xPosition + width , yPosition + height , new Color(0,0,0,102).getRGB());
        }else {
            Gui.drawRect(xPosition, yPosition, xPosition + width / 2, yPosition + height, new Color(0,0,0,102).getRGB());
            Gui.drawRect(xPosition + width / 2 , yPosition , xPosition + width , yPosition + height ,Color.black.getRGB());
        }
        this.drawString(mc.fontRendererObj , "Ender Chest", xPosition + 3 , yPosition + height/2 - mc.fontRendererObj.FONT_HEIGHT/2,-1);
        this.drawString(mc.fontRendererObj , "Backpacks", xPosition + width / 2 + 3 , yPosition + height/2 - mc.fontRendererObj.FONT_HEIGHT/2,-1);
    }

   public void Switch(){
        StorageOverlay.echest = !StorageOverlay.echest;
   }
}
