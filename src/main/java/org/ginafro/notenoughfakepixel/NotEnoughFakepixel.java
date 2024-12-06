package org.ginafro.notenoughfakepixel;

import cc.polyfrost.oneconfig.events.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.commands.TestCommand;
import org.ginafro.notenoughfakepixel.features.duels.KDCounter;
import org.ginafro.notenoughfakepixel.features.mlf.Info;
import org.ginafro.notenoughfakepixel.features.mlf.Map;
import org.ginafro.notenoughfakepixel.features.other.Fullbright;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.DungeonsMap;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.StarredMobDisplay;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals.StartingWithSolver;
import org.ginafro.notenoughfakepixel.features.skyblock.fishing.GreatCatchNotifier;
import org.ginafro.notenoughfakepixel.features.skyblock.overlays.StorageOverlay;
import org.ginafro.notenoughfakepixel.features.skyblock.qol.SlotLocking;
import org.ginafro.notenoughfakepixel.features.skyblock.slayers.SlayerInfoCommand;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Gamemode;
import org.ginafro.notenoughfakepixel.variables.Location;
import org.lwjgl.input.Keyboard;

@Mod(modid = "notenoughfakepixel", useMetadata=true)
public class NotEnoughFakepixel {

    public static Gamemode currentGamemode;
    public static Location sbLocation;
    public static Configuration config;
    public static boolean inDungeons = false;
    public static KeyBinding slotLocking = new KeyBinding("Slot Locking" , Keyboard.KEY_L , "NotEnoughFakepixel");

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        config = new Configuration();
        ClientCommandHandler.instance.registerCommand(new TestCommand());
        ClientCommandHandler.instance.registerCommand(new SlayerInfoCommand());
        ClientRegistry.registerKeyBinding(slotLocking);
        System.out.println("Command Registered");
        MinecraftForge.EVENT_BUS.register(this);
        registerModEvents();
        sbLocation = null;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e){

    }

    private void registerModEvents() {
        MinecraftForge.EVENT_BUS.register(new StartingWithSolver());
        MinecraftForge.EVENT_BUS.register(new KDCounter());
        MinecraftForge.EVENT_BUS.register(new GreatCatchNotifier());
        MinecraftForge.EVENT_BUS.register(new SlotLocking());
        MinecraftForge.EVENT_BUS.register(new StorageOverlay.StorageEvent());
        MinecraftForge.EVENT_BUS.register(new StarredMobDisplay());
        MinecraftForge.EVENT_BUS.register(this);
        EventManager.INSTANCE.register(new Info());
        EventManager.INSTANCE.register(new Fullbright());
        EventManager.INSTANCE.register(new DungeonsMap());
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

        if(!Minecraft.getMinecraft().isSingleplayer()){
            if(Minecraft.getMinecraft().getCurrentServerData().serverIP.contains("fakepixel")) {
                ScoreObjective objective = Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
                if (objective != null) {
                    String objName = ScoreboardUtils.cleanSB(objective.getDisplayName());
                    {
                        currentGamemode = Gamemode.getGamemode(objName);
                    }
                }
            }
        }

        if(currentGamemode == Gamemode.SKYBLOCK){
            if (!Minecraft.getMinecraft().isSingleplayer() && Minecraft.getMinecraft().getNetHandler() != null) {
                for(NetworkPlayerInfo playerInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()){
                    IChatComponent s1 = playerInfo.getDisplayName();
                    String name = "";
                    if(s1 != null){
                        name = StringUtils.stripControlCodes(s1.getUnformattedText());
                        if(name != null) {
                            if (name.contains("Area")) {
                                currentGamemode = Gamemode.SKYBLOCK;
                                String s = name.replace("Area: ", "");
                                sbLocation = Location.getLocation(s);
                            }
                            if (name.contains("Dungeon")){
                                currentGamemode = Gamemode.SKYBLOCK;
                                inDungeons = true;
                            }
                        }
                    }
                }
            }
        }

    }

}