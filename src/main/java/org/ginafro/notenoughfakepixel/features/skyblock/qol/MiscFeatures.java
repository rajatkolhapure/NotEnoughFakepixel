package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.regex.Pattern;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class MiscFeatures {
    ArrayList<Block> flowerPlaceable = new ArrayList<>(Arrays.asList(
            Blocks.grass,
            Blocks.dirt,
            Blocks.flower_pot,
            Blocks.tallgrass,
            Blocks.double_plant
    ));

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (!ScoreboardUtils.currentGamemode.isSkyblock() || Minecraft.getMinecraft().thePlayer != event.entityPlayer) return;
        ItemStack item = event.entityPlayer.getHeldItem();
        if (item == null) return;

        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            Block block = Minecraft.getMinecraft().theWorld.getBlockState(event.pos).getBlock();

            if (flowerPlaceable.contains(block)) {
                if (Configuration.qolBlockPlacingItems && item.getDisplayName().contains("Flower of Truth")) {
                    event.setCanceled(true);
                }
                if (Configuration.qolBlockPlacingItems && item.getDisplayName().contains("Spirit Sceptre")) {
                    event.setCanceled(true);
                }
            }

            if (Configuration.qolBlockPlacingItems && item.getItem() == Item.getItemFromBlock(Blocks.hopper) && item.getDisplayName().contains("Weird Tuba")) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderLivingSpecialsPre(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (Configuration.qolHideDyingMobs) {
            EntityLivingBase entity = event.entity;
            String name = entity.getDisplayName().getUnformattedText();

            Pattern pattern1 = Pattern.compile("^§.\\[§.Lv\\d+§.\\] §.+ (?:§.)+0§f/.+§c❤$");
            Pattern pattern2 = Pattern.compile("^.+ (?:§.)+0§c❤$");

            if (pattern1.matcher(name).matches() || pattern2.matcher(name).matches()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onRenderLivingPre(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (Configuration.qolHideDyingMobs) {
            EntityLivingBase entity = event.entity;
            if (entity.getHealth() <= 0 || entity.isDead) {
                event.setCanceled(true);
            }
        }
    }
}
