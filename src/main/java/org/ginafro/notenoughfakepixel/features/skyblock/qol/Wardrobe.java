package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.utils.TablistParser;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Wardrobe {

    private final Set<Integer> activeKeySet = new HashSet<>();


    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        // Get the list of key binds
        List<Integer> keyBinds = Configuration.wardrobeKeyBind.getKeyBinds();

        // Check if all keys in the list are currently pressed
        boolean allKeysPressed = keyBinds.stream().allMatch(Keyboard::isKeyDown);

        // If all keys are pressed and they are not already active
        if (allKeysPressed && !activeKeySet.containsAll(keyBinds)) {
            // Execute the action
            player.sendChatMessage("/wardrobe");

            // Mark these keys as active
            activeKeySet.addAll(keyBinds);
        }

        // Clear keys from activeKeySet when released
        keyBinds.forEach(key -> {
            if (!Keyboard.isKeyDown(key)) {
                activeKeySet.remove(key);
            }
        });
    }

    @SubscribeEvent
    public void onKeyPressOnGui(GuiScreenEvent.KeyboardInputEvent event) {
        if(!(event.gui instanceof GuiChest)) return;
        GuiChest chest = (GuiChest) event.gui;
        Container container = chest.inventorySlots;

        if(!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (!title.startsWith("Wardrobe")) return;

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        // Get the list of key binds
        List<Integer> keyBinds = Configuration.wardrobeKeyBind.getKeyBinds();

        // Check if all keys in the list are currently pressed
        boolean allKeysPressed = keyBinds.stream().allMatch(Keyboard::isKeyDown);

        // If all keys are pressed and they are not already active
        if (allKeysPressed && !activeKeySet.containsAll(keyBinds)) {
            // Execute the action
            Minecraft.getMinecraft().thePlayer.closeScreen(); // Close the chest

            // Mark these keys as active
            activeKeySet.addAll(keyBinds);
        }

        // Clear keys from activeKeySet when released
        keyBinds.forEach(key -> {
            if (!Keyboard.isKeyDown(key)) {
                activeKeySet.remove(key);
            }
        });
    }
}
