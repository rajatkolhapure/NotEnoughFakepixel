package org.ginafro.notenoughfakepixel.features.skyblock.overlays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class InvisibleButton extends GuiButton {
    public boolean ex;
    public InvisibleButton(int buttonId, int x, int y, int widthIn, int heightIn, boolean experimentation) {
        super(buttonId, x, y, widthIn, heightIn, "");
        ex = experimentation;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            Gui.drawRect(xPosition,yPosition,xPosition+width,yPosition+height,new Color(0,0,0,54).getRGB());
    }
    public void process(GuiContainer c){
        if(ex) {
            Minecraft.getMinecraft().playerController.windowClick(c.inventorySlots.windowId, 8 + id, 0, 1, Minecraft.getMinecraft().thePlayer);
        }else {
            Minecraft.getMinecraft().playerController.windowClick(c.inventorySlots.windowId, id, 0, 1, Minecraft.getMinecraft().thePlayer);
        }
    }

}
