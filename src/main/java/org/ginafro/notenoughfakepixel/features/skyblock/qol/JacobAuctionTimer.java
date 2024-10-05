package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.variables.Gamemode;

import java.util.Date;

public class JacobAuctionTimer {

    public boolean jacob , auction;

    public void onTick(TickEvent.ServerTickEvent e){
        if(Configuration.jATimer){
            if(NotEnoughFakepixel.currentGamemode == Gamemode.SKYBLOCK){

            }
        }
    }

}
