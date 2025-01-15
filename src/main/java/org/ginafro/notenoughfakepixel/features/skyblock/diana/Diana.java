package org.ginafro.notenoughfakepixel.features.skyblock.diana;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.events.PacketReadEvent;
import net.minecraft.client.Minecraft;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import net.minecraftforge.event.entity.player.*;
import org.ginafro.notenoughfakepixel.variables.MobDisplayTypes;
import org.ginafro.notenoughfakepixel.utils.InventoryUtils;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.ginafro.notenoughfakepixel.Configuration.*;

public class Diana {
    private static BlockPos overlayLoc = null;
    ParticleProcessor processor = new ParticleProcessor();
    Color white = new Color(255, 255, 255, 100);
    private Queue<GaiaConstruct> listGaiaAlive = new ConcurrentLinkedQueue<>();
    private Queue<SiameseLynx> listSiameseAlive = new ConcurrentLinkedQueue<>();
    private int distanceRenderHitbox = 64;

    Color red = new Color(255,0,0,255);
    Color green = new Color(0,255,0,255);

    @SubscribeEvent
    public void onPacketReceive(PacketReadEvent event) {
        if (!Configuration.dianaGeneral) return; // Check if the feature is enabled
        if (!ScoreboardUtils.currentLocation.isHub()) return; // Check if the player is in a hub
        Packet packet = event.packet;
         if (packet instanceof S2APacketParticles) {
             S2APacketParticles particles = (S2APacketParticles) packet;
             //System.out.println(particles.getParticleType().getParticleName());
             // magicCrit enchantmenttable footstep -> empty (blue)
             // crit enchantmenttable -> mob (white)
             // dripLava enchantmenttable -> treasure (brown)
             if (particles.getParticleType().getParticleName().equals("crit") ||
                     particles.getParticleType().getParticleName().equals("magicCrit") ||
                     particles.getParticleType().getParticleName().equals("dripLava") ||
                     particles.getParticleType().getParticleName().equals("enchantmenttable") ||
                     particles.getParticleType().getParticleName().equals("footstep")) {

                 processor.addParticle(particles);

             } else if (particles.getParticleType().getParticleName().equals("angryVillager")) {
                // Siamese Lynx feature
                 Entity closestSiamese = getClosestSiamese(new int[] {(int)particles.getXCoordinate(), (int)particles.getYCoordinate(), (int)particles.getZCoordinate()});
                 if (closestSiamese != null) {
                     for (SiameseLynx siamese : listSiameseAlive) {
                         if (siamese.getEntity1().getUniqueID() == closestSiamese.getUniqueID()) {
                             System.out.println("Siamese1 hittable");
                             siamese.setHittable(closestSiamese);
                             break;
                         } else if (siamese.getEntity2().getUniqueID() == closestSiamese.getUniqueID()) {
                             System.out.println("Siamese2 hittable");
                             siamese.setHittable(closestSiamese);
                             break;
                         }
                     }
                 }
             }

         }
    }

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        if (!Configuration.dianaGeneral) return; // Check if the feature is enabled
        if (!ScoreboardUtils.currentLocation.isHub()) return; // Check if the player is in a hub
        drawWaypoints(event.partialTicks); // Draw waypoints
        if (!Configuration.dianaGaiaConstruct) return; // Check if the feature is enabled
        dianaMobCheck();
        dianaMobRemover();
        dianaMobRender(event.partialTicks); // Check for gaia constructs in entities and draw a hitbox according hp and hit status
    }

    private void drawWaypoints(float partialTicks) {
        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        List<ParticleProcessor.ClassificationResult> safeResults = new ArrayList<>();
        synchronized (processor.getProcessedGroups()) {
            try {
                safeResults = new ArrayList<>(processor.getProcessedGroups());
            } catch (Exception ignored) {}
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

    private void dianaMobRender(float partialTicks) {

        WorldClient world = Minecraft.getMinecraft().theWorld;
        // Iterate world entities
        world.loadedEntityList.forEach(entity -> {
            if (entity == null) return;
            if (entity.getName() == null) return;
            if (entity instanceof EntityGolem){
                for (GaiaConstruct gaia : listGaiaAlive) {
                    Entity gaiaEntity = gaia.getEntity();
                    if (gaiaEntity.getUniqueID() == entity.getUniqueID()) {
                        if (gaia.canBeHit()) {
                            RenderUtils.renderEntityHitbox(
                                    gaiaEntity,
                                    partialTicks,
                                    new Color(Configuration.gaiaHittableColor.getRed(), Configuration.gaiaHittableColor.getGreen(), Configuration.gaiaHittableColor.getBlue(), 150),
                                    MobDisplayTypes.GAIA
                            );
                        } else {
                            RenderUtils.renderEntityHitbox(
                                    gaiaEntity,
                                    partialTicks,
                                    new Color(gaiaUnhittableColor.getRed(), gaiaUnhittableColor.getGreen(), gaiaUnhittableColor.getBlue(), 150),
                                    MobDisplayTypes.GAIA
                            );
                        }
                        break;
                    }
                }
            } else if (entity instanceof EntityOcelot){
                for (SiameseLynx siamese : listSiameseAlive) {
                    if (siamese.getHittable() == null) continue;
                    RenderUtils.renderEntityHitbox(
                            siamese.getHittable(),
                            partialTicks,
                            new Color(Configuration.siameseHittableColor.getRed(), Configuration.siameseHittableColor.getGreen(), Configuration.siameseHittableColor.getBlue(), 150),
                            MobDisplayTypes.SIAMESE
                    );
                }

            }
        });
    }

    private void dianaMobCheck() {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        // Iterate world entities
        world.loadedEntityList.forEach(entity -> {
            if (entity == null) return;
            if (entity.getName() == null) return;
            if (entity instanceof EntityGolem){
                // Iterate gaia list
                for (GaiaConstruct gaia : listGaiaAlive) {
                    // If already added, don't add again
                    if (gaia.getEntity().getUniqueID() == entity.getUniqueID()) return;
                }
                // If this point reached, no occurrences, so new gaia added
                listGaiaAlive.add(new GaiaConstruct(entity));
                System.out.println("Gaia added, "+listGaiaAlive.size());
            } else if (entity instanceof EntityOcelot){
                for (SiameseLynx siamese : listSiameseAlive) {

                    // If already added, don't add again
                    if (siamese.getEntity1().getUniqueID() == entity.getUniqueID()) return;
                    if (siamese.getEntity2() == null) {
                        siamese.setEntity2(entity);
                        System.out.println("Ocelot2 added, "+listSiameseAlive.size());
                    }
                    if (siamese.getEntity2().getUniqueID() == entity.getUniqueID()) return;
                }
                // If this point reached, no occurrences, so new siamese added
                listSiameseAlive.add(new SiameseLynx(entity));
                System.out.println("Siamese added, "+listSiameseAlive.size());
            }
        });
    }

    private void dianaMobRemover() {
        int[] playerCoords = new int[] {(int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ};
        for (GaiaConstruct gaia : listGaiaAlive) {
            int[] gaiaCoords = new int[]{gaia.getEntity().getPosition().getX(), gaia.getEntity().getPosition().getY(), gaia.getEntity().getPosition().getZ()};
            if (!processor.areCoordinatesClose(playerCoords,gaiaCoords,distanceRenderHitbox)) {
                listGaiaAlive.remove(gaia);
                System.out.println("Gaia removed for distance, "+listGaiaAlive.size());
            }
        }
        for (SiameseLynx siamese : listSiameseAlive) {
            if (siamese.getEntity1() == null && siamese.getEntity2() == null) {
                listSiameseAlive.remove(siamese);
                System.out.println("Siamese removed, "+listSiameseAlive.size());
                return;
            }
            if (siamese.getEntity1() != null) {
                int[] siamese1Coords = new int[]{siamese.getEntity1().getPosition().getX(), siamese.getEntity1().getPosition().getY(), siamese.getEntity1().getPosition().getZ()};
                if (!processor.areCoordinatesClose(playerCoords, siamese1Coords, distanceRenderHitbox)) {
                    siamese.setEntity1(null);
                }
            }
            if (siamese.getEntity2() != null) {
                int[] siamese2Coords = new int[]{siamese.getEntity2().getPosition().getX(), siamese.getEntity2().getPosition().getY(), siamese.getEntity2().getPosition().getZ()};
                if (!processor.areCoordinatesClose(playerCoords, siamese2Coords, distanceRenderHitbox)) {
                    siamese.setEntity2(null);
                }
            }
        }
    }

    /*@SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        System.out.println("\n\n"+event.entity.getName()+ " hurt");
        if (event.entity instanceof EntityGolem) { // check if golem has been hurt
            System.out.println("Golem hurt");
            for (GaiaConstruct gaia : listGaiaAlive) {
                if (gaia.getEntity() == event.entity) gaia.addHit();
            }
        }
    }*/
    @SubscribeEvent
    public void onSoundPacketReceive(PacketReadEvent event) {
        if (!Configuration.dianaGeneral) return; // Check if the feature is enabled
        if (!Configuration.dianaGaiaConstruct) return; // Check if the feature is enabled
        if (!ScoreboardUtils.currentLocation.isHub()) return; // Check if the player is in a hub
        Packet packet = event.packet;
        if (packet instanceof S29PacketSoundEffect) {
            S29PacketSoundEffect soundEffect = (S29PacketSoundEffect) packet;
            //System.out.println(soundEffect.getSoundName());
            int[] coordsSound = new int[] {(int)Math.floor(soundEffect.getX()), (int)Math.floor(soundEffect.getY()), (int)Math.floor(soundEffect.getZ())};
            // List<GaiaConstruct> safeResults = new ArrayList<GaiaConstruct>(listGaiaAlive);
            String soundName = soundEffect.getSoundName();
            switch (soundName) {
                // Remove explosion sound feature
                case "random.explode":
                    if (Math.floor(soundEffect.getPitch()*1000)/1000 == 1.190) {
                        if (Configuration.disableDianaExplosionSounds) {
                            if (event.isCancelable()) event.setCanceled(true);
                        }
                    }
                    break;
                // Remove waypoint at pling sound
                case "note.pling":
                    deleteClosestWaypoint(coordsSound[0],coordsSound[1],coordsSound[2]);
                    break;
                // Gaia track hits feature
                case "mob.zombie.metal":
                case "mob.irongolem.death":
                case "mob.irongolem.hit":
                    // Gaia track hits feature
                    GaiaConstruct closestGaia = getClosestGaia(coordsSound);
                    if (closestGaia == null) return;
                    if (soundName.equals("mob.zombie.metal") || soundName.equals("mob.irongolem.hit")) {
                        closestGaia.addHit();
                        System.out.println("Hit registered, "+closestGaia.getHits());
                    } else if (soundName.equals("mob.irongolem.death")) {
                        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
                        exec.schedule(new Runnable() {
                            public void run() {
                                listGaiaAlive.remove(closestGaia);
                                System.out.println("Gaia removed, " + listGaiaAlive.size());
                            }
                        }, 1, TimeUnit.SECONDS);
                    }
                    break;
                case "note.harp":
                    /*if (Configuration.disableDianaHarpSounds) {
                        if (event.isCancelable()) event.setCanceled(true);
                    }*/
                    break;
                // Siamese Lynx feature
                case "mob.cat.hitt":
                    //SiameseLynx closestSiamese = getClosestSiamese(coordsSound);
                    //closestSiamese.addHit();
                    break;
            }

            // Remove harp sound
            /*if (soundEffect.getSoundName().equals("note.harp")) {
                if (Configuration.disableDianaHarpSounds) {
                    System.out.println(soundEffect.getPitch());
                    System.out.println(soundEffect.getVolume());
                    if (event.isCancelable()) event.setCanceled(true);
                }
                return;
            }*/
        }
    }

    private GaiaConstruct getClosestGaia(int[] coords) {
        GaiaConstruct returnedGaia = null;
        float distance = Float.MAX_VALUE;
        for (GaiaConstruct gaia : listGaiaAlive) {
            int[] coordsGaia = new int[] {gaia.getEntity().getPosition().getX(), gaia.getEntity().getPosition().getY() ,gaia.getEntity().getPosition().getZ()};
            if (processor.getDistance(coords, coordsGaia) < distance) {
                distance = processor.getDistance(coords, coordsGaia);
                returnedGaia = gaia;
            }
        }
        return returnedGaia;
    }

    private Entity getClosestSiamese(int[] coords) {
        Entity returnedSiamese = null;
        float distance = Float.MAX_VALUE;
        for (SiameseLynx siamese : listSiameseAlive) {
            int[] coordsSiamese1 = new int[] {siamese.getEntity1().getPosition().getX(), siamese.getEntity1().getPosition().getY() ,siamese.getEntity1().getPosition().getZ()};
            int[] coordsSiamese2 = new int[] {siamese.getEntity2().getPosition().getX(), siamese.getEntity2().getPosition().getY() ,siamese.getEntity2().getPosition().getZ()};
            if (processor.getDistance(coords, coordsSiamese1) < distance) {
                distance = processor.getDistance(coords, coordsSiamese1);
                returnedSiamese = siamese.getEntity1();
            }
            if (processor.getDistance(coords, coordsSiamese2) < distance) {
                distance = processor.getDistance(coords, coordsSiamese2);
                returnedSiamese = siamese.getEntity2();
            }

        }
        return returnedSiamese;
    }


    @SubscribeEvent
    public void handleClick(PlayerInteractEvent event) {
        if (!Configuration.dianaGeneral) return; // Check if the feature is enabled
        if (!ScoreboardUtils.currentLocation.isHub()) return; // Check if the player is in a hub
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) return; // Check if right click on air
        //System.out.println(event.face + ", "+event.pos);
        //if (!event.face.equals("up")) return;
        //deleteClosestWaypoint(event.pos.getX(),event.pos.getY(),event.pos.getZ());
        if (Configuration.dianaAutoEquipAncestralSpade) autoEquipShovel(event.face.getName(),event.pos.getX(),event.pos.getY(),event.pos.getZ());
        //System.out.println(Minecraft.getMinecraft().thePlayer.getHeldItem().getDisplayName());
    }

    private void deleteClosestWaypoint(int x, int y, int z) {
        //int[] playerCoords = new int[] {(int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ};
        int[] coords = new int[] {x, y, z};
        ParticleProcessor.ClassificationResult res = processor.getClosestResult(coords);

        if (res == null) return;
        if (processor.areCoordinatesClose(res.getCoordinates(), coords, 3)) {
            res.setHidden(true);
            ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
            exec.schedule(new Runnable() {
                public void run() {
                    processor.deleteProcessedGroup(res);
                }
            }, 30000, TimeUnit.MILLISECONDS);
        }
    }

    private void autoEquipShovel(String face, int x, int y, int z) {
        if (!face.equals("up")) return;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        int[] playerCoords = new int[] {(int)player.posX, (int)player.posY, (int)player.posZ};
        ParticleProcessor.ClassificationResult res = processor.getClosestResult(playerCoords);
        if (res == null) return;
        if (res.isHidden()) return;
        int[] coordsBurrowClicked = new int[]{x, y+1, z};
        if (!Arrays.equals(res.getCoordinates(), coordsBurrowClicked)) return;
        // Clicked on a burrow
        if (InventoryUtils.getHeldItem() == null) return;
        if (!InventoryUtils.getHeldItem().getDisplayName().contains("Ancestral Spade")) {
            int slot = InventoryUtils.getSlot("Ancestral Spade");
            if (slot == -1) return;
            int currentSlot = InventoryUtils.getCurrentSlot();
            ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
            exec.schedule(new Runnable() {
                public void run() {
                    InventoryUtils.goToSlot(currentSlot);
                }
            }, 100, TimeUnit.MILLISECONDS);
            InventoryUtils.goToSlot(slot);
        }


    }

    @SubscribeEvent()
    public void onWorldUnload(WorldEvent.Unload event) {
        if (!Configuration.dianaGeneral) return; // Check if the feature is enabled
        processor.clearProcessedGroups();
        if (!Configuration.dianaGaiaConstruct) return; // Check if the feature is enabled
        listGaiaAlive.clear();
    }
}
