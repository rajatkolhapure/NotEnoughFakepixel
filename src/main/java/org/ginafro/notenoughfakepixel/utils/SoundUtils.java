package org.ginafro.notenoughfakepixel.utils;
import net.minecraft.client.Minecraft;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SoundUtils {

    public static void playSound(int[] coords, String sound, float volume, float pitch) {
        playSound(coords, sound, volume, pitch, 1);
    }

    public static void playSound(int[] coords, String sound, float volume, float pitch, int times) {
        playSound(coords, sound, volume, pitch, 1, 500);
    }

    public static void playSound(int[] coords, String sound, float volume, float pitch, int times, int delay) {
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
}
