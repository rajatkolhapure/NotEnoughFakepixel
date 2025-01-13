package org.ginafro.notenoughfakepixel.features.skyblock.diana;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.events.PacketReadEvent;
import net.minecraft.client.Minecraft;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import net.minecraftforge.event.entity.player.*;
import org.ginafro.notenoughfakepixel.variables.MobDisplayTypes;
import net.minecraft.entity.EntityLivingBase;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.eventhandler.Event;

import static org.ginafro.notenoughfakepixel.Configuration.*;

public class Diana {
    private static BlockPos overlayLoc = null;
    ParticleProcessor processor = new ParticleProcessor();
    Color white = new Color(255, 255, 255, 100);
    private Queue<GaiaConstruct> listGaiaAlive = new ConcurrentLinkedQueue<>();
    Color red = new Color(255,0,0,255);
    Color green = new Color(0,255,0,255);

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
                 /*double x = particles.getXCoordinate();
                 double y = particles.getYCoordinate();
                 double z = particles.getZCoordinate();*/
                 /*if (particles.getParticleType().getParticleName().equals("dripLava")) {
                    System.out.println(particles.getParticleType().getParticleName());
                    System.out.println(particles.getParticleCount());
                     System.out.println(particles.getXOffset());
                     System.out.println(particles.getYOffset());
                     System.out.println(particles.getZOffset());
                     //System.out.println(String.valueOf(x) + ", " + String.valueOf(y) + ", " + String.valueOf(z));
                 }*/


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
        if (!Configuration.dianaGeneral) return; // Check if the feature is enabled
        if (!ScoreboardUtils.currentLocation.isHub()) return; // Check if the player is in a hub
        drawWaypoints(event.partialTicks); // Draw waypoints
        if (!Configuration.dianaGaiaConstruct) return; // Check if the feature is enabled
        gaiaConstructRender(event.partialTicks); // Check for gaia constructs in entities and draw a hitbox according hp and hit status
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

