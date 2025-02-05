package org.ginafro.notenoughfakepixel.utils;

import cc.polyfrost.oneconfig.libs.universal.UResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Slot;
import net.minecraft.util.*;
import org.ginafro.notenoughfakepixel.variables.MobDisplayTypes;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.awt.Color;

public class RenderUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void drawOnSlot(int size, int xSlotPos, int ySlotPos, int colour) {
        drawOnSlot(size, xSlotPos, ySlotPos, colour, -1);
    }

    public static void drawOnSlot(int size, int xSlotPos, int ySlotPos, int colour, int number) {
        int guiLeft = (UResolution.getScaledWidth() - 176) / 2;
        int guiTop = (UResolution.getScaledHeight() - 222) / 2;
        int x = guiLeft + xSlotPos;
        int y = guiTop + ySlotPos;

        // Move down when chest isn't 6 rows
        if (size != 90) y += (6 - (size - 36) / 9) * 9;

        GL11.glTranslated(0, 0, 1);
        Gui.drawRect(x, y, x + 16, y + 16, colour);
        GL11.glTranslated(0, 0, -1);

        if (number != -1){
            String text = String.valueOf(number);
            int textWidth = mc.fontRendererObj.getStringWidth(text);

            // Push OpenGL states
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 300); // Bring the text to the foreground
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();

            // Render the string
            mc.fontRendererObj.drawStringWithShadow(text, x + 8 - textWidth / 2, y + 8 - 4, 0xFFFFFF);

            // Restore OpenGL states
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

    }

    private static final ResourceLocation beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");

    public static void renderBeaconBeam(
            double x, double y, double z, int rgb, float alphaMult,
            float partialTicks, Boolean disableDepth
    ) {
        int height = 300;
        int bottomOffset = 0;
        int topOffset = bottomOffset + height;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        if (disableDepth) {
            GlStateManager.disableDepth();
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(beaconBeam);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GlStateManager.disableLighting();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        double time = Minecraft.getMinecraft().theWorld.getTotalWorldTime() + (double) partialTicks;
        double d1 = MathHelper.func_181162_h(-time * 0.2D - (double) MathHelper.floor_double(-time * 0.1D));

        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;
        double d2 = time * 0.025D * -1.5D;
        double d4 = 0.5D + Math.cos(d2 + 2.356194490192345D) * 0.2D;
        double d5 = 0.5D + Math.sin(d2 + 2.356194490192345D) * 0.2D;
        double d6 = 0.5D + Math.cos(d2 + (Math.PI / 4D)) * 0.2D;
        double d7 = 0.5D + Math.sin(d2 + (Math.PI / 4D)) * 0.2D;
        double d8 = 0.5D + Math.cos(d2 + 3.9269908169872414D) * 0.2D;
        double d9 = 0.5D + Math.sin(d2 + 3.9269908169872414D) * 0.2D;
        double d10 = 0.5D + Math.cos(d2 + 5.497787143782138D) * 0.2D;
        double d11 = 0.5D + Math.sin(d2 + 5.497787143782138D) * 0.2D;
        double d14 = -1.0D + d1;
        double d15 = (double) (height) * 2.5D + d14;
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(x + d4, y + topOffset, z + d5).tex(1.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
        worldrenderer.pos(x + d4, y + bottomOffset, z + d5).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d6, y + bottomOffset, z + d7).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d6, y + topOffset, z + d7).tex(0.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
        worldrenderer.pos(x + d10, y + topOffset, z + d11).tex(1.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
        worldrenderer.pos(x + d10, y + bottomOffset, z + d11).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d8, y + bottomOffset, z + d9).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d8, y + topOffset, z + d9).tex(0.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
        worldrenderer.pos(x + d6, y + topOffset, z + d7).tex(1.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
        worldrenderer.pos(x + d6, y + bottomOffset, z + d7).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d10, y + bottomOffset, z + d11).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d10, y + topOffset, z + d11).tex(0.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
        worldrenderer.pos(x + d8, y + topOffset, z + d9).tex(1.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
        worldrenderer.pos(x + d8, y + bottomOffset, z + d9).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d4, y + bottomOffset, z + d5).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d4, y + topOffset, z + d5).tex(0.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
        tessellator.draw();

        GlStateManager.disableCull();
        double d12 = -1.0D + d1;
        double d13 = height + d12;

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.2D).tex(1.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.2D).tex(1.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.2D).tex(0.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.2D).tex(0.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.8D).tex(1.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.8D).tex(1.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.8D).tex(0.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.8D).tex(0.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.2D).tex(1.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.2D).tex(1.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.8D).tex(0.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.8D).tex(0.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.8D).tex(1.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.8D).tex(1.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.2D).tex(0.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.2D).tex(0.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
        tessellator.draw();

        GlStateManager.disableLighting();
        GlStateManager.enableTexture2D();
        if (disableDepth) {
            GlStateManager.enableDepth();
        }
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aabb, Color color, float width, float partialTicks) {
        Entity render = Minecraft.getMinecraft().getRenderViewEntity();

        double coordX = render.lastTickPosX + (render.posX - render.lastTickPosX) * partialTicks;
        double coordY = render.lastTickPosY + (render.posY - render.lastTickPosY) * partialTicks;
        double coordZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.translate(-coordX, -coordY, -coordZ);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(width);

        RenderGlobal.drawOutlinedBoundingBox(aabb, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        GlStateManager.translate(coordX, coordY, coordZ);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static void renderEntityHitbox(Entity entity, float partialTicks, Color color, MobDisplayTypes type) {
        renderEntityHitbox(entity, partialTicks, color, type, false);
    }

    public static void renderEntityHitbox(Entity entity, float partialTicks, Color color, MobDisplayTypes type, boolean isBoss) {

        Vector3f loc = new Vector3f(
                (float) entity.posX - 0.5f,
                (float) entity.posY - 0.5f,
                (float) entity.posZ - 0.5f);

        if (type == MobDisplayTypes.BAT || isBoss){
            GlStateManager.disableDepth();
        }
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        Entity player = mc.getRenderViewEntity();
        double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        double x = loc.x - playerX + 0.5;
        double y = loc.y - playerY - 0.5;
        if (type == MobDisplayTypes.BAT){
            y = (loc.y - playerY) + 1;
        } else if (type == MobDisplayTypes.FEL){
            y = loc.y - playerY + 2.3;
        }
        double z = loc.z - playerZ + 0.5;

        // IF the mob is a bat make the hitbox smaller
        double y1, y2, x1, x2, z1, z2;

        if (type == MobDisplayTypes.BAT) {
            y1 = y - 0.3;
            y2 = y + 0.3;
            x1 = x - 0.3;
            x2 = x + 0.3;
            z1 = z - 0.3;
            z2 = z + 0.3;
        } else if (type == MobDisplayTypes.FEL) {
            y1 = y - 0.5;
            y2 = y + 3.0;
            x1 = x - 0.5;
            x2 = x + 0.5;
            z1 = z - 0.5;
            z2 = z + 0.5;
        } else if (type == MobDisplayTypes.WOLF) {
            y1 = y - 0.0;
            y2 = y + 1.0;
            x1 = x - 0.5;
            x2 = x + 0.5;
            z1 = z - 0.5;
            z2 = z + 0.5;
        } else if (type == MobDisplayTypes.SPIDER && Boolean.TRUE == isBoss) {
            y1 = y - 1.0;
            y2 = y + 0.0;
            x1 = x - 0.75;
            x2 = x + 0.75;
            z1 = z - 0.75;
            z2 = z + 0.75;
        } else if (type == MobDisplayTypes.SPIDER) {
            y1 = y - 0.0;
            y2 = y + 1.0;
            x1 = x - 0.75;
            x2 = x + 0.75;
            z1 = z - 0.75;
            z2 = z + 0.75;
        } else if (type == MobDisplayTypes.ENDERMAN && Boolean.TRUE == isBoss){
            y1 = y - 2.75;
            y2 = y + 0.0;
            x1 = x - 0.5;
            x2 = x + 0.5;
            z1 = z - 0.5;
            z2 = z + 0.5;
        } else if (type == MobDisplayTypes.ENDERMAN){
            y1 = y - 2.0;
            y2 = y + 1.0;
            x1 = x - 0.5;
            x2 = x + 0.5;
            z1 = z - 0.5;
            z2 = z + 0.5;
        } else if (type == MobDisplayTypes.GAIA) {
            y1 = y + 1.0;
            y2 = y + 4.0;
            x1 = x - 0.75;
            x2 = x + 0.75;
            z1 = z - 0.75;
            z2 = z + 0.75;
        } else if (type == MobDisplayTypes.SIAMESE) {
            y1 = y + 0.0;
            y2 = y + 0.7;
            x1 = x - 0.3;
            x2 = x + 0.3;
            z1 = z - 0.3;
            z2 = z + 0.3;
        }  else if (type == MobDisplayTypes.BLAZE) {
            y1 = y - 1.0;
            y2 = y + 0.8;
            x1 = x - 0.3;
            x2 = x + 0.3;
            z1 = z - 0.3;
            z2 = z + 0.3;
        } else if (type == MobDisplayTypes.BLAZINGSOUL) {
            y1 = y + 0.2;
            y2 = y + 0.785;
            x1 = x - 0.3;
            x2 = x + 0.3;
            z1 = z - 0.3;
            z2 = z + 0.3;
        } else if (type == MobDisplayTypes.ITEM) {
            y1 = y + 1.0;
            y2 = y + 1.25;
            x1 = x - 0.125;
            x2 = x + 0.125;
            z1 = z - 0.125;
            z2 = z + 0.125;
        } else if (type == MobDisplayTypes.ITEMBIG) {
            y1 = y + 1.0;
            y2 = y + 2;
            x1 = x - 0.5;
            x2 = x + 0.5;
            z1 = z - 0.5;
            z2 = z + 0.5;
        } else if (type == MobDisplayTypes.WITHERESSENCE) {
            y1 = y + 2.5;
            y2 = y + 3.0;
            x1 = x - 0.3;
            x2 = x + 0.3;
            z1 = z - 0.3;
            z2 = z + 0.3;
        } else {
            y1 = y - 1;
            y2 = y + 1;
            x1 = x - 0.5;
            x2 = x + 0.5;
            z1 = z - 0.5;
            z2 = z + 0.5;
        }

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        GL11.glLineWidth(3.0f);

        if (type == MobDisplayTypes.GAIA) {
            GL11.glLineWidth(5.0f);
        }

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();

        double[][] vertices = {
                {x1, y1, z1}, {x2, y1, z1}, {x2, y2, z1}, {x1, y2, z1},
                {x1, y1, z2}, {x2, y1, z2}, {x2, y2, z2}, {x1, y2, z2}
        };

        int[][] edges = {
                {0, 1}, {1, 2}, {2, 3}, {3, 0},
                {4, 5}, {5, 6}, {6, 7}, {7, 4},
                {0, 4}, {1, 5}, {2, 6}, {3, 7}
        };

        for (int[] edge : edges) {
            worldRenderer.pos(vertices[edge[0]][0], vertices[edge[0]][1], vertices[edge[0]][2])
                    .color(red, green, blue, alpha).endVertex();
            worldRenderer.pos(vertices[edge[1]][0], vertices[edge[1]][1], vertices[edge[1]][2])
                    .color(red, green, blue, alpha).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static void drawTag(String str, double[] pos, Color color, float partialTicks) {
        FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        double viewerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double viewerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double viewerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
        double x = ((pos[0] - viewerX) + 0.5);
        double y = ((pos[1] - viewerY) + 0.5);
        double z = ((pos[2] - viewerZ) + 0.5);
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        float f = 3F;
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + 2.5, (float)z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 0;
        int j = fontrenderer.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)(-j - 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(-j - 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(j + 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(j + 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, colorToInt(color));
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static void drawTracer(int[] targetPos, Color color) {
        if (targetPos.length != 3) {
            throw new IllegalArgumentException("Target position must be an array of exactly 3 integers (x, y, z).");
        }

        // Save and disable view bobbing
        boolean userViewBobbing = Minecraft.getMinecraft().gameSettings.viewBobbing;
        Minecraft.getMinecraft().gameSettings.viewBobbing = false;

        // Restore view bobbing
        Minecraft.getMinecraft().gameSettings.viewBobbing = userViewBobbing;

        // Configure OpenGL state
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);

        // Set color
        GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        // Calculate player's eye position
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        double eyeX = 0;
        double eyeY = player.getEyeHeight();
        double eyeZ = 1;

        // Rotate based on player's pitch and yaw
        double pitchRadians = Math.toRadians(-player.rotationPitch);
        double yawRadians = Math.toRadians(-player.rotationYaw);

        double rotatedX = eyeX * Math.cos(yawRadians) - eyeZ * Math.sin(yawRadians);
        double rotatedZ = eyeX * Math.sin(yawRadians) + eyeZ * Math.cos(yawRadians);

        double finalEyeX = rotatedX;
        double finalEyeY = eyeY + eyeX * Math.sin(pitchRadians);
        double finalEyeZ = rotatedZ;

        // Draw the line
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(finalEyeX, finalEyeY, finalEyeZ); // Start at the player's eye position
        GL11.glVertex3d(targetPos[0] + 0.5, targetPos[1] + 0.5, targetPos[2] + 0.5); // End at target position (center of the block)
        GL11.glEnd();

        // Restore OpenGL state
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
    }



    public static void highlightSlot(Slot slot, Color color){
        boolean lightingState = GL11.glIsEnabled(GL11.GL_LIGHTING);

        GlStateManager.disableLighting();
        GlStateManager.color(1f, 1f, 1f, 1f);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0f, 0f, 110f + Minecraft.getMinecraft().getRenderItem().zLevel);
        Gui.drawRect(
                slot.xDisplayPosition,
                slot.yDisplayPosition,
                slot.xDisplayPosition + 16,
                slot.yDisplayPosition + 16,
                color.getRGB()
        );

        GlStateManager.popMatrix();

        if (lightingState) {
            GlStateManager.enableLighting();
        }
    }
    public static void draw3DLine(Vec3 pos1, Vec3 pos2, Color color, int lineWidth, boolean depth, float partialTicks) {
        draw3DLine(pos1, pos2, color, lineWidth, depth, partialTicks, false);
    }

    public static void draw3DLine(Vec3 pos1, Vec3 pos2, Color color, int lineWidth, boolean depth, float partialTicks, boolean fromHead) {
        Entity render = Minecraft.getMinecraft().getRenderViewEntity();
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();

        double coordX = render.lastTickPosX + (render.posX - render.lastTickPosX) * partialTicks;
        double coordY = render.lastTickPosY + (render.posY - render.lastTickPosY) * partialTicks;
        double coordZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * partialTicks;
        Vec3 pos2final = pos2;
        GlStateManager.pushMatrix();
        GlStateManager.translate(-coordX, -coordY, -coordZ);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(lineWidth);
        if (!depth) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GlStateManager.depthMask(false);
        }

        if (fromHead) {
            pos2final = new Vec3(0, 0, 1).rotatePitch(-(float) Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationPitch)).rotateYaw(-(float) Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw));
        }
        GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        worldRenderer.pos(pos1.xCoord, pos1.yCoord, pos1.zCoord).endVertex();
        worldRenderer.pos(pos2final.xCoord, pos2final.yCoord, pos2final.zCoord).endVertex();
        Tessellator.getInstance().draw();

        GlStateManager.translate(coordX, coordY, coordZ);
        if (!depth) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GlStateManager.depthMask(true);
        }
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static void highlightBlock(BlockPos pos, Color color, boolean disableDepth, float partialTicks) {
        highlightBlock(pos,color,disableDepth,false,partialTicks);
    }

    public static void highlightBlock(BlockPos pos, Color color, boolean disableDepth, boolean isButton, float partialTicks) {
        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        double x = pos.getX() - viewerX;
        double y = pos.getY() - viewerY;
        double z = pos.getZ() - viewerZ;

        if (disableDepth) GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        double initialToAddX = 0;
        if (!disableDepth) {
            initialToAddX = .05;
        }
        if (!isButton) {
            if (disableDepth) {
                RenderUtils.drawFilledBoundingBox(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1), 1f, color);
            } else {
                RenderUtils.drawFilledBoundingBox(new AxisAlignedBB(x - initialToAddX, y, z, x + 1 + initialToAddX, y + 1, z + 1), 1f, color);
            }
        } else {
            RenderUtils.drawFilledBoundingBox(new AxisAlignedBB(x, y+0.5-0.13, z+0.5-0.191, x-.13, y+0.5+0.13, z+0.5+0.191), 1f, color);
        }


        GlStateManager.enableLighting();
        if (disableDepth) GlStateManager.enableDepth();
        GlStateManager.enableCull();
    }

    public static void drawFilledBoundingBox(AxisAlignedBB p_181561_0_, float alpha, Color color) {
        GlStateManager.pushMatrix(); // Save the current state
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();

        // Ensure consistent color and alpha
        GlStateManager.color(
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                color.getAlpha() / 255f * alpha
        );

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        //vertical
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        tessellator.draw();

        GlStateManager.color(
                color.getRed() / 255f * 0.8f,
                color.getGreen() / 255f * 0.8f,
                color.getBlue() / 255f * 0.8f,
                color.getAlpha() / 255f * alpha
        );

        //x
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();

        GlStateManager.color(
                color.getRed() / 255f * 0.9f,
                color.getGreen() / 255f * 0.9f,
                color.getBlue() / 255f * 0.9f,
                color.getAlpha() / 255f * alpha
        );
        //z
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();

        // Reset OpenGL state to ensure no side effects
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F); // Reset to white
        GlStateManager.popMatrix(); // Restore the saved state
    }

    public static int colorToInt(Color color) {
        return (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
    }


}
