package org.ginafro.notenoughfakepixel.features.skyblock.diana;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.gui.impl.Waypoint;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;

public class ParticleProcessor {

    private final float distanceThreshold = 3.0f; // Distance (in blocks) to distinguish if its same burrow
    private final int particleThreshold = 6; // Number of particles needed to be considered as a burrow
    private int delaySWaypointRemove = 60; // Time to remove a waypoint after generated
    private int windowQueue = 6; // Process this number of particles each time
    private final Queue<S2APacketParticles> particleQueue = new ConcurrentLinkedQueue<>(); // Particle concurrent queue
    private final Set<ClassificationResult> processedGroups = new HashSet<>(); // To track already classified groups
    private SoundManager soundManager = new SoundManager();

    public void addParticle(S2APacketParticles particle) {
        if (!(particle.getParticleType().getParticleName().equals("magicCrit")
                || particle.getParticleType().getParticleName().equals("enchantmenttable")
                || particle.getParticleType().getParticleName().equals("crit")
                || particle.getParticleType().getParticleName().equals("dripLava"))) return;
        int[] playerCoords = new int[] {(int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ};
        ParticleProcessor.ClassificationResult closestResult = getClosestResult(playerCoords);
        if (closestResult != null) {
            int[] particleCoords = new int[]{(int) Math.floor(particle.getXCoordinate()), (int) Math.floor(particle.getYCoordinate()), (int) Math.floor(particle.getZCoordinate())};
            if (areCoordinatesClose(closestResult.getCoordinates(), particleCoords, distanceThreshold)) return;
        }
        particleQueue.add(particle);
        /*for(S2APacketParticles p : particleQueue) {
            System.out.println(p.getParticleType().getParticleName());
        }*/
        //System.out.println(processedGroups.toString());
        if (particleQueue.size() > windowQueue) {
            /*System.out.println("\n\n\nQueue: (" + particleQueue.size()+")\n");
            for(S2APacketParticles p : particleQueue) {
                System.out.println(p.getParticleType().getParticleName());
                System.out.println(p.getXCoordinate() + ", " + p.getYCoordinate() + ", " + p.getZCoordinate());
            }*/
            // Procesar partículas acumuladas
            processParticles();
            /*for (ParticleProcessor.ClassificationResult result : results) {
                System.out.println(Arrays.toString(result.getCoordinates()));
            }*/
        }
    }

    public List<ClassificationResult> processParticles() {
        List<ClassificationResult> results = new ArrayList<>();

        List<S2APacketParticles> currentGroup = new ArrayList<>();
        S2APacketParticles previousParticle = null;

        while (!particleQueue.isEmpty()) {
            S2APacketParticles currentParticle = particleQueue.poll();

            if (previousParticle != null
                    && !(previousParticle.getParticleType().getParticleName().equals("dripLava")
                    && currentParticle.getParticleType().getParticleName().equals("dripLava"))
                    && isClose(previousParticle, currentParticle, particleThreshold)) {
                currentGroup.add(currentParticle);
            } else {
                if (!currentGroup.isEmpty() && currentGroup.size() > particleThreshold) {
                    /*System.out.println("\n\n\nGroup formed\n");
                    for (S2APacketParticles p : currentGroup) {
                        System.out.println(p.getParticleType().getParticleName() + "  " + p.getXCoordinate() + "," + p.getYCoordinate() + "," + p.getZCoordinate());
                    }*/
                    ClassificationResult result = classifyGroup(currentGroup);
                    if (result != null && !isDuplicateResult(result)) {
                        BlockPos block = new BlockPos(result.getCoordinates()[0], result.getCoordinates()[1]-1, result.getCoordinates()[2]);
                        System.out.println(block.getX() + ", " + block.getY() + ", " + block.getZ());
                        System.out.println(Minecraft.getMinecraft().theWorld.isAirBlock(block));
                        if (Minecraft.getMinecraft().theWorld.isAirBlock(block)) return null;
                        results.add(result);
                        markAsProcessed(result);
                        //System.out.println("\n\n\nNew result\n");
                        /*for (ClassificationResult c : results) {
                            System.out.println(c.getType() + ", " + c.getCoordinates()[0] + ", " + c.getCoordinates()[1] + ", " + c.getCoordinates()[2]);
                        }*/
                    }
                }
                currentGroup.clear();
                currentGroup.add(currentParticle);
            }
            previousParticle = currentParticle;
        }

        // Process the last group
        if (!currentGroup.isEmpty() && currentGroup.size() > particleThreshold) {
            ClassificationResult result = classifyGroup(currentGroup);
            if (result != null && !isDuplicateResult(result)) {
                BlockPos block = new BlockPos(result.getCoordinates()[0], result.getCoordinates()[1]-1, result.getCoordinates()[2]);
                //System.out.println(block.getX() + ", " + block.getY() + ", " + block.getZ());
                //System.out.println(Minecraft.getMinecraft().theWorld.isAirBlock(block));
                if (Minecraft.getMinecraft().theWorld.isAirBlock(block)) return null;
                results.add(result);
                markAsProcessed(result);
                //System.out.println(result.getType()+" BURROW WAYPOINT ADDED IN "+result.getCoordinates()[0] + ", " + result.getCoordinates()[1] + ", " + result.getCoordinates()[2]);
                /*System.out.println("\n\n\nLast group formed\n");
                for (S2APacketParticles p : currentGroup) {
                    System.out.println(p.getParticleType().getParticleName() + "  " + p.getXCoordinate() + "," + p.getYCoordinate() + "," + p.getZCoordinate());
                }*/
                //Waypoint waypoint = new Waypoint(result.getCoordinates()[0], result.getCoordinates()[1], result.getCoordinates()[2], Minecraft.getMinecraft().theWorld);
            }
        }

        return results;
    }

    private boolean isClose(S2APacketParticles p1, S2APacketParticles p2, float threshold) {
        double distance = Math.sqrt(
                Math.pow(p1.getXCoordinate() - p2.getXCoordinate(), 2) +
                        Math.pow(p1.getYCoordinate() - p2.getYCoordinate(), 2) +
                        Math.pow(p1.getZCoordinate() - p2.getZCoordinate(), 2)
        );
        return distance < threshold;
    }

    private ClassificationResult classifyGroup(List<S2APacketParticles> group) {
        Set<String> groupTypes = new HashSet<>();
        double sumX = 0, sumY = 0, sumZ = 0;
        int cont = 0;
        //double minX = 500, minY = 500, minZ = 500;
        //double maxX = -500, maxY = 0, maxZ = -500;
        //System.out.println("\n\n\nClassifying group\n");
        for (S2APacketParticles particle : group) {
            //System.out.println(particle.getParticleType().getParticleName() + "  " + particle.getXCoordinate() + "," + particle.getYCoordinate() + "," + particle.getZCoordinate());
            groupTypes.add(particle.getParticleType().getParticleName());
            if (!particle.getParticleType().getParticleName().equals("dripLava")) {
                sumX += particle.getXCoordinate();
                sumY += particle.getYCoordinate();
                sumZ += particle.getZCoordinate();
                cont++;
                /*if (particle.getXCoordinate() < minX) {
                    minX = particle.getXCoordinate();
                }
                if (particle.getYCoordinate() < minY) {
                    minY = particle.getYCoordinate();
                }
                if (particle.getZCoordinate() < minZ) {
                    minZ = particle.getZCoordinate();
                }
                if (particle.getXCoordinate() > maxX) {
                    maxX = particle.getXCoordinate();
                }
                if (particle.getYCoordinate() > maxY) {
                    maxY = particle.getYCoordinate();
                }
                if (particle.getZCoordinate() > maxZ) {
                    maxZ = particle.getZCoordinate();
                }*/
            }
            /*sumX += particle.getXCoordinate();
            sumY += particle.getYCoordinate();
            sumZ += particle.getZCoordinate();*/
        }
        /*int size = group.size();
        double avgX = sumX / size;
        double avgY = sumY / size;
        double avgZ = sumZ / size;*/
        /*double avgX = (minX+maxX) / 2;
        double avgY = (minY+maxY) / 2;
        double avgZ = (minZ+maxZ) / 2;*/
        double avgX = sumX / cont;
        double avgY = sumY / cont;
        double avgZ = sumZ / cont;

        int[] roundedCoordinates = new int[]{
                (int) Math.floor(avgX),
                (int) Math.floor(avgY),
                (int) Math.floor(avgZ)
        };

        if (groupTypes.contains("magicCrit") && groupTypes.contains("enchantmenttable")) {
            //System.out.println("EMPTY BURROW WAYPOINT ADDED IN "+roundedCoordinates[0] + ", " + roundedCoordinates[1] + ", " + roundedCoordinates[2]);
            return new ClassificationResult("EMPTY", roundedCoordinates);
        }
        if (groupTypes.contains("crit") && groupTypes.contains("enchantmenttable")) {
            //System.out.println("MOB WAYPOINT ADDED IN "+roundedCoordinates[0] + ", " + roundedCoordinates[1] + ", " + roundedCoordinates[2]);
            return new ClassificationResult("MOB", roundedCoordinates);
        }
        if (groupTypes.contains("dripLava") && groupTypes.contains("enchantmenttable")) {
            //System.out.println("TREASURE BURROW WAYPOINT ADDED IN "+roundedCoordinates[0] + ", " + roundedCoordinates[1] + ", " + roundedCoordinates[2]);
            return new ClassificationResult("TREASURE", roundedCoordinates);
        }
        /* Segun un mod de ref -> no funciona esto
        S2APacketParticles first = group.get(0);
        if (first.getParticleType().getParticleName().equals("magicCrit") && first.getParticleCount() == 4 && first.getParticleSpeed() == 0.01f && first.getXOffset() == 0.5f && first.getYOffset() == 0.1f && first.getZOffset() == 0.5f) {
            return new ClassificationResult("EMPTY", roundedCoordinates);
        } else if (first.getParticleType().getParticleName().equals("crit") && first.getParticleCount() == 3 && first.getParticleSpeed() == 0.01f && first.getXOffset() == 0.5f && first.getYOffset() == 0.1f && first.getZOffset() == 0.5f) {
            return new ClassificationResult("MOB", roundedCoordinates);
        } else if (first.getParticleType().getParticleName().equals("dripLava") && first.getParticleCount() == 2 && first.getParticleSpeed() == 0.01f && first.getXOffset() == 0.35f && first.getYOffset() == 0.1f && first.getZOffset() == 0.35f) {
            return new ClassificationResult("TREASURE", roundedCoordinates);
        }*/

        return null;
    }

    private boolean isDuplicateResult(ClassificationResult result) {
        for (ClassificationResult processedResult : processedGroups) {
            if (areCoordinatesClose(processedResult.getCoordinates(), result.getCoordinates(), distanceThreshold)) {
                return true;
            }
        }
        return false;
    }

    public float getDistance(int[] coords1, int[] coords2) {
        return (float) Math.sqrt(
                Math.pow(coords1[0] - coords2[0], 2) +
                        Math.pow(coords1[1] - coords2[1], 2) +
                        Math.pow(coords1[2] - coords2[2], 2)
        );
    }

    public boolean areCoordinatesClose(int[] coords1, int[] coords2, float threshold) {
        double distance = getDistance(coords1, coords2);
        return distance < threshold; // Adjust threshold as needed
    }

    private void markAsProcessed(ClassificationResult result) {
        processedGroups.add(result);
        if (Configuration.dianaSounds) {
            if (result.getType().equals("EMPTY")) {
                soundManager.playWaypointSound(result.getCoordinates());
            } else if (result.getType().equals("MOB")) {
                soundManager.playWaypointSound(result.getCoordinates());
            } else if (result.getType().equals("TREASURE")) {
                soundManager.playTreasureSound(result.getCoordinates());
            }
        }
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.schedule(new Runnable() {
            public void run() {
                processedGroups.remove(result);
            }
        }, delaySWaypointRemove, TimeUnit.SECONDS);
    }

    public Set<ClassificationResult> getProcessedGroups() {
        return processedGroups;
    }

    public void deleteProcessedGroup(ClassificationResult result) {
        processedGroups.remove(result);
    }

    public void clearProcessedGroups() {
        processedGroups.clear();
    }

    public ClassificationResult getClosestResult(int[] coords) {
        ClassificationResult result = null;
        float distance = 9999;
        if (processedGroups.isEmpty()) return null;
        for (ClassificationResult res : processedGroups) {
            float dist = getDistance(coords, res.getCoordinates());
            if (dist < distance) {
                distance = dist;
                result = res;
            }
        }
        return result;
    }

    public float getDistanceThreshold() {
        return distanceThreshold;
    }

    public static class ClassificationResult {
        private final String type;
        private final int[] coordinates;
        private int state = 0;
        private boolean hidden = false;

        public ClassificationResult(String type, int[] coordinates) {
            this.type = type;
            this.coordinates = coordinates;
        }

        public String getType() {
            return type;
        }

        public int[] getCoordinates() {
            return coordinates;
        }

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }

        public String getUniqueKey() {
            return type + ":" + coordinates[0] + "," + coordinates[1] + "," + coordinates[2];
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return String.format(
                    "ClassificationResult{type='%s', coordinates=(%d, %d, %d)}",
                    type, coordinates[0], coordinates[1], coordinates[2]
            );
        }
    }
}

