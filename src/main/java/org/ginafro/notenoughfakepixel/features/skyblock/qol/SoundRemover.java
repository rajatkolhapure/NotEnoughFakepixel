package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.events.PacketReadEvent;
import org.ginafro.notenoughfakepixel.utils.InventoryUtils;

public class SoundRemover {
    @SubscribeEvent
    public void onSoundPacketReceive(PacketReadEvent event) {
        Packet packet = event.packet;
        if (packet instanceof S29PacketSoundEffect) {
            S29PacketSoundEffect soundEffect = (S29PacketSoundEffect) packet;

            if (soundEffect.getSoundName().equals("mob.villager.yes") || soundEffect.getSoundName().equals("mob.villager.haggle")) {
                if (Configuration.qolDisableJerryChineGunSounds && InventoryUtils.getSlot("Jerry-chine Gun") != -1) {
                    if (event.isCancelable()) event.setCanceled(true);
                }
                return;
            }

            if (soundEffect.getSoundName().equals("mob.endermen.portal")) {
                if (Configuration.qolDisableAoteSounds && InventoryUtils.getSlot("Aspect of the End") != -1) {
                    if (event.isCancelable()) event.setCanceled(true);
                }
                return;
            }

        }
    }
}
