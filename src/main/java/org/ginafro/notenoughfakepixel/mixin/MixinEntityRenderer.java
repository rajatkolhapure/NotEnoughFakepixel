package org.ginafro.notenoughfakepixel.mixin;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import org.ginafro.notenoughfakepixel.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer implements IResourceManagerReloadListener {

    @Inject(method = "hurtCameraEffect" , at = @At("HEAD") , cancellable = true)
    private void onHurtCam(float partialTicks, CallbackInfo ci){
        if (Configuration.noHurtCam) ci.cancel();
    }

    @Inject(method = "addRainParticles", at = @At("HEAD"), cancellable = true)
    private void disableRainRendering(CallbackInfo ci) {
        if (Configuration.disableRain) { ci.cancel(); }
    }

    @Inject(method = "updateCameraAndRender", at = @At("HEAD"), cancellable = true)
    private void disablePortalEffect(float partialTicks, long nanoTime, CallbackInfo ci) {
        if (Configuration.noNausea) {
            ci.cancel();
        }
    }

}
