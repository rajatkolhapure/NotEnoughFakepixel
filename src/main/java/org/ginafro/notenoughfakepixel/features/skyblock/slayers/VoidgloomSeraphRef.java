package org.ginafro.notenoughfakepixel.features.skyblock.slayers;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBeacon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.events.BlockChangeEvent;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.utils.SoundUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Handles Voidgloom Seraph-related features, including beacon highlighting, path recording,
 * and skull or block highlighting in Minecraft Skyblock gameplay.
 */
public class VoidgloomSeraphRef {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static boolean activateFeatures = false;
    private static final Color BEACON_COLOR = new Color(0, 255, 0, 77);

    private static BlockPos beacon = null;
    private static EntityArmorStand beaconEntity = null;
    private static final ArrayList<Vec3> beaconPath = new ArrayList<>();
    private static int ticksCounter = 0;

    /**
     * Called on each client tick. Handles beacon detection, activation toggling, and path recording.
     */
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!isInSkyblockEnd()) {
            activateFeatures = false;
            return;
        }

        if (Configuration.slayerIgnoreOtherVoidgloom) {
            activateFeatures = ScoreboardUtils.scoreboardContains("Voidgloom Seraph") &&
                    ScoreboardUtils.scoreboardContains("Slay the boss!");
            if (!activateFeatures) {
                reset();
            }
        } else {
            activateFeatures = true;
        }

        detectBeaconEntity();

        if (Configuration.slayerShowBeaconPath && beaconEntity != null && activateFeatures) {
            recordBeaconPath(beaconEntity);
        }

        if (Configuration.slayerBeaconWarningTitle && activateFeatures && beacon != null) {
            displayBeaconWarning();
        }
    }

    /**
     * Detects beacon entities near the player and updates the beacon entity reference.
     */
    private void detectBeaconEntity() {
        if (mc.thePlayer == null) return;

        AxisAlignedBB detectionBox = mc.thePlayer.getEntityBoundingBox().expand(20, 20, 20);
        Collection<EntityArmorStand> entities = mc.theWorld.getEntitiesWithinAABB(EntityArmorStand.class, detectionBox);

        for (EntityArmorStand entity : entities) {
            for (ItemStack item : entity.getInventory()) {
                if (item != null && item.getItem() == Item.getItemFromBlock(Blocks.beacon)) {
                    beaconEntity = entity;
                    break;
                }
            }
        }
    }

    /**
     * Records the position of the beacon entity to the path list.
     */
    private void recordBeaconPath(EntityArmorStand entity) {
        Vec3 beaconPosition = entity.getPositionVector().addVector(0, 1, 0);
        beaconPath.add(beaconPosition);
        ticksCounter++;
    }

    /**
     * Displays a visual and auditory warning for a spawned beacon.
     */
    private void displayBeaconWarning() {
        mc.ingameGUI.displayTitle(EnumChatFormatting.GOLD + "BEACON SPAWNED", "", 2, 10, 2);
        int[] coordsPlayer = new int[] {mc.thePlayer.getPosition().getX(), mc.thePlayer.getPosition().getY(), mc.thePlayer.getPosition().getZ()};
        SoundUtils.playSound(coordsPlayer, "random.anvil_land", 3.0f, 1.0f);
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        if (!isInSkyblockEnd() || !activateFeatures) return;

        try {
            if (beacon == null && beaconEntity != null && beaconEntity.getPosition().distanceSq(event.position) < 5 &&
                    event.newBlock.getBlock() instanceof BlockBeacon) {
                beaconEntity = null;
                beacon = event.position;
            } else if (event.oldBlock.getBlock() instanceof BlockBeacon &&
                    event.newBlock.getBlock() instanceof BlockAir &&
                    event.position.equals(beacon)) {
                beacon = null;
            }
        } catch (Exception ignored) {
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!isInSkyblockEnd()) return;

        renderBeaconPath(event);

        if (Configuration.slayerHighlightVoidgloomBeacons && beacon != null && activateFeatures) {
            RenderUtils.highlightBlock(beacon, BEACON_COLOR, event.partialTicks);
        }

        if (Configuration.slayerHighlightVoidgloomSkulls && activateFeatures) {
            highlightFallingBlocks(event);
        }
    }

    private void renderBeaconPath(RenderWorldLastEvent event) {
        if (ticksCounter >= 120) {
            ticksCounter = 0;
            beaconPath.clear();
            return;
        }

        for (int i = 1; i < beaconPath.size(); i++) {
            int alpha = ticksCounter < 100 ? 255 : 255 * (120 - ticksCounter) / 20;
            RenderUtils.draw3DLine(beaconPath.get(i - 1), beaconPath.get(i),
                    new Color(0, 255, 0, alpha), 5, false, event.partialTicks);
        }
    }

    private void highlightFallingBlocks(RenderWorldLastEvent event) {
        AxisAlignedBB detectionBox = mc.thePlayer.getEntityBoundingBox().expand(20, 20, 20);
        Collection<EntityFallingBlock> entities = mc.theWorld.getEntitiesWithinAABB(EntityFallingBlock.class, detectionBox);

        for (EntityFallingBlock entity : entities) {
            RenderUtils.highlightBlock(entity.getPosition(), BEACON_COLOR, event.partialTicks);
        }
    }

    private void reset() {
        beacon = null;
        beaconPath.clear();
        ticksCounter = 0;
        beaconEntity = null;
    }

    private boolean isInSkyblockEnd() {
        return ScoreboardUtils.currentLocation.isEnd() && ScoreboardUtils.currentGamemode.isSkyblock();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (isInSkyblockEnd()) {
            reset();
        }
    }
}