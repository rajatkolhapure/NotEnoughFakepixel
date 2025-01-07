package org.ginafro.notenoughfakepixel;

import cc.polyfrost.oneconfig.events.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.ginafro.notenoughfakepixel.commands.TestCommand;
import org.ginafro.notenoughfakepixel.features.duels.KDCounter;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.*;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals.ClickOnColorsSolver;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals.StartingWithSolver;
import org.ginafro.notenoughfakepixel.features.skyblock.fishing.GreatCatchNotifier;
import org.ginafro.notenoughfakepixel.features.skyblock.mining.*;
import org.ginafro.notenoughfakepixel.features.skyblock.overlays.StorageOverlay;
import org.ginafro.notenoughfakepixel.features.skyblock.qol.*;
import org.ginafro.notenoughfakepixel.features.skyblock.slayers.SlayerInfoCommand;
import org.ginafro.notenoughfakepixel.features.skyblock.slayers.SlayerMobsDisplay;
import org.ginafro.notenoughfakepixel.events.Handlers.PacketHandler;
import org.ginafro.notenoughfakepixel.utils.*;

@Mod(modid = "notenoughfakepixel", useMetadata=true)
public class NotEnoughFakepixel {

    public static Configuration config;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        config = new Configuration();
        ClientCommandHandler.instance.registerCommand(new TestCommand());
        ClientCommandHandler.instance.registerCommand(new SlayerInfoCommand());

        MinecraftForge.EVENT_BUS.register(this);
        registerModEvents();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e){

    }

    private void registerModEvents() {
        // Dungeons
        MinecraftForge.EVENT_BUS.register(new StartingWithSolver());
        MinecraftForge.EVENT_BUS.register(new ClickOnColorsSolver());

        MinecraftForge.EVENT_BUS.register(new AutoReadyDungeon());
        MinecraftForge.EVENT_BUS.register(new AutoCloseChests());

        MinecraftForge.EVENT_BUS.register(new StarredMobDisplay());
        MinecraftForge.EVENT_BUS.register(new BatMobDisplay());
        MinecraftForge.EVENT_BUS.register(new FelMobDisplay());
        MinecraftForge.EVENT_BUS.register(new ThreeWeirdos());
        EventManager.INSTANCE.register(new DungeonsMap());
        // Mining
        MinecraftForge.EVENT_BUS.register(new MiningOverlay());
        MinecraftForge.EVENT_BUS.register(new DrillFuelParsing());
        MinecraftForge.EVENT_BUS.register(new AbilityNotifier());
        MinecraftForge.EVENT_BUS.register(new EventsMsgSupressor());
        MinecraftForge.EVENT_BUS.register(new DrillFix());
        MinecraftForge.EVENT_BUS.register(new PuzzlerSolver());
        MinecraftForge.EVENT_BUS.register(new RemoveGhostInvis());
        // Fishing
        MinecraftForge.EVENT_BUS.register(new GreatCatchNotifier());
        // Enchanting
        //MinecraftForge.EVENT_BUS.register(new EnchantingSolvers());
        MinecraftForge.EVENT_BUS.register(new ChocolateFactory());
        // QOL
        MinecraftForge.EVENT_BUS.register(new ShowCurrentPet());
        MinecraftForge.EVENT_BUS.register(new ChatCleaner());
        MinecraftForge.EVENT_BUS.register(new MiddleClickEvent());
        //MinecraftForge.EVENT_BUS.register(new SlotLocking());
        MinecraftForge.EVENT_BUS.register(new StorageOverlay.StorageEvent());
        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new Fullbright());
        MinecraftForge.EVENT_BUS.register(new KDCounter());
        // Slayer
        MinecraftForge.EVENT_BUS.register(new SlayerMobsDisplay());
        // Parsers
        MinecraftForge.EVENT_BUS.register(new TablistParser());
        MinecraftForge.EVENT_BUS.register(new ScoreboardUtils());

    }

    public static GuiScreen openGui;
    public static long lastOpenedGui;
    public int theme = 0;
    public static String th = "default";
    public static ResourceLocation bg = new ResourceLocation("notenoughfakepixel:backgrounds/" + th + "/background.png");

    public String getTheme(){
        return th;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e){
        if (e.phase != TickEvent.Phase.START) return;
        if (Minecraft.getMinecraft().thePlayer == null) {
            openGui = null;
            return;
        }
        if(Configuration.theme != theme){
            this.theme = Configuration.theme;
            if(Configuration.theme == 0){
                th = "default";
            }else if(Configuration.theme == 1){
                th = "dark";
            }else if(Configuration.theme == 2){
                th = "ocean";
            }
            bg = new ResourceLocation("notenoughfakepixel:backgrounds/" + th + "/background.png");
        }
        if (openGui != null) {
            if (Minecraft.getMinecraft().thePlayer.openContainer != null) {
                Minecraft.getMinecraft().thePlayer.closeScreen();
            }
            Minecraft.getMinecraft().displayGuiScreen(openGui);
            openGui = null;
            lastOpenedGui = System.currentTimeMillis();
        }

        ScoreboardUtils.parseScoreboard();
    }

    @SubscribeEvent
    public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        event.manager.channel().pipeline().addBefore("packet_handler", "nef_packet_handler", new PacketHandler());
        System.out.println("Added packet handler to channel pipeline.");
    }
}