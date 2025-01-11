package org.ginafro.notenoughfakepixel.features.skyblock.diana;

import net.minecraft.util.Vector3d;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

import java.util.ArrayList;
import java.util.List;

public class GuessBurrow {

    int dingIndex = 0;
    boolean hasDinged = false;
    Float firstPitch = 0f;
    Float lastDingPitch = 0f;
    Vector3d lastParticlePoint = null;
    Vector3d lastParticlePoint2 = null;
    Vector3d firstParticlePoint = null;
    Vector3d particlePoint = null;
    Vector3d guessPoint = null;
    Vector3d lastSoundPoint = null;
    ArrayList<Vector3d> locations = new ArrayList<>();
    ArrayList<Float> dingSlope = new ArrayList<>();
    Float distance = null;
    Float distance2 = null;

    @SubscribeEvent
    public void onHarpSound(PlaySoundAtEntityEvent event) {
        if (!Configuration.autoDropItems) return; // Check if the feature is enabled
        if (!ScoreboardUtils.currentLocation.isHub()) return; // Check if the player is in a dungeon
        if (!event.name.equals("note.harp")) return;
        //event.entity.chunkCoordX
        float pitch = event.pitch;
        if (!hasDinged) {
            firstPitch = pitch;
        }
        hasDinged = true;
        if (pitch < lastDingPitch) {
            firstPitch = pitch;
            dingIndex = 0;
            dingSlope.clear();
            lastDingPitch = pitch;
            lastParticlePoint = null;
            lastParticlePoint2 = null;
            lastSoundPoint = null;
            firstParticlePoint = null;
            distance = null;
            locations.clear();
        }
        if (lastDingPitch == 0f) {
            lastDingPitch = pitch;
            distance = null;
            lastParticlePoint = null;
            lastParticlePoint2 = null;
            lastSoundPoint = null;
            firstParticlePoint = null;
            locations.clear();
            return;
        }
        dingIndex++;

        if (dingIndex > 1) {
            dingSlope.add(pitch - lastDingPitch);
        }

        if (dingSlope.size() > 20) {
            dingSlope.remove(0);
        }

        Float slope = dingSlope.isEmpty() ? 0
                : dingSlope.stream().reduce(0f, Float::sum) / dingSlope.size();

        Vector3d pos = new Vector3d();
        pos.x = event.entity.posX;
        pos.y = event.entity.posY;
        pos.z = event.entity.posZ;
        lastSoundPoint = pos;
        lastDingPitch = pitch;

        if (lastParticlePoint2 == null || particlePoint == null || firstParticlePoint == null) {
            return;
        }

        distance2 = (float)((Math.E / slope) - distance(pos, firstParticlePoint));

        if (distance2 > 1000) {
            //ChatUtils.debug("Soopy distance2 is " + distance2);
            distance2 = 0f;
            guessPoint = null;

            // workaround: returning if the distance is too big
            return;
        }

        double lineDist = distance(lastParticlePoint2, particlePoint);

        distance = distance2;
        Vector3d changesHelp = subtract(particlePoint, lastParticlePoint2);
        List<Double> changes = new ArrayList<>();
        changes.add(changesHelp.x / lineDist);
        changes.add(changesHelp.y / lineDist);
        changes.add(changesHelp.z / lineDist);

        if (lastSoundPoint != null) {
            guessPoint = new Vector3d();
            guessPoint.x = lastSoundPoint.x + changes.get(0) * distance;
            guessPoint.y = lastSoundPoint.y + changes.get(1) * distance;
            guessPoint.z = lastSoundPoint.z + changes.get(2) * distance;
        }

    }

    public static double distance(Vector3d d1, Vector3d d2) {
        double dx = d2.x - d1.x;
        double dy = d2.y - d1.y;
        double dz = d2.z - d1.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static Vector3d subtract(Vector3d d1, Vector3d d2) {
        Vector3d v = new Vector3d();
        v.x = d1.x - d2.x;
        v.y = d1.y - d2.y;
        v.z = d1.z - d2.z;
        return v;
    }
}
