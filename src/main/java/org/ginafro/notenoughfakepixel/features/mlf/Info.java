package org.ginafro.notenoughfakepixel.features.mlf;

import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.events.event.HudRenderEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.StringUtils;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Gamemode;

import java.util.List;

public class Info {

    String inc, bal , e1 = "", e2 = "";
    @Subscribe
    public void onRender(HudRenderEvent e){
        if(Configuration.mlf && Configuration.INFO){
            if(NotEnoughFakepixel.currentGamemode == Gamemode.MLF){
                Gui.drawRect(5 , Minecraft.getMinecraft().displayHeight / 2 - 30, 40 , Minecraft.getMinecraft().displayHeight / 2 + 30 , new OneColor(0,0,0,102).getRGB());
                List<String> sideBarLines = ScoreboardUtils.getSidebarLines();
                a(sideBarLines);
                Minecraft.getMinecraft().fontRendererObj.drawString("Income: " + inc , 7 , Minecraft.getMinecraft().displayHeight / 2 - 22 , -1);
                Minecraft.getMinecraft().fontRendererObj.drawString("Balance: " + bal , 7 , Minecraft.getMinecraft().displayHeight / 2 - 13 , -1);
                Minecraft.getMinecraft().fontRendererObj.drawString("Events: ", 7 , Minecraft.getMinecraft().displayHeight / 2 - 5 , -1);
                Minecraft.getMinecraft().fontRendererObj.drawString("- " + e1, 7 , Minecraft.getMinecraft().displayHeight / 2 + 3 , -1);
                Minecraft.getMinecraft().fontRendererObj.drawString("- " + e1, 7 , Minecraft.getMinecraft().displayHeight / 2 + 11 , -1);

            }
        }
    }

    public void a(List<String> sideBarLines) {
        if (sideBarLines != null) {
            for (String s : sideBarLines) {
                if (s.contains("- ")) {
                    if (e1.isEmpty()) {
                        e1 = StringUtils.stripControlCodes(s.replace("- ", ""));
                    } else {
                        e2 = StringUtils.stripControlCodes(s.replace("- ", ""));
                    }
                }
                if (s.contains("Balance")) {
                    bal = StringUtils.stripControlCodes(s.replace("Balance: ", ""));
                }
                if (s.contains("Income")) {
                    inc = StringUtils.stripControlCodes(s.replace("Income: ", ""));
                }
            }

        }

    }
}
