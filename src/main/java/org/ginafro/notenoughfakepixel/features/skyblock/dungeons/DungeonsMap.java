package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;


import cc.polyfrost.oneconfig.events.event.HudRenderEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec4b;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.InventoryUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import net.minecraft.client.gui.MapItemRenderer;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;

public class DungeonsMap {

    Tessellator tessellator;
    WorldRenderer worldrenderer;
    private static float playerMarkerScale = 1.4F;
    private static float othersMarkerScale = 1.25F;
    double playerPositionX, playerPositionY = 0;
    ArrayList<String> teammates = new ArrayList<>();
    boolean firstTime = true;
    private static final Color[] colors = {Color.YELLOW, Color.BLUE, Color.RED, Color.ORANGE};
    private ResourceLocation mapIconsTexture = new ResourceLocation("textures/map/map_icons.png");
    Minecraft mc = Minecraft.getMinecraft();

    float mapBorderWidth = 2.0F;
    public DungeonsMap() {
        //marker = new DungeonsMapMarker(mc.thePlayer);
        tessellator = Tessellator.getInstance();
        worldrenderer = tessellator.getWorldRenderer();
    }

    @Subscribe
    public void onRender(HudRenderEvent e){
        if(!Configuration.dungeonsMap) return;
        if(!ScoreboardUtils.currentLocation.isDungeon()) return;
        ItemStack map = mc.thePlayer.inventory.getStackInSlot(8);
        if(map == null && map.getItem() == null) return;
        if(map.getItem() instanceof ItemMap){
            ItemMap map1 = (ItemMap) map.getItem();
            MapData data = map1.getMapData(map , mc.theWorld);
            if (data != null) {
                if (firstTime) {
                    firstTime = false;
                    teammates = getTeammates();
                }
                drawMap(data);
                drawBorderMap();
                drawMarkers(data.mapDecorations);
            }
        }
    }

    private void drawMap(MapData data) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(Configuration.dungeonsMapScale, Configuration.dungeonsMapScale, Configuration.dungeonsMapScale);

        // Define the scissor area based on known map position
        float x1 = (Configuration.dungeonsMapOffsetX) * Configuration.dungeonsMapScale;
        float y1 = (Configuration.dungeonsMapOffsetY) * Configuration.dungeonsMapScale;
        float x2 = x1 + (128 * Configuration.dungeonsMapScale);
        float y2 = y1 + (128 * Configuration.dungeonsMapScale);

        // Convert coordinates for OpenGL's bottom-left origin
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaleFactor = scaledResolution.getScaleFactor();

        int scissorX = (int) (x1 * scaleFactor);
        int scissorY = (int) (y1 * scaleFactor);
        int scissorWidth = (int) ((x2 - x1) * scaleFactor);
        int scissorHeight = (int) ((y2 - y1) * scaleFactor);

        // Enable scissor test (this will clip everything *outside* the 128x128 area)
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(scissorX, mc.displayHeight - (scissorY + scissorHeight), scissorWidth, scissorHeight);

        // Move map to the correct screen position
        GlStateManager.translate(Configuration.dungeonsMapOffsetX, Configuration.dungeonsMapOffsetY, 0);
        if (Configuration.dungeonsRotateMap) {
            float angle = -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
            GlStateManager.translate(64, 64, 0);
            GlStateManager.rotate(angle + 180, 0, 0, 1);
            GlStateManager.translate(-64, -64, 0);
            float translateX = 64.0F - (float) playerPositionX;
            float translateY = 64.0F - (float) playerPositionY;
            GlStateManager.translate(translateX, translateY, 0);
        }

        // Render the map (clipped to scissor area)
        mc.entityRenderer.getMapItemRenderer().renderMap(data, false);

