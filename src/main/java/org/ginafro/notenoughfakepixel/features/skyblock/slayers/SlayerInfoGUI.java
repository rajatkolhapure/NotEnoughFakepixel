package org.ginafro.notenoughfakepixel.features.skyblock.slayers;

import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.renderer.font.Fonts;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityWolf;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.gui.ModGUI;

import java.io.IOException;
import java.util.Objects;

public class SlayerInfoGUI extends ModGUI {

    public static String ENDERMAN = "Enderman", SPIDER = "Spider" , ZOMBIE = "Zombie" , BLAZE = "Blaze" , WOLF = "Wolf";
    private static String boss;
    public EntityLivingBase entity;
    public SlayerInfoGUI(String b){
        if(Objects.equals(b, ENDERMAN)){
            entity = new EntityEnderman(Minecraft.getMinecraft().theWorld);
            EntityEnderman ent = (EntityEnderman) entity;
            boss = "Enderman";
        }
        if(Objects.equals(b, SPIDER)){
            entity = new EntitySpider(Minecraft.getMinecraft().theWorld);
            boss = "Tarantula";
        }
        if(Objects.equals(b, ZOMBIE)){
            entity = new EntityZombie(Minecraft.getMinecraft().theWorld);
            boss = "Revenant";
        }
        if(Objects.equals(b, BLAZE)){
            entity = new EntityBlaze(Minecraft.getMinecraft().theWorld);
            boss = "Inferno Dreadlord";
        }
        if(Objects.equals(b, WOLF)){
            entity = new EntityWolf(Minecraft.getMinecraft().theWorld);
            boss = "Wolf";
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.scale(1.0f,1.0f,1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(NotEnoughFakepixel.bg);
        int x = this.width / 2 - 250;
        int y = this.height / 2 - 125;
        Gui.drawScaledCustomSizeModalRect(x , y , 0f,0f,500,250,500,250,500,250);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(boss , x + 25 , y + 15, -1);
        renderEntity(entity, this.width / 2 - 180, this.height / 2 + 100, this.width / 2 - 170, this.height / 2 );
    }


    public static void renderEntity(EntityLivingBase entity, int posX, int posY, int mouseX, int mouseY) {
        GlStateManager.color(1F, 1F, 1F, 1F);

        int scale = 70;
        float bottomOffset = 0F;
        EntityLivingBase stack = entity;
        while (true) {

            stack.ticksExisted = Minecraft.getMinecraft().thePlayer.ticksExisted;
            GuiInventory.drawEntityOnScreen(
                    posX,
                    (int) (posY - bottomOffset * scale),
                    scale,
                    posX - mouseX,
                    (int) (posY - stack.getEyeHeight() * scale - mouseY),
                    stack
            );
            bottomOffset += stack.getMountedYOffset();
            if (!(stack.riddenByEntity instanceof EntityLivingBase)) {
                break;
            }
            stack = (EntityLivingBase) stack.riddenByEntity;
        }

    }

}
