package org.ginafro.notenoughfakepixel.mixin;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.IChatComponent;
import org.ginafro.notenoughfakepixel.features.skyblock.qol.DamageCommas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RendererLivingEntity.class)
public class MixinRendererLivingEntity {
    @Redirect(method = "renderName*", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/entity/EntityLivingBase;getDisplayName()Lnet/minecraft/util/IChatComponent;"))
    public IChatComponent renderName_getDisplayName(EntityLivingBase entity) {
        if (entity instanceof EntityArmorStand) {
            return DamageCommas.replaceName(entity);
        } else {
            return entity.getDisplayName();
        }
    }

}
