package org.ginafro.notenoughfakepixel.features.skyblock.diana;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.sound.SoundEvent;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SoundManager {

    String waypointSound = "random.pop";
    String waypointTreasureSound = "random.pop";
    float volumeWaypointSound = 3.0f;
    float volumeWaypointTreasureSound = 4.0f;

    public void playSound(int[] coords, String sound, float volume, float pitch) {
        playSound(coords, sound, volume, pitch, 1);
    }

    public void playSound(int[] coords, String sound, float volume, float pitch, int times) {
        playSound(coords, sound, volume, pitch, 1, 500);
    }

    public void playSound(int[] coords, String sound, float volume, float pitch, int times, int delay) {
        Minecraft.getMinecraft().theWorld.playSound(coords[0], coords[1], coords[2], sound, volume, pitch, false);
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        for (int i = 1; i < times; i++) {
            exec.schedule(new Runnable() {
                public void run() {
                    Minecraft.getMinecraft().theWorld.playSound(coords[0], coords[1], coords[2], sound, volume, pitch, false);
                }
            }, delay, TimeUnit.MILLISECONDS);
        }
    }

    public void playWaypointSound(int[] coords) {
        playSound(coords, waypointSound, volumeWaypointSound, 0.5f);
    }

    public void playTreasureSound(int[] coords) {
        playSound(coords, waypointTreasureSound, volumeWaypointTreasureSound, 2.0f);
    }
}
