package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.variables.MobDisplayTypes;

import java.awt.*;

public class StarredMobDisplay {

    MobDisplayTypes mobDisplayType = MobDisplayTypes.NONE;

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        if (!Configuration.dungeonsStarredMobs) return;
        if (Minecraft.getMinecraft().thePlayer == null) return;
        if (Minecraft.getMinecraft().theWorld == null) return;

        WorldClient world = Minecraft.getMinecraft().theWorld;

        world.loadedEntityList.forEach(entity -> {
            if (entity == null) return;
            if (entity.getName() == null) return;
            if (!(entity instanceof EntityArmorStand)) return;
            if (!entity.getName().contains("✮")) return;
            Color color = new Color(
                    Configuration.dungeonsStarredBoxColor.getRed(),
                    Configuration.dungeonsStarredBoxColor.getGreen(),
                    Configuration.dungeonsStarredBoxColor.getBlue(),
                    Configuration.dungeonsStarredBoxColor.getAlpha()
            );

            if (entity.getName().contains("Stormy")) {
                color = new Color(
                        Configuration.dungeonsStormyColor.getRed(),
                        Configuration.dungeonsStormyColor.getGreen(),
                        Configuration.dungeonsStormyColor.getBlue(),
                        Configuration.dungeonsStormyColor.getAlpha()
                );
            } else if (entity.getName().contains("Withermancer")) {
                color = new Color(
                        Configuration.dungeonsWithermancerColor.getRed(),
                        Configuration.dungeonsWithermancerColor.getGreen(),
                        Configuration.dungeonsWithermancerColor.getBlue(),
                        Configuration.dungeonsWithermancerColor.getAlpha()
                );
                mobDisplayType = MobDisplayTypes.WITHERMANCER;
            } else if (entity.getName().contains("Zombie Commander")) {
                color = new Color(
                        Configuration.dungeonsZombieCommanderColor.getRed(),
                        Configuration.dungeonsZombieCommanderColor.getGreen(),
                        Configuration.dungeonsZombieCommanderColor.getBlue(),
                        Configuration.dungeonsZombieCommanderColor.getAlpha()
                );
            } else if (entity.getName().contains("Skeleton Master")) {
                color = new Color(
                        Configuration.dungeonsSkeletonMasterColor.getRed(),
                        Configuration.dungeonsSkeletonMasterColor.getGreen(),
                        Configuration.dungeonsSkeletonMasterColor.getBlue(),
                        Configuration.dungeonsSkeletonMasterColor.getAlpha()
                );
            }


            if (Configuration.dungeonsStarredMobsEsp) GlStateManager.disableDepth();
            RenderUtils.renderEntityHitbox(
                    entity,
                    event.partialTicks,
                    color,
                    mobDisplayType
            );
            if (Configuration.dungeonsStarredMobsEsp) GlStateManager.enableDepth();
            mobDisplayType = MobDisplayTypes.NONE;
        });
    }

}
