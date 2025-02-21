package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WarpsShortcut {

    private final Set<Integer> activeKeySet = new HashSet<>();

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (!Configuration.qolShortcutWarps) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        checkWarpIs(player);
        checkWarpHub(player);
        checkWarpDh(player);
    }

    private void checkWarpIs(EntityPlayerSP player) {
        // Get the list of key binds
        List<Integer> keyBindsWarpIs = Configuration.qolShortcutWarpIs.getKeyBinds();
        // Check if all keys in the list are currently pressed
        boolean allKeysPressedWarpIs = keyBindsWarpIs.stream()
                .allMatch(key -> key >= 0 && key < Keyboard.KEYBOARD_SIZE && Keyboard.isKeyDown(key));
        // If all keys are pressed and they are not already active
        if (allKeysPressedWarpIs && !activeKeySet.containsAll(keyBindsWarpIs)) {
            // Execute the action
            player.sendChatMessage("/warp is");

            // Mark these keys as active
            activeKeySet.addAll(keyBindsWarpIs);
        }
        // Clear keys from activeKeySet when released
        keyBindsWarpIs.forEach(key -> {
            if (!Keyboard.isKeyDown(key)) {
                activeKeySet.remove(key);
            }
        });
    }

    private void checkWarpHub(EntityPlayerSP player) {
        // Get the list of key binds
        List<Integer> keyBindsWarpHub = Configuration.qolShortcutWarpHub.getKeyBinds();
        // Check if all keys in the list are currently pressed
        boolean allKeysPressedWarpHub = keyBindsWarpHub.stream()
                .allMatch(key -> key >= 0 && key < Keyboard.KEYBOARD_SIZE && Keyboard.isKeyDown(key));
        // If all keys are pressed and they are not already active
        if (allKeysPressedWarpHub && !activeKeySet.containsAll(keyBindsWarpHub)) {
            // Execute the action
            player.sendChatMessage("/warp hub");

            // Mark these keys as active
            activeKeySet.addAll(keyBindsWarpHub);
        }
        // Clear keys from activeKeySet when released
        keyBindsWarpHub.forEach(key -> {
            if (!Keyboard.isKeyDown(key)) {
                activeKeySet.remove(key);
            }
        });
    }

    private void checkWarpDh(EntityPlayerSP player) {
        // Get the list of key binds
        List<Integer> keyBindsWarpDungeonHub = Configuration.qolShortcutWarpDh.getKeyBinds();
        // Check if all keys in the list are currently pressed
        boolean allKeysPressedWarpDungeonHub = keyBindsWarpDungeonHub.stream()
                .allMatch(key -> key >= 0 && key < Keyboard.KEYBOARD_SIZE && Keyboard.isKeyDown(key));
        // If all keys are pressed and they are not already active
        if (allKeysPressedWarpDungeonHub && !activeKeySet.containsAll(keyBindsWarpDungeonHub)) {
            // Execute the action
            player.sendChatMessage("/warp dh");

            // Mark these keys as active
            activeKeySet.addAll(keyBindsWarpDungeonHub);
        }
        // Clear keys from activeKeySet when released
        keyBindsWarpDungeonHub.forEach(key -> {
            if (!Keyboard.isKeyDown(key)) {
                activeKeySet.remove(key);
            }
        });
    }
}
