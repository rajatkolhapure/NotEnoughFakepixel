package org.ginafro.notenoughfakepixel.features.skyblock.slayers;

import cc.polyfrost.oneconfig.config.core.OneColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Constants;
import org.ginafro.notenoughfakepixel.variables.MobDisplayTypes;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlayerMobsDisplay {

    private Pattern pattern = Pattern.compile("Id:\"([^\"]+)\"");


    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (Configuration.slayerBosses) onRender(event, true);
        if (Configuration.slayerMinibosses) onRender(event, false);
    }

    private void onRender(RenderWorldLastEvent event, boolean isBoss) {
        switch (ScoreboardUtils.currentLocation) {
            case HUB:
            case PRIVATE_HUB:
                showHitboxHub(event);
                break;
            case PARK:
                showHitboxSven(event, isBoss);
                break;
            case SPIDERS_DEN:
                showHitboxTarantula(event, isBoss);
                break;
            case THE_END:
                showHitboxVoidgloom(event, isBoss);
                break;
            case CRIMSON_ISLE:
                showHitboxBlaze(event, isBoss);
                break;
        }
    }

    private void showHitboxHub(@NotNull RenderWorldLastEvent event) {
        showHitboxFromHub(MobDisplayTypes.NONE, Configuration.slayerColor, event.partialTicks);
    }

    private void showHitboxSven(@NotNull RenderWorldLastEvent event, boolean isBoss) {
        showHitbox(MobDisplayTypes.WOLF, Configuration.slayerColor, event.partialTicks, Constants.SVEN_SLAYER_MINIBOSSES, isBoss);
    }

    private void showHitboxTarantula(@NotNull RenderWorldLastEvent event, boolean isBoss) {
        showHitbox(MobDisplayTypes.SPIDER, Configuration.slayerColor, event.partialTicks, Constants.TARANTULA_SLAYER_MINIBOSSES, isBoss);
    }

    private void showHitboxVoidgloom(@NotNull RenderWorldLastEvent event, boolean isBoss) {
        showHitbox(MobDisplayTypes.ENDERMAN, Configuration.slayerColor, event.partialTicks, Constants.VOIDGLOOM_SLAYER_MINIBOSSES, isBoss);
    }

    private void showHitboxBlaze(@NotNull RenderWorldLastEvent event, boolean isBoss) {
        showHitbox(MobDisplayTypes.NONE, Configuration.slayerColor, event.partialTicks, Constants.BLAZE_SLAYER_MINIBOSSES, isBoss);
    }

    private void showHitbox(MobDisplayTypes type, OneColor configColor, float partialTicks, String[] namesList, boolean isBoss) {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        world.loadedEntityList.forEach(entity -> {
            if (entity == null || entity.getName() == null) return;
            if (!(entity instanceof EntityArmorStand)) return;

            String[] names = isBoss ? Constants.SLAYER_BOSSES : namesList;
            Color color = new Color(
                    configColor.getRed(),
                    configColor.getGreen(),
                    configColor.getBlue(),
                    configColor.getAlpha()
            );
            if (isBoss) {
                color = new Color(
                        Configuration.slayerBossColor.getRed(),
                        Configuration.slayerBossColor.getGreen(),
                        Configuration.slayerBossColor.getBlue(),
                        Configuration.slayerBossColor.getAlpha()
                );
            }

            for (String name : names) {
                if (entity.getName().contains(name)) {
                    RenderUtils.renderEntityHitbox(
                            entity,
                            partialTicks,
                            color,
                            type,
                            isBoss
                    );
                }
            }
        });

    }

    private void showHitboxFromHub(MobDisplayTypes type, OneColor configColor, float partialTicks) {

        WorldClient world = Minecraft.getMinecraft().theWorld;
        world.loadedEntityList.forEach(entity -> {
            if (entity == null) return;
            if (entity.getName() == null) return;


            for (String name : Constants.SLAYER_BOSSES){
                if (entity.getName().contains(name)) {
                    MobDisplayTypes entityType = type;
                    if (entity.getName().contains("Sven Packmaster")){
                        entityType = MobDisplayTypes.WOLF;
                    }

                    RenderUtils.renderEntityHitbox(
                            entity,
                            partialTicks,
                            new Color(
                                    Configuration.slayerBossColor.getRed(),
                                    Configuration.slayerBossColor.getGreen(),
                                    Configuration.slayerBossColor.getBlue(),
                                    Configuration.slayerBossColor.getAlpha()
                            ),
                            entityType,
                            true
                    );
                    return;
                }
            }

            Color color = new Color(
                    Configuration.slayerColor.getRed(),
                    Configuration.slayerColor.getGreen(),
                    Configuration.slayerColor.getBlue(),
                    Configuration.slayerColor.getAlpha()
            );

            for (String name : Constants.SVEN_SLAYER_MINIBOSSES) {
                if (entity.getName().contains(name)) {
                    RenderUtils.renderEntityHitbox(
                            entity,
                            partialTicks,
                            color,
                            MobDisplayTypes.WOLF,
                            false
                    );
                    return;
                }
            }

            for (String name : Constants.REVENANT_SLAYER_MINIBOSSES){
                if (entity.getName().contains(name)) {
                    RenderUtils.renderEntityHitbox(
                            entity,
                            partialTicks,
                            color,
                            MobDisplayTypes.NONE,
                            false
                    );
                    return;
                }
            }
        });
    }

}
