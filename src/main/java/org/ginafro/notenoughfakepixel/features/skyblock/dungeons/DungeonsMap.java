package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;


import cc.polyfrost.oneconfig.events.event.HudRenderEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.MapData;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

public class DungeonsMap {

    @Subscribe
    public void onRender(HudRenderEvent e){
        if(Configuration.dungeonsMap){
            if(ScoreboardUtils.currentLocation.isDungeon()){
                ItemStack map = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(8);
                if(map == null && map.getItem() == null) return;
                if(map.getItem() instanceof ItemMap){
                    ItemMap map1 = (ItemMap) map.getItem();
                    MapData data = map1.getMapData(map , Minecraft.getMinecraft().theWorld);
                    if (data != null) {
                        GlStateManager.pushMatrix();
                        drawMap(data,false,Configuration.dungeonMapScale);
                        GlStateManager.popMatrix();
                    }

                }
            }
        }
    }



    private void drawMap(MapData data, boolean b, float scale) {
        GlStateManager.scale(scale,scale,scale);
        Minecraft.getMinecraft().entityRenderer.getMapItemRenderer().renderMap(data , b);
    }

}
