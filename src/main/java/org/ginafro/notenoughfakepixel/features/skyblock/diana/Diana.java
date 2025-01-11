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
import org.ginafro.notenoughfakepixel.events.PacketReadEvent;
import net.minecraft.util.EnumParticleTypes;
import org.ginafro.notenoughfakepixel.gui.impl.Waypoint;
import org.ginafro.notenoughfakepixel.gui.impl.WaypointGUI;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

import java.awt.*;
import java.util.List;

public class Diana {
    private static BlockPos overlayLoc = null;
    ParticleProcessor processor = new ParticleProcessor();
    Color red = new Color(255, 0, 0, 100);
    Color green = new Color(0, 255, 0, 100);
    Color white = new Color(255, 255, 255, 100);
    Color blue = new Color(0, 0, 255, 100);
    @SubscribeEvent
    public void onPacketReceive(PacketReadEvent event) {
        if (!ScoreboardUtils.currentLocation.isHub()) return; // Check if the player is in a hub
        Packet packet = event.packet;
         if (packet instanceof S2APacketParticles) {
             S2APacketParticles particles = (S2APacketParticles) packet;
             if (particles.getParticleType().getParticleName().equals("crit") ||
                     particles.getParticleType().getParticleName().equals("magicCrit") ||
                     particles.getParticleType().getParticleName().equals("dripLava") ||
                     particles.getParticleType().getParticleName().equals("enchantmenttable") ||
                     particles.getParticleType().getParticleName().equals("footstep")) {
                 double x = particles.getXCoordinate();
                 double y = particles.getYCoordinate();
                 double z = particles.getZCoordinate();
                 if (particles.getParticleType().getParticleName().equals("dripLava")) {
                    System.out.println(particles.getParticleType().getParticleName());
                    System.out.println(particles.getParticleCount());
                     System.out.println(particles.getXOffset());
                     System.out.println(particles.getYOffset());
                     System.out.println(particles.getZOffset());
                     //System.out.println(String.valueOf(x) + ", " + String.valueOf(y) + ", " + String.valueOf(z));
                 }


                 //if (particles.getParticleType().getParticleName().equals("dripLava") && ) {}
                 processor.addParticle(particles);

                 // magicCrit enchantmenttable footstep -> empty (blue)
                 // crit enchantmenttable -> mob (white)
                 // dripLava enchantmenttable -> treasure (brown)
             }

         }
    }

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        if (!ScoreboardUtils.currentLocation.isHub()) return; // Check if the player is in a hub
        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks;

        for (ParticleProcessor.ClassificationResult result : processor.getProcessedGroups()) {
            //RenderUtils.renderBeaconBeam(result.getCoordinates()[0], result.getCoordinates()[1], result.getCoordinates()[2], 0x1fd8f1, 1.0f, event.partialTicks, true);
            Color newColor = white;
            if (result.getType().equals("EMPTY")) newColor = blue;
            if (result.getType().equals("MOB")) newColor = white;
            if (result.getType().equals("TREASURE")) newColor = red;
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
    public void onGuiBackgroundRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!ScoreboardUtils.currentLocation.isHub()) return; // Check if the player is in a hub
        processor.clearProcessedGroups();
    }
}
