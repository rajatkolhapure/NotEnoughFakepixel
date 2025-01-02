package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.utils.TablistParser;

public class AutoReadyDungeon {

    private static boolean clicked = false;

    @SubscribeEvent()
    public void onGuiOpen(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (clicked) return;
        if (!Configuration.autoReadyDungeon) return;
        if (!ScoreboardUtils.currentLocation.isDungeon()) return;
        if (event.gui == null) return;
        if (!(event.gui instanceof GuiChest)) return;

        GuiChest chest = (GuiChest) event.gui;
        Container container = chest.inventorySlots;

        String chestName = TablistParser.currentOpenChestName;
        if (chestName == null || chestName.isEmpty()) return;

        if (chestName.startsWith("Catacombs -")) {
            ContainerChest containerChest = (ContainerChest) container;

            for(Slot slot : containerChest.inventorySlots) {

                if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                ItemStack item = slot.getStack();
                if (item == null) continue;
                if (item.getItem() instanceof ItemSkull) {

                    Minecraft.getMinecraft().playerController.windowClick(chest.inventorySlots.windowId, slot.getSlotIndex() + 9, 0, 0, Minecraft.getMinecraft().thePlayer);
                    clicked = true;
                }

            }
        }
    }

    @SubscribeEvent()
    public void onWorldUnload(WorldEvent.Unload event) {
        clicked = false;
    }

}
