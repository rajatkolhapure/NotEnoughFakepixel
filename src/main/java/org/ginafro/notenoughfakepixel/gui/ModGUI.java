package org.ginafro.notenoughfakepixel.gui;

import net.minecraft.client.gui.GuiScreen;

public abstract class ModGUI extends GuiScreen{


    protected void drawrect(int x,int y, int x1, int y1 , int color){
        net.minecraft.client.gui.Gui.drawRect(x, y, x1, y1, color);
    }

}

