package org.ginafro.notenoughfakepixel.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.ginafro.notenoughfakepixel.gui.impl.ConfigurationGUI;

public class TextButton extends Button {

    public int color;
    public TextButton(int buttonId, int x, int y, int w, int h, String buttonText, int colorCode) {
        super(buttonId, x, y, w, h, buttonText);
        color = colorCode;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if(this.enabled && this.visible){
            GlStateManager.scale(1f,1f,1f);
            GlStateManager.color(1f,1f,1f,1f);
            Minecraft.getMinecraft().fontRendererObj.drawString(displayString,xPosition,yPosition,color);
        }
    }

    @Override
    public void execute() {
        ConfigurationGUI.openCategory = this.displayString;
    }
}
