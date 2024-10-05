package org.ginafro.notenoughfakepixel.features.skyblock.overlays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;

import java.io.IOException;

public class StorageOverlay extends GuiScreen {
    int unlockedEnders = 0;
    int unlockedBags = 0;
    public GuiContainer gc;
    public GuiScreen gui;
    public SwitchButton switchButton;
    public static boolean echest = true;

    public StorageOverlay(GuiScreen g){
    gc = (GuiContainer) g;
    gui = g;
    }


    @Override
    public void initGui() {
        GlStateManager.scale(1.0f,1.0f,1.0f);
        this.buttonList.add(switchButton = new SwitchButton(312, this.width / 2 - 100 , 10 ));
        for(int i =9; i <17; i++){
            Container c = gc.inventorySlots;
            if(c.getSlot(i).getStack().getDisplayName().contains("Ender")){
                unlockedEnders++;
            }
        }
        for(int i = 27; i < 45; i++){
            Container c = gc.inventorySlots;
            if(!c.getSlot(i).getStack().getDisplayName().contains("Empty")){
                unlockedBags++;
            }
        }
        GlStateManager.scale(1.0f,1.0f,1.0f);
        if(echest){
            int yIndex = 0;
            int xIndex = 0;
        for(int i = 0; i < unlockedEnders; i++){
            if(xIndex == 3){
                yIndex++;
                xIndex = 0;
            }
            int xPos = (gui.width / 2 - 275) + (186 * xIndex);
            int yPos = 35 + (100 * yIndex);
            this.buttonList.add(new InvisibleButton(i-1 , xPos,yPos,176,95,true));
            xIndex++;
            }
        }else {
            int yIndex = 0;
            int xIndex = 0;
            for(int i = 0; i < unlockedBags; i++){
                if(xIndex == 5){
                    yIndex++;
                    xIndex = 0;
                }
                int xPos = (gui.width / 2 - 280) + (120 * xIndex);
                int yPos = 35 + (57 * yIndex);
                this.buttonList.add(new InvisibleButton(27 + i , xPos,yPos,116, 52,false));
                xIndex++;
            }
        }
    }


    @Override
    public void updateScreen() {
        this.buttonList.clear();
        GlStateManager.scale(1.0f,1.0f,1.0f);
        this.buttonList.add(switchButton = new SwitchButton(312, this.width / 2 - 100 , 10 ));
        if(echest){
            int yIndex = 0;
            int xIndex = 0;
            for(int i = 0; i < unlockedEnders; i++){
                if(xIndex == 3){
                    yIndex++;
                    xIndex = 0;
                }
                int xPos = (gui.width / 2 - 275) + (186 * xIndex);
                int yPos = 35 + (100 * yIndex);
                this.buttonList.add(new InvisibleButton(i+1 , xPos,yPos,176,95,true));
                xIndex++;
            }
        }else {
            int yIndex = 0;
            int xIndex = 0;
            for(int i = 0; i < unlockedBags; i++){
                if(xIndex == 5){
                    yIndex++;
                    xIndex = 0;
                }
                int xPos = (gui.width / 2 - 280) + (120 * xIndex);
                int yPos = 35 + (57 * yIndex);
                this.buttonList.add(new InvisibleButton(27 + i , xPos,yPos,116, 52,false));
                xIndex++;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.scale(1.0f,1.0f,1.0f);
        if(echest) {
            int yIndex = 0;
            int xIndex = 0;
            for (int i = 0; i < unlockedEnders; i++) {
                if (xIndex == 3) {
                    yIndex++;
                    xIndex = 0;
                }
                ResourceLocation r = new ResourceLocation("notenoughfakepixel:skyblock/overlay_echest.png");
                Minecraft.getMinecraft().getTextureManager().bindTexture(r);
                int xPos = (gui.width / 2 - 275) + (186 * xIndex);
                int yPos = 35 + (100 * yIndex);
                GuiScreen.drawScaledCustomSizeModalRect(xPos, yPos, 0f, 0f, 176, 95, 176, 95, 176, 95);
                int a = i + 1;
                this.drawString(fontRendererObj , "Ender Chest " + a    ,xPos + 88 , yPos + fontRendererObj.FONT_HEIGHT / 2 , -1);
                xIndex++;
            }
        }else {
            int yIndex = 0;
            int xIndex = 0;
            for(int i = 0; i < unlockedBags; i++){
                if(xIndex == 5){
                    yIndex++;
                    xIndex = 0;
                }
                ResourceLocation r = new ResourceLocation("notenoughfakepixel:skyblock/overlay_bag.png");
                Minecraft.getMinecraft().getTextureManager().bindTexture(r);
                int xPos = (gui.width / 2 - 280) + (120 * xIndex);
                int yPos = 35 + (57 * yIndex);
                GuiScreen.drawScaledCustomSizeModalRect(xPos, yPos, 0f, 0f, 116, 52, 116, 52, 116, 52);
                int a = i + 1;
                this.drawString(fontRendererObj , "Backpack " + a,xPos + 10 , yPos + fontRendererObj.FONT_HEIGHT / 2 , -1);
                xIndex++;
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        for(GuiButton b : buttonList){
            if(b.id == button.id){
                if(b instanceof InvisibleButton){
                    InvisibleButton ib = (InvisibleButton) b;
                    System.out.println("ID: " + b.id);
                    ib.process(gc);
                }
                else if(b instanceof SwitchButton){
                    SwitchButton sb = (SwitchButton) b;
                    sb.Switch();
                }
            }
        }
    }

    public static class StorageEvent {

    @SubscribeEvent
    public void onOpen(GuiScreenEvent.BackgroundDrawnEvent e) {
        if (e.gui instanceof GuiContainer) {
            GuiContainer gc = (GuiContainer) e.gui;
            if(gc instanceof GuiInventory) return;
            if (gc.inventorySlots.getSlot(4).getStack() != null) {
                if (gc.inventorySlots.getSlot(4).getStack().getDisplayName().contains("Ender")) {
                    if (Configuration.storageOverlay) {
                        Minecraft.getMinecraft().displayGuiScreen(new StorageOverlay(e.gui));
                    }
                }
            }
            }
        }
    }
}
