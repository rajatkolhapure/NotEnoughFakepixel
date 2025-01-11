package org.ginafro.notenoughfakepixel.features.skyblock.diana;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S2APacketParticles;
import org.ginafro.notenoughfakepixel.gui.impl.Waypoint;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;

public class ParticleProcessor {

    private final Queue<S2APacketParticles> particleQueue = new ConcurrentLinkedQueue<>();
    private final Set<ClassificationResult> processedGroups = new HashSet<>(); // To track already classified groups

    public void addParticle(S2APacketParticles particle) {
        particleQueue.add(particle);
        /*for(S2APacketParticles p : particleQueue) {
            System.out.println(p.getParticleType().getParticleName());
        }*/
        //System.out.println(processedGroups.toString());
        if (particleQueue.size() > 1) {
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
                    ClassificationResult result = classifyGroup(currentGroup);
                    if (result != null && !isDuplicate(result)) {
                        results.add(result);
                        markAsProcessed(result);
                        //Waypoint waypoint = new Waypoint(result.getCoordinates()[0], result.getCoordinates()[1], result.getCoordinates()[2], Minecraft.getMinecraft().theWorld);


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
        return distance < 4.0;
    }

    private ClassificationResult classifyGroup(List<S2APacketParticles> group) {
        Set<String> groupTypes = new HashSet<>();
        double sumX = 0, sumY = 0, sumZ = 0;

        for (S2APacketParticles particle : group) {
            groupTypes.add(particle.getParticleType().getParticleName());
            sumX += particle.getXCoordinate();
            sumY += particle.getYCoordinate();
            sumZ += particle.getZCoordinate();
        }

        int size = group.size();
        double avgX = sumX / size;
        double avgY = sumY / size;
        double avgZ = sumZ / size;

        int[] roundedCoordinates = new int[]{
                (int) Math.round(avgX),
                (int) Math.round(avgY),
                (int) Math.round(avgZ)
        };

        if (groupTypes.contains("magicCrit") && groupTypes.contains("enchantmenttable") && groupTypes.contains("footstep")) {
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
        return distance < 2.0; // Adjust threshold as needed
    }

    private void markAsProcessed(ClassificationResult result) {
        processedGroups.add(result);
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

