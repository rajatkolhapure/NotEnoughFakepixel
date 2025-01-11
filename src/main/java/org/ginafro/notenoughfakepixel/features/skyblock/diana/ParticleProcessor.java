package org.ginafro.notenoughfakepixel.features.skyblock.diana;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S2APacketParticles;
import org.ginafro.notenoughfakepixel.gui.impl.Waypoint;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;

public class ParticleProcessor {

    private final float distanceThreshold = 4.0f;
    private final Queue<S2APacketParticles> particleQueue = new ConcurrentLinkedQueue<>();
    private final Set<ClassificationResult> processedGroups = new HashSet<>(); // To track already classified groups
    private int delaySWaypointRemove = 20;

    public void addParticle(S2APacketParticles particle) {
        particleQueue.add(particle);
        /*for(S2APacketParticles p : particleQueue) {
            System.out.println(p.getParticleType().getParticleName());
        }*/
        //System.out.println(processedGroups.toString());
        if (particleQueue.size() > 30) {
            // Procesar partículas acumuladas
            List<ParticleProcessor.ClassificationResult> results = processParticles();
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

            if (previousParticle != null && isClose(previousParticle, currentParticle)) {
                currentGroup.add(currentParticle);
            } else {
                if (!currentGroup.isEmpty()) {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    for (S2APacketParticles p : currentGroup) {
                        System.out.println(p);
                    }
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    ClassificationResult result = classifyGroup(currentGroup);
                    if (result != null && !isDuplicate(result)) {
                        results.add(result);
                        markAsProcessed(result);
                    }
                }
                currentGroup.clear();
                currentGroup.add(currentParticle);
            }
            previousParticle = currentParticle;
        }

        // Process the last group
        if (!currentGroup.isEmpty()) {
            ClassificationResult result = classifyGroup(currentGroup);
            if (result != null && !isDuplicate(result)) {
                results.add(result);
                markAsProcessed(result);
                //Waypoint waypoint = new Waypoint(result.getCoordinates()[0], result.getCoordinates()[1], result.getCoordinates()[2], Minecraft.getMinecraft().theWorld);
            }
        }

        return results;
    }

    private boolean isClose(S2APacketParticles p1, S2APacketParticles p2) {
        double distance = Math.sqrt(
                Math.pow(p1.getXCoordinate() - p2.getXCoordinate(), 2) +
                        Math.pow(p1.getYCoordinate() - p2.getYCoordinate(), 2) +
                        Math.pow(p1.getZCoordinate() - p2.getZCoordinate(), 2)
        );
        return distance < distanceThreshold;
    }

    private ClassificationResult classifyGroup(List<S2APacketParticles> group) {
        Set<String> groupTypes = new HashSet<>();
        double sumX = 0, sumY = 0, sumZ = 0;
        int cont = 0;
        for (S2APacketParticles particle : group) {
            groupTypes.add(particle.getParticleType().getParticleName());
            if (particle.getParticleType().getParticleName().equals("enchantmenttable")) {
                cont++;
                sumX += particle.getXCoordinate();
                sumY += particle.getYCoordinate();
                sumZ += particle.getZCoordinate();
            }
        }
        double avgX = sumX / cont;
        double avgY = sumY / cont;
        double avgZ = sumZ / cont;

        int[] roundedCoordinates = new int[]{
                (int) Math.round(avgX),
                (int) Math.round(avgY),
                (int) Math.round(avgZ)
        };

        if (groupTypes.contains("magicCrit") && groupTypes.contains("enchantmenttable")) {
            return new ClassificationResult("EMPTY", roundedCoordinates);
        }
        if (groupTypes.contains("crit") && groupTypes.contains("enchantmenttable")) {
            return new ClassificationResult("MOB", roundedCoordinates);
        }
        if (groupTypes.contains("dripLava") && groupTypes.contains("enchantmenttable")) {
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

    private boolean isDuplicate(ClassificationResult result) {
        for (ClassificationResult processedResult : processedGroups) {
            if (areCoordinatesClose(processedResult.getCoordinates(), result.getCoordinates())) {
                return true;
            }
        }
        return false;
    }

    private boolean areCoordinatesClose(int[] coords1, int[] coords2) {
        double distance = Math.sqrt(
                Math.pow(coords1[0] - coords2[0], 2) +
                        Math.pow(coords1[1] - coords2[1], 2) +
                        Math.pow(coords1[2] - coords2[2], 2)
        );
        return distance < distanceThreshold; // Adjust threshold as needed
    }

    private void markAsProcessed(ClassificationResult result) {
        processedGroups.add(result);
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

    public static class ClassificationResult {
        private final String type;
        private final int[] coordinates;

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

        public String getUniqueKey() {
            return type + ":" + coordinates[0] + "," + coordinates[1] + "," + coordinates[2];
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

