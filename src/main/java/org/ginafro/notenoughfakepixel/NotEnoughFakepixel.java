package org.ginafro.notenoughfakepixel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.commands.TestCommand;

@Mod(modid = "notenoughfakepixel", useMetadata=true)
public class NotEnoughFakepixel {
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new TestCommand());
        System.out.println("Command Registered");
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static GuiScreen openGui;
    public static long lastOpenedGui;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e){
        if (e.phase != TickEvent.Phase.START) return;
        if (Minecraft.getMinecraft().thePlayer == null) {
            openGui = null;
            return;
        }
        if (openGui != null) {
            if (Minecraft.getMinecraft().thePlayer.openContainer != null) {
                Minecraft.getMinecraft().thePlayer.closeScreen();
            }
            Minecraft.getMinecraft().displayGuiScreen(openGui);
            openGui = null;
            lastOpenedGui = System.currentTimeMillis();
        }
    }
}