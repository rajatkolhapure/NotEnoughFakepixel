package org.ginafro.notenoughfakepixel.gui.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.gui.ModGUI;

public class ModMenu extends ModGUI {

    private int index = 0;
    @Override
    public void initGui() {
        super.initGui();
    }

    public static void openGUI(){
        NotEnoughFakepixel.openGui = new ModMenu();
    }

    public void addCategory(String categoryName ){
        index++;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0F,1.0F,1.0F);
        GlStateManager.color(1.0F,1.0F,1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("notenoughfakepixel:backgrounds/default/background.png"));
        Gui.drawScaledCustomSizeModalRect(this.width / 2 - 256 , this.height / 2 - 160, 0.0F,0.0F,512,300,512,300,512,300);
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("notenoughfakepixel:backgrounds/default/sidebar.png"));
        Gui.drawScaledCustomSizeModalRect(this.width / 2 - 250 , this.height / 2 - 160, 0.0F,0.0F,64,300,64,300,64,300);
        GlStateManager.popMatrix();
    }
}
