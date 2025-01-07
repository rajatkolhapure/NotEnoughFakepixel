package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ItemUtils;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.StringUtils;
import org.ginafro.notenoughfakepixel.utils.TablistParser;

import java.awt.*;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class ChocolateFactory {

    private Pattern upgradeCostPattern = Pattern.compile("(?<cost>[0-9,]+) Chocolate");

    @SubscribeEvent()
    public void onGuiOpen(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!Configuration.showBestUpgrade || event.gui == null || !(event.gui instanceof GuiChest)) return;

        TreeMap<Float, Slot> upgradeCosts = new TreeMap<>();
        GuiChest chest = (GuiChest) event.gui;
        Container container = chest.inventorySlots;

        String chestName = TablistParser.currentOpenChestName;
        if (chestName == null || !chestName.startsWith("Chocolate Factory")) return;

        int index = 0;
        ContainerChest containerChest = (ContainerChest) container;
        for (Slot slot : containerChest.inventorySlots) {
            if (slot.getSlotIndex() < 28 || slot.getSlotIndex() > 34) continue;
            index++;
            ItemStack item = slot.getStack();
            if (item != null && item.getItem() instanceof ItemSkull) {
                String upgradeCost = ItemUtils.getLoreLine(item, upgradeCostPattern);
                if (upgradeCost == null) continue;
                upgradeCost = StringUtils.cleanColor(upgradeCost).replaceAll(",", "").replaceAll(" Chocolate", "");

                float costRatio = Float.parseFloat(upgradeCost) / index;
                upgradeCosts.put(costRatio, slot);
            }
        }
        if (upgradeCosts.isEmpty()) return;
        float lowestValue = upgradeCosts.firstKey();
        Slot associatedSlot = upgradeCosts.get(lowestValue);

        RenderUtils.drawOnSlot(containerChest.inventorySlots.size(), associatedSlot.xDisplayPosition, associatedSlot.yDisplayPosition, new Color(0, 255, 0, 100).getRGB());
    }
}
