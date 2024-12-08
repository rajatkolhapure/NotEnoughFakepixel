package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;

public class Fullbright {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static final float BRIGHTEST = 1.0f;
    private static final float FULLBRIGHT = 10000.0f;

    @SubscribeEvent
    public void onRender(RenderHandEvent event) {
        if(Configuration.fullbright) changeBrightness(FULLBRIGHT);
        else changeBrightness(BRIGHTEST);
    }

    private void changeBrightness(float toLevel) {
        float moveBy = toLevel - mc.gameSettings.gammaSetting;
        if (moveBy == 0) return;
        System.out.println("changing light level");
        mc.gameSettings.gammaSetting += moveBy;
        mc.gameSettings.saveOptions();
    }
}
