package org.ginafro.notenoughfakepixel.features.duels;

import cc.polyfrost.oneconfig.hud.BasicHud;
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.renderer.font.Font;
import cc.polyfrost.oneconfig.renderer.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Gamemode;

public class KDCounter extends BasicHud {

    public static int deaths = 0;
    public static int kill = 0;



    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e){
        if(!Configuration.duels && !Configuration.counter.isEnabled() && ScoreboardUtils.currentGamemode != Gamemode.DUELS) return;
        String opponent = "";
        if(e.message.getUnformattedText().contains("Opponents")){
            opponent = e.message.getUnformattedText().replace("Opponents: ", "");
        }
        String[] msges = e.message.getUnformattedText().split(" ");
        if(msges[0] == opponent){
            kill++;
        }
        if(msges[0] == Minecraft.getMinecraft().thePlayer.getName()){
            deaths++;
        }
        if(e.message.getUnformattedText().contains("WINNER")){
            kill = 0;
            deaths = 0;
        }
    }

    @Override
    protected void draw(UMatrixStack matrices, float x, float y, float scale, boolean example) {
        if(!Configuration.duels && !Configuration.counter.isEnabled() && ScoreboardUtils.currentGamemode != Gamemode.DUELS) return;
        GlStateManager.scale(scale,scale,scale);
        Minecraft.getMinecraft().fontRendererObj.drawString("K/D: " + kill + "/" + deaths , (int) x, (int) y,-1);
    }

    @Override
    protected float getWidth(float scale, boolean example) {
        return 45 * scale;
    }

    @Override
    protected float getHeight(float scale, boolean example) {
        return 11 * scale ;
    }
}
