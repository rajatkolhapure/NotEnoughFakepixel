package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals;


import cc.polyfrost.oneconfig.config.core.OneColor;
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
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Gamemode;

public class StartingWithSolver {

    @SubscribeEvent
    public void onOpen(GuiScreenEvent.BackgroundDrawnEvent e){
        if(ScoreboardUtils.currentGamemode != Gamemode.SKYBLOCK) return;
        if(!ScoreboardUtils.currentLocation.isDungeon()) return;
        if(!Configuration.dungeonsStartsWith) return;
        if(!(e.gui instanceof GuiChest)) return;

        GuiChest chest = (GuiChest) e.gui;
        Container container = chest.inventorySlots;

        if(!(container instanceof ContainerChest)) return;

        ContainerChest containerChest = (ContainerChest) container;
        String name = containerChest.getLowerChestInventory().getDisplayName().getUnformattedText();
        if(name.toLowerCase().contains("what starts with")){
            char letter = name.charAt(name.length() - 2);

            for(Slot slot : containerChest.inventorySlots){
                if(slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;

                ItemStack item = slot.getStack();
                if(item == null) continue;
                if(item.isItemEnchanted()) continue;

                if(StringUtils.stripControlCodes(item.getDisplayName()).charAt(0) == letter) {
                    OneColor color = Configuration.dungeonsTerminalColor;
                    color.setAlpha(102);
                    RenderUtils.drawOnSlot(chest.inventorySlots.inventorySlots.size(), slot.xDisplayPosition , slot.yDisplayPosition , color.getRGB());
                }

            }
        }

    }
}
