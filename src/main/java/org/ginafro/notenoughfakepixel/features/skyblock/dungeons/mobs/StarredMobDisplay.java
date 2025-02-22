package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.mobs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.MobDisplayTypes;

import java.awt.*;
import java.util.regex.Pattern;

public class StarredMobDisplay {

    MobDisplayTypes mobDisplayType = MobDisplayTypes.NONE;

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        if (!Configuration.dungeonsStarredMobs) return;
        if (Minecraft.getMinecraft().thePlayer == null) return;
        if (Minecraft.getMinecraft().theWorld == null) return;

        WorldClient world = Minecraft.getMinecraft().theWorld;

        world.loadedEntityList.forEach(entity -> {
            if (entity == null || entity.getName() == null || !(entity instanceof EntityArmorStand)) return;
            if (!entity.getName().contains("✮")) return;

            // Ensure entity is not dying before rendering
            if (entity instanceof EntityLivingBase && isDying((EntityLivingBase) entity)) return;

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
            } else if (entity.getName().contains("Fels")) {
                mobDisplayType = MobDisplayTypes.FELALIVE;
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

    private static final Pattern PATTERN1 = Pattern.compile("^§.\\[§.Lv\\d+§.\\] §.+ (?:§.)+0§f/.+§c❤$");
    private static final Pattern PATTERN2 = Pattern.compile("^.+ (?:§.)+0§c❤$");
    private static final Pattern PATTERN_RUNIC = Pattern.compile("^§.\\[§.Runic§.\\] §.+ (?:§.)+0§f/.+§c❤$");

    private boolean isDying(EntityLivingBase entity) {
        if (entity == null) return false;
        if (entity.getHealth() <= 0 || entity.isDead) return true;
        IChatComponent displayName = entity.getDisplayName();
        if (displayName == null) return false;
        String name = displayName.getUnformattedText();
        return PATTERN1.matcher(name).matches() ||
                PATTERN2.matcher(name).matches() ||
                PATTERN_RUNIC.matcher(name).matches();
    }
}
