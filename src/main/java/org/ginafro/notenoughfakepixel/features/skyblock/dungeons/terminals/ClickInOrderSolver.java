package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals;

import cc.polyfrost.oneconfig.config.core.OneColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Gamemode;

public class ClickInOrderSolver {

    public int stackCount = 1;

    @SubscribeEvent
    public void onOpen(GuiScreenEvent.BackgroundDrawnEvent e){
        if(!Configuration.sb && !Configuration.clickInOrder && ScoreboardUtils.currentGamemode != Gamemode.SKYBLOCK) return;
        if(e.gui instanceof GuiChest){
            GuiChest chest = (GuiChest) e.gui;
            Container container = chest.inventorySlots;
            if(container instanceof ContainerChest){
                ContainerChest containerChest = (ContainerChest) container;
                for(Slot slot : containerChest.inventorySlots){
                    ItemStack item = slot.getStack();
                    if(item != null ){
                        if(Block.getBlockFromItem(item.getItem()) instanceof BlockStainedGlassPane){
                            if(item.getMetadata() == 15){
                                continue;
                            }if(item.getMetadata() == 14) {
                                if (item.stackSize == stackCount) {
                                    OneColor color = Configuration.terminalColor;
                                    color.setAlpha(102);
                                    RenderUtils.drawOnSlot(chest.inventorySlots.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, color.getRGB());
                                    stackCount++;
                                    if(stackCount > 15){
                                        stackCount = 1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
