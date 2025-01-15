package org.ginafro.notenoughfakepixel.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import org.ginafro.notenoughfakepixel.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityPlayerSP.class)
public class EntityPlayerSPMixinNausea extends AbstractClientPlayer {

    public EntityPlayerSPMixinNausea(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }
}
