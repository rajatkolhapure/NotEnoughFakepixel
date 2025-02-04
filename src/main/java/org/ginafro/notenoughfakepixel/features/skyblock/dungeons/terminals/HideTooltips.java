package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.lwjgl.input.Mouse;

public class HideTooltips {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onTooltipLow(ItemTooltipEvent event) {
        if (!Configuration.dungeonsHideTooltips) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (!ScoreboardUtils.currentLocation.isDungeon()) return;
        if (event.toolTip == null) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;

        if (mc.currentScreen instanceof GuiChest) {
            ContainerChest chest = (ContainerChest) player.openContainer;
            IInventory inv = chest.getLowerChestInventory();
            String chestName = inv.getDisplayName().getUnformattedText();

            if (chestName.equals("Click in order!") ||
                    chestName.startsWith("Select all the") ||
                    chestName.startsWith("What starts with")) {
                event.toolTip.clear();
            }
        }
    }
}
