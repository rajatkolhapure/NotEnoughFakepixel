package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.lwjgl.input.Mouse;

import java.util.Arrays;
import java.util.List;

public class MiddleClickEvent {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static List<String> chestNames = Arrays.asList(
            "Experimentation Table",
            "Chronomatron",
            "Ultrasequencer",
            "Superpairs",

            "What starts with '",
            "Correct all the panes!",
            "Complete the maze!",
            "Click in order!",
            "Select all the "
        );

    @SubscribeEvent
    public void onMouseClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!Configuration.middleClickChests) return;
        if (Mouse.getEventButton() != 0 || !Mouse.getEventButtonState()) return;

        // Check if the current screen is a chest GUI
        if (!(mc.currentScreen instanceof GuiChest)) return;

        GuiChest chestGui = (GuiChest) mc.currentScreen;
        ContainerChest container = (ContainerChest) chestGui.inventorySlots;

        // Get the chest's name
        String currentChestName = container.getLowerChestInventory().getDisplayName().getUnformattedText();

        // Replace with your specific chest name condition
        for (String chestName : chestNames){
            if (currentChestName.startsWith(chestName)) {
                // Check if the left mouse button is clicked

                // Cancel the normal click event
                event.setCanceled(true);

                // Simulate a middle-click
                int slot = chestGui.getSlotUnderMouse() != null ? chestGui.getSlotUnderMouse().slotNumber : -1;

                if (slot >= 0) {
                    // Perform the middle-click action
                    mc.playerController.windowClick(
                            container.windowId,  // The window ID of the chest
                            slot,               // Slot clicked
                            2,                  // Middle-click (button 2)
                            3,                  // Click type (3 is PICKUP_ALL for middle-click)
                            mc.thePlayer        // Player entity
                    );
                }

            }
        }
    }
}
