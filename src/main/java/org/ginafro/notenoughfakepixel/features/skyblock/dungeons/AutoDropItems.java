package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AutoDropItems {

    private static final String TARGET_ITEM_NAME1 = "Training Weights";
    private static final String TARGET_ITEM_NAME2 = "Defuse Kit";

    @SubscribeEvent
    public void onPickupSound(PlaySoundAtEntityEvent event) {
        if (!Configuration.autoDropItems) return; // Check if the feature is enabled
        if (!ScoreboardUtils.currentLocation.isDungeon()) return; // Check if the player is in a dungeon
        if (!event.name.equals("random.pop")) return;
        int delayMs = 500;
        // Drop items at delayMs milliseconds after pickup sound
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.schedule(new Runnable() {
            public void run() {
                checkInventoryAndDrop();
            }
        }, delayMs, TimeUnit.MILLISECONDS);
        checkInventoryAndDrop();
    }

    private void checkInventoryAndDrop() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP p = mc.thePlayer;
        for (int i = 0; i < p.inventory.mainInventory.length; i++) {
            // If we find a non-empty slot that isn't slot 8.
            if (p.inventory.mainInventory[i] != null && i != 8) {
                //System.out.println(i+" ITERATING INVENTORY "+p.inventory.mainInventory[i].getDisplayName());
                if (p.inventory.mainInventory[i].getDisplayName().contains(TARGET_ITEM_NAME1) || p.inventory.mainInventory[i].getDisplayName().contains(TARGET_ITEM_NAME2)) {
                    //System.out.println("TARGET FOUND "+p.inventory.mainInventory[i].getDisplayName());
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 0, 4, mc.thePlayer);
                }
            }
        }
    }

}