        // Disable scissor test after rendering
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
    }

    private void drawBorderMap() {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D(); // Disable texture to draw plain colors
        GlStateManager.color(Configuration.dungeonsMapBorderColor.getRed()/255f, Configuration.dungeonsMapBorderColor.getGreen()/255f, Configuration.dungeonsMapBorderColor.getBlue()/255f, 1.0F); // Set border color (white)
        GL11.glLineWidth(mapBorderWidth); // Border thickness

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);

        float x1 = (Configuration.dungeonsMapOffsetX) * Configuration.dungeonsMapScale;
        float y1 = (Configuration.dungeonsMapOffsetY) * Configuration.dungeonsMapScale;
        float x2 = x1 + (128 * Configuration.dungeonsMapScale);
        float y2 = y1 + (128 * Configuration.dungeonsMapScale);

        worldrenderer.pos(x1, y1, 0.0D).endVertex(); // Top-left
        worldrenderer.pos(x2, y1, 0.0D).endVertex(); // Top-right
        worldrenderer.pos(x2, y2, 0.0D).endVertex(); // Bottom-right
        worldrenderer.pos(x1, y2, 0.0D).endVertex(); // Bottom-left

        tessellator.draw();

        GlStateManager.color(1.0F,1.0F,1.0F, 1.0F); // Set border color (white)
        GlStateManager.enableTexture2D(); // Re-enable textures
        GlStateManager.popMatrix();
    }



    private void drawMarkers(Map<String, Vec4b> mapDecorations) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaleFactor = scaledResolution.getScaleFactor();

        // Define scissor region based on configurations
        float x1 = (Configuration.dungeonsMapOffsetX) * Configuration.dungeonsMapScale;
        float y1 = (Configuration.dungeonsMapOffsetY) * Configuration.dungeonsMapScale;
        float x2 = x1 + (128 * Configuration.dungeonsMapScale);
        float y2 = y1 + (128 * Configuration.dungeonsMapScale);

        int scissorX = (int) (x1 * scaleFactor);
        int scissorY = (int) (y1 * scaleFactor);
        int scissorWidth = (int) ((x2 - x1) * scaleFactor);
        int scissorHeight = (int) ((y2 - y1) * scaleFactor);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(scissorX, mc.displayHeight - (scissorY + scissorHeight), scissorWidth, scissorHeight);

        int colorIndex = 0;
        for (Map.Entry<String, Vec4b> entry : mapDecorations.entrySet()) {
            GlStateManager.pushMatrix();
            byte iconType = entry.getValue().func_176110_a();
            if (iconType == 3) iconType = 0; // Convertir en marcador blanco para aplicar color

            double markerX = entry.getValue().func_176112_b() / 2.0F + 64.0F;
            double markerY = entry.getValue().func_176113_c() / 2.0F + 64.0F;
            float playerAngle = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);

            if (iconType == 1) {
                playerPositionX = markerX;
                playerPositionY = markerY;
            }

            if (!Configuration.dungeonsRotateMap) {
                GlStateManager.translate((Configuration.dungeonsMapOffsetX + markerX) * Configuration.dungeonsMapScale,
                        (Configuration.dungeonsMapOffsetY + markerY) * Configuration.dungeonsMapScale,
                        0.0);
            } else if (iconType == 1) {
                GlStateManager.translate((Configuration.dungeonsMapOffsetX + 64.0F) * Configuration.dungeonsMapScale,
                        (Configuration.dungeonsMapOffsetY + 64.0F) * Configuration.dungeonsMapScale,
                        0.0);
            } else if (iconType == 0) {
                float relativeX = (float) (markerX - 64);
                float relativeY = (float) (markerY - 64);
                relativeX += 64.0F - (float) playerPositionX;
                relativeY += 64.0F - (float) playerPositionY;
                float angleRad = (float) Math.toRadians(-MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw));
                float rotatedX = (float) (relativeX * Math.cos(angleRad) - relativeY * Math.sin(angleRad));
                float rotatedY = (float) (relativeX * Math.sin(angleRad) + relativeY * Math.cos(angleRad));

                GlStateManager.translate((Configuration.dungeonsMapOffsetX + 64.0F - rotatedX) * Configuration.dungeonsMapScale,
                        (Configuration.dungeonsMapOffsetY + 64.0F - rotatedY) * Configuration.dungeonsMapScale,
                        0.0);
            }

            float angle = 180F;
            if (!Configuration.dungeonsRotateMap && iconType == 1) angle = playerAngle;
            if (iconType == 0) angle = (float) (entry.getValue().func_176111_d() * 360) / 16.0F;
            if (Configuration.dungeonsRotateMap && iconType == 0) angle = angle + 180 - playerAngle;

            GlStateManager.rotate(angle, 0.0F, 0.0F, 1.0F);
            if (iconType == 1) GlStateManager.scale(Configuration.dungeonsMapScale * 4 * playerMarkerScale,
                    Configuration.dungeonsMapScale * 4 * playerMarkerScale,
                    3.0F * playerMarkerScale);
            if (iconType == 0) GlStateManager.scale(Configuration.dungeonsMapScale * 4 * othersMarkerScale,
                    Configuration.dungeonsMapScale * 4 * othersMarkerScale,
                    3.0F * othersMarkerScale);

            float g = (float) (iconType % 4) / 4.0F;
            float h = (float) (iconType / 4) / 4.0F;
            float l = (float) (iconType % 4 + 1) / 4.0F;
            float m = (float) (iconType / 4 + 1) / 4.0F;

            GlStateManager.translate(-0.125F, 0.125F, 0.0F);
            mc.getTextureManager().bindTexture(mapIconsTexture);

            if (iconType == 0) {
                switch (colorIndex) {
                    case 0: GlStateManager.color(0.0F, 0.0F, 1.0F, 1.0F); break; // Azul
                    case 1: GlStateManager.color(1.0F, 1.0F, 0.0F, 1.0F); break; // Amarillo
                    case 2: GlStateManager.color(1.0F, 0.5F, 0.0F, 1.0F); break; // Naranja
                    case 3: GlStateManager.color(1.0F, 0.0F, 0.0F, 1.0F); break; // Rojo
                }
                colorIndex = (colorIndex + 1) % (mapDecorations.size() - 1);
            }

            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            float eachDecorationZOffset = -0.001F;
            worldrenderer.pos(-1.0D, 1.0D, eachDecorationZOffset).tex(g, h).endVertex();
            worldrenderer.pos(1.0D, 1.0D, eachDecorationZOffset).tex(l, h).endVertex();
            worldrenderer.pos(1.0D, -1.0D, eachDecorationZOffset).tex(l, m).endVertex();
            worldrenderer.pos(-1.0D, -1.0D, eachDecorationZOffset).tex(g, m).endVertex();
            tessellator.draw();

            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.popMatrix();
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }


    private ArrayList<String> getTeammates() {
        ArrayList<String> teammates = new ArrayList<>();
        // Grab all the world player entities and try to correlate them to the map
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (!Objects.equals(player.getName(), mc.thePlayer.getName()) &&
                    !Objects.equals(player.getName(), "§e§lCLICK") &&
                    !Objects.equals(player.getName(), "?")) teammates.add(player.getName());
        }
        Collections.sort(teammates);
        System.out.println(teammates);
        return teammates;
    }
}
