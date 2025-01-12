package org.ginafro.notenoughfakepixel.features.skyblock.diana;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.events.PacketReadEvent;
import net.minecraft.util.EnumParticleTypes;
import org.ginafro.notenoughfakepixel.gui.impl.Waypoint;
import org.ginafro.notenoughfakepixel.gui.impl.WaypointGUI;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import net.minecraftforge.event.entity.player.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.ginafro.notenoughfakepixel.Configuration.*;

public class Diana {
    private static BlockPos overlayLoc = null;
    private static ParticleProcessor processor = new ParticleProcessor();
    Color white = new Color(255, 255, 255, 100);

    @SubscribeEvent
    public void onPacketReceive(PacketReadEvent event) {
        if (!Configuration.dianaGeneral) return; // Check if the feature is enabled
        if (!ScoreboardUtils.currentLocation.isHub()) return; // Check if the player is in a hub
        Packet packet = event.packet;
         if (packet instanceof S2APacketParticles) {
             S2APacketParticles particles = (S2APacketParticles) packet;
             if (particles.getParticleType().getParticleName().equals("crit") ||
                     particles.getParticleType().getParticleName().equals("magicCrit") ||
                     particles.getParticleType().getParticleName().equals("dripLava") ||
                     particles.getParticleType().getParticleName().equals("enchantmenttable") ||
                     particles.getParticleType().getParticleName().equals("footstep")) {
                 processor.addParticle(particles);
             }
         }
    }

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        if (!Configuration.dianaGeneral) return; // Check if the feature is enabled
        if (!ScoreboardUtils.currentLocation.isHub()) return; // Check if the player is in a hub
        drawWaypoints(event.partialTicks); // Draw waypoints
    }

    private void drawWaypoints(float partialTicks) {
        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        List<ParticleProcessor.ClassificationResult> safeResults = new ArrayList<>();
        synchronized (processor.getProcessedGroups()) {
            // THIS CAN CONTAIN CONCURRENT MODIFICATION EXCEPTIONS
            try {
                safeResults = new ArrayList<>(processor.getProcessedGroups());
            } catch (Exception ignored){}
        }
        for (ParticleProcessor.ClassificationResult result : safeResults) {
            if (result.isHidden()) continue;
            //RenderUtils.renderBeaconBeam(result.getCoordinates()[0], result.getCoordinates()[1], result.getCoordinates()[2], 0x1fd8f1, 1.0f, event.partialTicks, true);
            Color newColor = white;
            if (result.getType().equals("EMPTY")) newColor = emptyBurrowColor.toJavaColor();
            if (result.getType().equals("MOB")) newColor = mobBurrowColor.toJavaColor();
            if (result.getType().equals("TREASURE")) newColor = treasureBurrowColor.toJavaColor();
            newColor = new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), 100);
            AxisAlignedBB bb = new AxisAlignedBB(
                    result.getCoordinates()[0] - viewerX,
                    result.getCoordinates()[1] - viewerY,
                    result.getCoordinates()[2] - viewerZ,
                    result.getCoordinates()[0] + 1 - viewerX,
                    result.getCoordinates()[1] + 1 - viewerY + 100,
                    result.getCoordinates()[2] + 1 - viewerZ
            ).expand(0.01f, 0.01f, 0.01f);
            GlStateManager.disableCull();
            RenderUtils.drawFilledBoundingBox(bb, 1f, newColor);
            GlStateManager.enableCull();
            GlStateManager.enableTexture2D();
        }
    }

    @SubscribeEvent
    public void handleClick(PlayerInteractEvent event) {
        if (!Configuration.dianaGeneral) return; // Check if the feature is enabled
        if (!ScoreboardUtils.currentLocation.isHub()) return; // Check if the player is in a hub
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) return; // Check if right click on air
        deleteClosestWaypoint();
    }

    private void deleteClosestWaypoint() {
        int[] playerCoords = new int[] {(int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ};
        ParticleProcessor.ClassificationResult res = processor.getClosestResult(playerCoords);
        if (res == null) return;
        if (Minecraft.getMinecraft().thePlayer.getHeldItem().getDisplayName().contains("Ancestral Spade") && processor.areCoordinatesClose(playerCoords, res.getCoordinates())) {
            ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
            if (res.getType().equals("EMPTY") || res.getType().equals("TREASURE")) {
                res.setHidden(true);
                exec.schedule(new Runnable() {
                    public void run() {
                        processor.deleteProcessedGroup(res);
                    }
                }, 20000, TimeUnit.MILLISECONDS);

            } else if (res.getType().equals("MOB")) {
                if (res.getState() == 0) res.setState(1);
                else if (res.getState() == 1) {
                    res.setHidden(true);
                    exec.schedule(new Runnable() {
                        public void run() {
                            processor.deleteProcessedGroup(res);
                        }
                    }, 20000, TimeUnit.MILLISECONDS);
                }
            }
        }
    }
}
