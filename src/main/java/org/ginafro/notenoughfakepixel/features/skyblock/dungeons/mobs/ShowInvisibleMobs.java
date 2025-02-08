package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.mobs;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.DungeonManager;

public class ShowInvisibleMobs {

    @SubscribeEvent
    public void onRenderEntity(RenderWorldLastEvent event) {
        if (!Configuration.dungeonsShowFels && !Configuration.dungeonsShowShadowAssassin) return;
        if (!DungeonManager.checkEssentials()) return;

        Minecraft.getMinecraft().theWorld.loadedEntityList.forEach(entity -> {
            if (entity.isInvisible()) {
                boolean shouldShow = false;

                if (entity instanceof EntityEnderman) {
                    shouldShow = (Configuration.dungeonsShowFels && entity.getName().equals("Dinnerbone"));
                } else if (entity instanceof EntityPlayer) {
                    String entityName = entity.getName().trim();
                    shouldShow = (Configuration.dungeonsShowShadowAssassin && entityName.contains("Shadow Assassin"));
                }

                if (shouldShow) {
                    System.out.println(entity.getName() + " SET TO VISIBLE");
                    entity.setInvisible(false);
                }
            }
        });

    }

}
