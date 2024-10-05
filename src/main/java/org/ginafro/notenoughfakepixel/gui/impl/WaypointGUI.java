package org.ginafro.notenoughfakepixel.gui.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import org.ginafro.notenoughfakepixel.gui.ModGUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WaypointGUI extends ModGUI {

    public static List<Waypoint> waypoints = new ArrayList<>();

    private GuiTextField xPos,YPos,ZPos;
    @Override
    public void initGui() {
        Minecraft mc = Minecraft.getMinecraft();
         xPos = new GuiTextField(100 ,mc.fontRendererObj , this.width / 2 - 150 , this.height / 2 - 100 , 90 , 30);
         YPos = new GuiTextField(101 ,mc.fontRendererObj , this.width / 2 - 50 , this.height / 2 - 100 , 90 , 30);
         ZPos = new GuiTextField(102 ,mc.fontRendererObj , this.width / 2 + 50 , this.height / 2 - 100 , 90 , 30);
         this.buttonList.add(new GuiButton(1001, this.width / 2 , this.height / 2, 100 , 30 , "Create Waypoint"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 1001){
            Waypoint waypoint = new Waypoint(Double.valueOf(xPos.getSelectedText()), Double.valueOf(YPos.getSelectedText()), Double.valueOf(ZPos.getSelectedText()), Minecraft.getMinecraft().theWorld);
            waypoints.add(waypoint);
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        xPos.drawTextBox();
        YPos.drawTextBox();
        ZPos.drawTextBox();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        xPos.textboxKeyTyped(typedChar,keyCode);
        YPos.textboxKeyTyped(typedChar,keyCode);
        ZPos.textboxKeyTyped(typedChar,keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        xPos.mouseClicked(mouseX,mouseX,mouseButton);
        YPos.mouseClicked(mouseX,mouseX,mouseButton);
        ZPos.mouseClicked(mouseX,mouseX,mouseButton);
    }
}