    private void gaiaConstructRender(float partialTicks) {
        gaiaConstructCheck();
        gaiaConstructRemover();

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
            }
        });

        /*for (GaiaConstruct gaia : listGaiaAlive) {
            Entity entity = gaia.getEntity();
            if (gaia.canBeHit()) {
                RenderUtils.renderEntityHitbox(
                        entity,
                        partialTicks,
                        new Color(0, 255, 0, 150),
                        MobDisplayTypes.GAIA
                );
            } else {
                RenderUtils.renderEntityHitbox(
                        entity,
                        partialTicks,
                        new Color(255, 0, 0, 150),
                        MobDisplayTypes.GAIA
                );
            }
        }*/
    }

    private void gaiaConstructCheck() {
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
                //EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                //System.out.println(entity.toString());
                //System.out.println(entity.getName()+", "+entity.getDisplayName()+", "+entity.getCustomNameTag());
                //System.out.println(entityLivingBase.getName()+", "+entityLivingBase.getDisplayName()+", "+entityLivingBase.getCustomNameTag());
                //System.out.println(entityLivingBase.getHealth());
                // If this point reached, no occurrences, so new gaia added
                listGaiaAlive.add(new GaiaConstruct(entity));
                //System.out.println("Gaia added, "+listGaiaAlive.size());
            }
        });
    }

    private void gaiaConstructRemover() {
        int[] playerCoords = new int[] {(int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ};
        for (GaiaConstruct gaia : listGaiaAlive) {
            int[] gaiaCoords = new int[]{gaia.getEntity().getPosition().getX(), gaia.getEntity().getPosition().getY(), gaia.getEntity().getPosition().getZ()};
            if (!processor.areCoordinatesClose(playerCoords,gaiaCoords,25)) {
                listGaiaAlive.remove(gaia);
                //System.out.println("Gaia removed, "+listGaiaAlive.size());
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
            if (!(soundEffect.getSoundName().equals("mob.zombie.metal") || soundEffect.getSoundName().equals("mob.irongolem.death") || soundEffect.getSoundName().equals("mob.irongolem.hit"))) return;
            int[] coordsSound = new int[] {(int)Math.floor(soundEffect.getX()), (int)Math.floor(soundEffect.getY()), (int)Math.floor(soundEffect.getZ())};
            // List<GaiaConstruct> safeResults = new ArrayList<GaiaConstruct>(listGaiaAlive);
            GaiaConstruct closestGaia = getClosestGaia(coordsSound);
            if (closestGaia == null) return;
            if (soundEffect.getSoundName().equals("mob.zombie.metal")) {
                closestGaia.addHit();
                //System.out.println(listGaiaAlive.size());
            } else if (soundEffect.getSoundName().equals("mob.irongolem.hit")) {
                closestGaia.setHits(0);

            } else if (soundEffect.getSoundName().equals("mob.irongolem.death")) {
                ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
                exec.schedule(new Runnable() {
                    public void run() {
                        listGaiaAlive.remove(closestGaia);
                        //System.out.println("Gaia removed, "+listGaiaAlive.size());
                    }
                }, 1, TimeUnit.SECONDS);
            }
        }
    }

    private GaiaConstruct getClosestGaia(int[] coords) {
        GaiaConstruct returnedGaia = null;
        float distance = Float.MAX_VALUE;
        for (GaiaConstruct gaia : listGaiaAlive) {
            int[] coordsGaia = new int[] {gaia.getEntity().getPosition().getX(), gaia.getEntity().getPosition().getY() ,gaia.getEntity().getPosition().getZ()};
            if (processor.getDistance(coords, coordsGaia) < distance) {
                returnedGaia = gaia;
            }
        }
        return returnedGaia;
    }

    /*@SubscribeEvent
    public void onIronGolemHit(PlaySoundAtEntityEvent event) {
        if (!Configuration.dianaGeneral) return; // Check if the feature is enabled
        if (!ScoreboardUtils.currentLocation.isHub()) return; // Check if the player is in a dungeon
        System.out.println(event.name);
        if (!(event.name.equals("random.anvil_land")) ) return;
        System.out.println(event.name);
        if (event.entity instanceof EntityGolem) { // check if golem has been hurt
            System.out.println("Golem hurt");
            for (GaiaConstruct gaia : listGaiaAlive) {
                if (gaia.getEntity() == event.entity) {
                    System.out.println("Hit added");
                    gaia.addHit();
                }
            }
        }
    }*/


    @SubscribeEvent
    public void handleClick(PlayerInteractEvent event) {
        if (!Configuration.dianaGeneral) return; // Check if the feature is enabled
        if (!ScoreboardUtils.currentLocation.isHub()) return; // Check if the player is in a hub
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) return; // Check if right click on air
        //System.out.println(event.face + ", "+event.pos);

        deleteClosestWaypoint(event.face.getName(),event.pos.getX(),event.pos.getY(),event.pos.getZ());
        autoEquipShovel(event.face.getName(),event.pos.getX(),event.pos.getY(),event.pos.getZ());
        //System.out.println(Minecraft.getMinecraft().thePlayer.getHeldItem().getDisplayName());
    }

    private void deleteClosestWaypoint(String face, int x, int y, int z) {
        if (!face.equals("up")) return;
        int[] playerCoords = new int[] {(int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ};
        ParticleProcessor.ClassificationResult res = processor.getClosestResult(playerCoords);
        if (res == null) return;
        int[] coordsWaypoint = new int[] {res.getCoordinates()[0], res.getCoordinates()[2]};
        int[] coordsBurrowClicked = new int[]{x, z};
        if (!Arrays.equals(coordsWaypoint, coordsBurrowClicked)) return;
        if (Minecraft.getMinecraft().thePlayer.getHeldItem().getDisplayName().contains("Ancestral Spade")) {
            ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
            if (res.getType().equals("EMPTY") || res.getType().equals("TREASURE")) {
                res.setHidden(true);
                exec.schedule(new Runnable() {
                    public void run() {
                        processor.deleteProcessedGroup(res);
                    }
                }, 30000, TimeUnit.MILLISECONDS);

            } else if (res.getType().equals("MOB")) {
                if (res.getState() == 0) res.setState(1);
                else if (res.getState() == 1) {
                    res.setHidden(true);
                    exec.schedule(new Runnable() {
                        public void run() {
                            processor.deleteProcessedGroup(res);
                        }
                    }, 30000, TimeUnit.MILLISECONDS);
                }
            }
        }
    }

    private void autoEquipShovel(String face, int x, int y, int z) {
        if (!face.equals("up")) return;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        int[] playerCoords = new int[] {(int)player.posX, (int)player.posY, (int)player.posZ};
        ParticleProcessor.ClassificationResult res = processor.getClosestResult(playerCoords);
        if (res == null) return;
        int[] coordsBurrowClicked = new int[]{x, y+1, z};
        if (!Arrays.equals(res.getCoordinates(), coordsBurrowClicked)) return;

        //player.inventory.currentItem
    }

    @SubscribeEvent()
    public void onWorldUnload(WorldEvent.Unload event) {
        if (!Configuration.dianaGeneral) return; // Check if the feature is enabled
        processor.clearProcessedGroups();
        if (!Configuration.dianaGaiaConstruct) return; // Check if the feature is enabled
        listGaiaAlive.clear();
    }
}
