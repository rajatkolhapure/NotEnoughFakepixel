package org.ginafro.notenoughfakepixel.mixin;

import net.minecraft.world.World;
import org.ginafro.notenoughfakepixel.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class MixinWorld {
    @Inject(method = "spawnParticle(IZDDDDDD[I)V", at = @At("HEAD"), cancellable = true)
    public void spawnParticle(
            int particleID, boolean p_175720_2_, double xCood, double yCoord, double zCoord,
            double xOffset, double yOffset, double zOffset, int[] p_175720_15_, CallbackInfo ci
    ) {
        if (Configuration.disableHyperionExplosions && particleID == 1) {
            ci.cancel();
        }
    }

}
