package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals;


import cc.polyfrost.oneconfig.config.core.OneColor;
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
import org.ginafro.notenoughfakepixel.variables.Gamemode;


public class ClickOnColorsSolver {

    @SubscribeEvent
    public void onOpen(GuiScreenEvent.BackgroundDrawnEvent e){
        if(!ScoreboardUtils.currentLocation.isDungeon()) return;

        if(!Configuration.selectColors && ScoreboardUtils.currentGamemode != Gamemode.SKYBLOCK) return;
        if(!(e.gui instanceof GuiChest)) return;

        GuiChest chest = (GuiChest) e.gui;
        Container container = chest.inventorySlots;

        if(!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (!title.startsWith("Select all the ")) return;
        String color = title.split("the ")[1].split(" items")[0].toLowerCase();

        ContainerChest containerChest = (ContainerChest) container;
        for(Slot slot : containerChest.inventorySlots) {
            // select only the items in the chest
            if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
            ItemStack item = slot.getStack();
            if (item == null) continue;
            if (item.isItemEnchanted()) continue;

            if (item.getItem() == Items.dye) {
                System.out.println(F7ColorsDict.getColorFromDye(item.getMetadata()).toString() + " " + color);
                if (color.equals(F7ColorsDict.getColorFromDye(item.getMetadata()).toString())) {
                    highlightSlot(slot, chest);
                }
            } else if (Block.getBlockFromItem(item.getItem()) instanceof BlockStainedGlassPane ||
                    Block.getBlockFromItem(item.getItem()) instanceof BlockStainedGlass ||
                    Block.getBlockFromItem(item.getItem()) instanceof BlockColored ||
                    Block.getBlockFromItem(item.getItem()) instanceof BlockCarpet) {
                if (color.equals(F7ColorsDict.getColorFromMain(item.getMetadata()).toString())) {
                    highlightSlot(slot, chest);
                }
            }
        }
    }

    public static void highlightSlot(Slot slot, GuiChest chest){
        OneColor color1 = Configuration.terminalColor;
        RenderUtils.drawOnSlot(chest.inventorySlots.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, color1.getRGB());
    }

}
