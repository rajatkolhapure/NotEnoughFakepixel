package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

import java.awt.*;

public class StarredMobDisplay {

    // HAVE TO FIX

    @SubscribeEvent
    public void onLoad(TickEvent.WorldTickEvent ev){
        if(!Configuration.starredMobs){
            System.out.println("");
            return;
        }
        if(!ScoreboardUtils.inDungeons) return;
        for(Entity e : ev.world.loadedEntityList){
            if(e.getDisplayName().getUnformattedText().startsWith("✮")){
                BlockPos bp = e.getPosition();
                BlockPos b = new BlockPos(bp.getX()+1,bp.getY()+2,bp.getZ()+1);
                RenderUtils.drawOutlinedBoundingBox(new AxisAlignedBB(bp,b), new Color(0,255,0),2, 1);
            }
        }
    }

}
