package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.variables.MobDisplayTypes;

import java.awt.*;

public class StarredMobDisplay {

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        if (!Configuration.starredMobs) return;
        if (Minecraft.getMinecraft().thePlayer == null) return;
        if (Minecraft.getMinecraft().theWorld == null) return;

        WorldClient world = Minecraft.getMinecraft().theWorld;

        world.loadedEntityList.forEach(entity -> {
            if (entity == null) return;
            if (entity.getName() == null) return;
            if (!(entity instanceof EntityArmorStand)) return;
            if (entity.getName().startsWith("§6✮")){
                Color color = new Color(
                        Configuration.starredBoxColor.getRed(),
                        Configuration.starredBoxColor.getGreen(),
                        Configuration.starredBoxColor.getBlue(),
                        Configuration.starredBoxColor.getAlpha()
                );

                RenderUtils.renderEntityHitbox(
                        entity,
                        event.partialTicks,
                        color,
                        MobDisplayTypes.NONE
                );
            }
        });
    }

}
