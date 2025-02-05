package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.DungeonManager;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;

public class CorrectPanesSolver {
    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent e){
        if(!Configuration.dungeonsTerminalCorrectPanesSolver) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        if(!(e.gui instanceof GuiChest)) return;

        GuiChest chest = (GuiChest) e.gui;
        Container container = chest.inventorySlots;

        if(!(container instanceof ContainerChest)) return;

        ContainerChest containerChest = (ContainerChest) container;
        String name = containerChest.getLowerChestInventory().getDisplayName().getUnformattedText();
        if(name.equals("Correct all the panes!")){
            for(Slot slot : containerChest.inventorySlots) {
                // select only the items in the chest
                if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                ItemStack item = slot.getStack();
                if (item == null) continue;
                if (Block.getBlockFromItem(item.getItem()) instanceof BlockStainedGlassPane) {
                    if (item.getMetadata() == 5) {
                        item.getItem().setDamage(item, 15);
                    } else if (item.getMetadata() == 14) {
                        item.getItem().setDamage(item, 0);
                        RenderUtils.drawOnSlot(chest.inventorySlots.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, Configuration.dungeonsCorrectColor.getRGB());
                    } else if (item.getMetadata() == 0) {
                        RenderUtils.drawOnSlot(chest.inventorySlots.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, Configuration.dungeonsCorrectColor.getRGB());
                    }
                }

            }
        }
    }
}
