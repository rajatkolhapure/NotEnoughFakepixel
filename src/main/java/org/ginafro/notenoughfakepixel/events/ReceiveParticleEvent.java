/*package org.ginafro.notenoughfakepixel.events;

import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ReceiveParticleEvent extends LorenzEvent {
    public final EnumParticleTypes type;
    public final LorenzVec location;
    public final int count;
    public final float speed;
    public final LorenzVec offset;
    public final boolean longDistance;
    public final int[] particleArgs;

    private Double distanceToPlayer;

    public ReceiveParticleEvent(EnumParticleTypes type, LorenzVec location, int count, float speed, LorenzVec offset, boolean longDistance, int[] particleArgs) {
        this.type = type;
        this.location = location;
        this.count = count;
        this.speed = speed;
        this.offset = offset;
        this.longDistance = longDistance;
        this.particleArgs = particleArgs;
    }

    public double getDistanceToPlayer() {
        if (distanceToPlayer == null) {
            distanceToPlayer = location.distanceToPlayer();
        }
        return distanceToPlayer;
    }

    @Override
    public String toString() {
        return "ReceiveParticleEvent(type='" + type + '\'' +
                ", location=" + location.roundTo(1) +
                ", count=" + count +
                ", speed=" + speed +
                ", offset=" + offset.roundTo(1) +
                ", longDistance=" + longDistance +
                ", particleArgs=" + java.util.Arrays.toString(particleArgs) +
                ", distanceToPlayer=" + distanceToPlayer.roundTo(1) +
                ')';
    }
}*/