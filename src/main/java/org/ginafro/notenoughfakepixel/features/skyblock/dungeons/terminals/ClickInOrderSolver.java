package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.lwjgl.input.Mouse;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.DungeonManager;

import java.awt.*;
import java.util.ArrayList;

public class ClickInOrderSolver {

    public int round = 1;

    @SubscribeEvent
    public void onOpen(GuiOpenEvent e){
        if (!Configuration.dungeonsTerminalClickInOrderSolver) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (!ScoreboardUtils.currentLocation.isDungeon()) return;
        if(e.gui instanceof GuiChest){
            GuiChest chest = (GuiChest) e.gui;
            Container container = chest.inventorySlots;
            if(container instanceof ContainerChest){
                String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                if (!title.startsWith("Click in")) return;
                round = 1;
            }
        }
    }

    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!Configuration.dungeonsTerminalClickInOrderSolver) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (!ScoreboardUtils.currentLocation.isDungeon()) return;
        if(event.gui instanceof GuiChest) {
            GuiChest chest = (GuiChest) event.gui;
            Container container = chest.inventorySlots;
            if (container instanceof ContainerChest) {
                String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                if (!title.startsWith("Click in")) return;
                ContainerChest containerChest = (ContainerChest) container;
                for (int i = 1; i < 3; i++) {
                    for (int j = 1; j < 8; j++) {
                        Slot slot = containerChest.getSlot(i*9+j);
                        if (slot == null ||slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                        if (slot.getStack() == null || !(Block.getBlockFromItem(slot.getStack().getItem()) instanceof BlockStainedGlassPane)) continue;
                        // Is stained glass
                        //if (slot.getStack().getItemDamage() == 14 && Configuration.dungeonsTerminalHideIncorrect) {
                        //    slot.getStack().getItem().setDamage(slot.getStack(), 15);
                        //}
                        if (slot.getStack().stackSize == round) {
                            //slot.getStack().getItem().setDamage(slot.getStack(), 0);
                            RenderUtils.drawOnSlot(container.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, Configuration.dungeonsCorrectColor.getRGB());
                        } else if (slot.getStack().stackSize == round+1) {
                            //slot.getStack().getItem().setDamage(slot.getStack(), 0);
                            RenderUtils.drawOnSlot(container.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, new Color(Configuration.dungeonsAlternativeColor.getRed(), Configuration.dungeonsAlternativeColor.getGreen(), Configuration.dungeonsAlternativeColor.getBlue()).getRGB());
                        //} else if (slot.getStack().stackSize == round+2) {
                            //slot.getStack().getItem().setDamage(slot.getStack(), 0);
                            //RenderUtils.drawOnSlot(container.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, new Color(Configuration.dungeonsAlternativeColor.getRed(), Configuration.dungeonsAlternativeColor.getGreen(), Configuration.dungeonsAlternativeColor.getBlue(), 40).getRGB());
                        } else {
                            if (Configuration.dungeonsTerminalHideIncorrect) RenderUtils.drawOnSlot(container.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, new Color(Configuration.dungeonsAlternativeColor.getRed(), Configuration.dungeonsAlternativeColor.getGreen(), Configuration.dungeonsAlternativeColor.getBlue(), 40).getRGB());
                        }

                        // Set uncompleted non-highlighted slots to gray to hide
                        /*if (slot.getStack().getItemDamage() == 14 && Configuration.dungeonsTerminalHideIncorrect) {
                            slot.getStack().getItem().setDamage(slot.getStack(), 15);
                        }*/

                        // Set completed slots to gray to hide && round+1
                        if (slot.getStack().getItemDamage() == 5) {
                            if (Configuration.dungeonsTerminalHideIncorrect) slot.getStack().getItem().setDamage(slot.getStack(), 15);
                            round++;
                        }

                    }
                }
            }
        }
    }

    /*@SubscribeEvent
    public void onMouseClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!Configuration.dungeonsTerminalClickInOrderSolver) return;
        if (!DungeonManager.checkEssentialsF7()) return;

        if (!Mouse.getEventButtonState()) return;
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChest)) return; // Check if the current screen is a chest GUI
        GuiChest guiChest = (GuiChest) Minecraft.getMinecraft().currentScreen;
        Container container = guiChest.inventorySlots;
        if (!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (!title.startsWith("Click in")) return;
        Slot slot = guiChest.getSlotUnderMouse();
        if (slot == null || slot.getStack() == null) return;
        if (slot.getStack().stackSize == round) {
            //round++;
            if (Configuration.dungeonsTerminalHideIncorrect && slot.getStack().getItemDamage() == 0) {
                slot.getStack().getItem().setDamage(slot.getStack(), 15);
            }
            slot.getStack().getItem().setDamage(slot.getStack(), 5);
        }
    }*/

}
