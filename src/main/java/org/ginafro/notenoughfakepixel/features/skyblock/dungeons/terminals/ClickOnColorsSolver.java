package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals;


import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.F7ColorsDict;
import org.lwjgl.input.Mouse;

public class ClickOnColorsSolver {

    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent e){
        if(!Configuration.dungeonsTerminalSelectColorsSolver) return;
        if(!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if(!ScoreboardUtils.currentLocation.isDungeon()) return;
        if(!(e.gui instanceof GuiChest)) return;

        GuiChest chest = (GuiChest) e.gui;
        Container container = chest.inventorySlots;

        if(!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (!title.startsWith("Select all the")) return;
        String color = title.split("the ")[1].split(" items")[0].toLowerCase();

        ContainerChest containerChest = (ContainerChest) container;
        for(Slot slot : containerChest.inventorySlots) {
            // Select only the items in the chest
            if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;

            ItemStack itemStack = slot.getStack();
            if (itemStack == null) continue;

            if (containerChest.inventorySlots.indexOf(slot) == 49) {
                if (!Configuration.dungeonsTerminalHideIncorrect) continue;
                itemStack.setItem(containerChest.inventorySlots.get(0).getStack().getItem());
                itemStack.getItem().setDamage(itemStack, 15);
                continue;
            }

            // Hide already clicked
            if (itemStack.isItemEnchanted()) {
                if (!Configuration.dungeonsTerminalHideIncorrect) continue;
                itemStack.setItem(containerChest.inventorySlots.get(0).getStack().getItem());
                itemStack.getItem().setDamage(itemStack, 15);
                continue;
            }

            // If its tint
            if (itemStack.getItem() == Items.dye) {
                //System.out.println(F7ColorsDict.getColorFromDye(itemStack.getMetadata()).toString() + " " + color);
                if (color.equals(F7ColorsDict.getColorFromDye(itemStack.getMetadata()).toString())) {
                    RenderUtils.drawOnSlot(chest.inventorySlots.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, Configuration.dungeonsCorrectColor.getRGB());
                } else {
                    // HIDE OTHER SLOTS
                    if (!Configuration.dungeonsTerminalHideIncorrect) continue;
                    // Hide unwanted slots
                    itemStack.setItem(containerChest.inventorySlots.get(0).getStack().getItem());
                    itemStack.getItem().setDamage(itemStack, 15);
                }
            // If its glass type
            } else if (Block.getBlockFromItem(itemStack.getItem()) instanceof BlockStainedGlassPane ||
                    Block.getBlockFromItem(itemStack.getItem()) instanceof BlockStainedGlass ||
                    Block.getBlockFromItem(itemStack.getItem()) instanceof BlockColored ||
                    Block.getBlockFromItem(itemStack.getItem()) instanceof BlockCarpet) {
                if (color.equals(F7ColorsDict.getColorFromMain(itemStack.getMetadata()).toString())) {
                    //itemStack.getItem().setDamage(itemStack, 0);
                    RenderUtils.drawOnSlot(chest.inventorySlots.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, Configuration.dungeonsCorrectColor.getRGB());
                } else {
                    // HIDE OTHER SLOTS
                    if (!Configuration.dungeonsTerminalHideIncorrect) continue;
                    if (itemStack.getMetadata() != 15) {
                        itemStack.setItem(containerChest.inventorySlots.get(0).getStack().getItem());
                        itemStack.getItem().setDamage(itemStack, 15);
                    }
                }
            } else {
                // HIDE OTHER SLOTS
                if (!Configuration.dungeonsTerminalHideIncorrect) continue;
                itemStack.setItem(containerChest.inventorySlots.get(0).getStack().getItem());
                itemStack.getItem().setDamage(itemStack, 15);
            }
        }
    }

    @SubscribeEvent
    public void onMouseClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!Configuration.dungeonsPreventMissclicks) return;
        if (!Configuration.dungeonsTerminalClickInOrderSolver) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (!ScoreboardUtils.currentLocation.isDungeon()) return;
        if (!Mouse.getEventButtonState()) return;
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChest)) return; // Check if the current screen is a chest GUI
        GuiChest guiChest = (GuiChest) Minecraft.getMinecraft().currentScreen;
        Container container = guiChest.inventorySlots;
        if (!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (!title.startsWith("Select all the")) return;
        if (guiChest.getSlotUnderMouse() == null || guiChest.getSlotUnderMouse().getStack() == null) return;
        if (Block.getBlockFromItem(guiChest.getSlotUnderMouse().getStack().getItem()) instanceof BlockStainedGlassPane && guiChest.getSlotUnderMouse().getStack().getMetadata() == 15) {
            event.setCanceled(true);
        }
    }

}
