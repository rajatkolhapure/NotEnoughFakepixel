package org.ginafro.notenoughfakepixel;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import org.ginafro.notenoughfakepixel.features.duels.KDCounter;
import org.ginafro.notenoughfakepixel.features.mlf.Map;

public class Configuration extends Config {

    public Configuration(){
        super(new Mod("NotEnoughFakepixel", ModType.UTIL_QOL, "assets/notenoughfakepixel/logo.png"), "config.json");
        initialize();
    }

    @Override
    public boolean supportsProfiles() {
        return true;
    }


    @Header(text = "Gamemode mods" , size = 2)
    public boolean header = true;
    @Switch(name = "My Little Farm" , description = "Enable/Disable all my little farm mods")
    public static boolean mlf = true;
    @Switch(name = "Skyblock" , description = "Enable/Disable all skyblock mods")
    public static boolean sb = true;
    @Switch(name = "Bedwars" , description = "Enable/Disable all bedwars mods")
    public static boolean bw = true;
    @Switch(name = "Skywars" , description = "Enable/Disable all skywars mods")
    public static boolean sw = true;
    @Switch(name = "Duels" , description = "Enable/Disable all duels mods")
    public static boolean duels = true;
    @Dropdown(name = "Theme" , size = 2 , options = {"Default" , "Dark" , "Ocean"})
    public static int theme = 1;

    // Quality Of Life

    @Header(text = "Quality of Life" , category = "Quality of Life", size = 2)
    public boolean qol = true;
    @Switch(name = "Fullbright" , category = "Quality of Life")
    public static boolean fullbright = true;
    @Slider(name = "Brightness" , category = "Quality of Life", min = 0f, max = 10f)
    public static float gamma = 1f;

    // MLF

    @Header(text = "My Little Farm", category = "My Little Farm", size = 2)
    public boolean mylittlefarm = true;
    @Switch(name = "MLF Info" , description = "Get information on various things in mlf", category = "My Little Farm")
    public static boolean INFO = false;
    @HUD(name = "Map Display" , category = "My Little Farm")
    public static Map map = new Map();

    // DUELS

    @Header(text = "Duels" , category = "Duels" , size = 2)
    public boolean ignored = true;
    @HUD(name = "Kill Death Counter" , category = "Duels")
    public static KDCounter counter = new KDCounter();



    // BW

    @Header(text = "BedWars" , category = "BedWars" , size = 2)
    public boolean bedwars = true;

    // SW
    @Header(text = "SkyWars" , category = "SkyWars" , size = 2)
    public boolean skywars = true;

    // SKYBLOCK
    @Header(text = "Skyblock" , category = "Skyblock" , size = 2)
    public boolean skyblock = true;
    @Switch(name = "Map " , category = "Skyblock" , subcategory = "Quality Of Life" )
    public static boolean sb_map = true;
    @Switch(name = "Dungeons Map" , category = "Skyblock" , subcategory = "Dungeons" )
    public static boolean dungeonsMap = true;
    @Slider(name = "Dungeons Map Scale" ,category = "Skyblock" , subcategory = "Dungeons", min = 0.1f,max=10f)
    public static float dungeonMapScale = 1.0f;
    @Switch(name = "Starts With Solver" , category = "Skyblock" , subcategory = "Dungeons")
    public static boolean startsWith = true;
    @Switch(name = "Click In Order Solver" , category = "Skyblock" , subcategory = "Dungeons")
    public static boolean clickInOrder = true;
    @Color(name = "Terminal Overlay Color", category = "Skyblock" , subcategory = "Dungeons")
    public static OneColor terminalColor = new OneColor(0,255,0);
    @Switch(name = "Notify on Great Catch" , category = "Skyblock" , subcategory = "Fishing")
    public static boolean greatCatch = true;
    @Switch(name = "Notify on Trophy Fish" , category = "Skyblock" , subcategory = "Fishing")
    public static boolean trophyFish = true;
    @Switch(name = "Damage Commas" , category = "Skyblock" , subcategory = "Quality Of Life")
    public static boolean dmgCommas = true;
    @Switch(name = "Slot Locking" , category = "Skyblock" , subcategory = "Quality Of Life")
    public static boolean slotLocking = true;
    @Switch(name = "Jacob/Auction Timer" , category = "Skyblock" , subcategory = "Quality Of Life")
    public static boolean jATimer = true;
    @Switch(name = "Starred Mobs Helper" , category = "Skyblock" , subcategory = "Dungeons")
    public static boolean starredMobs = true;

    @Switch(name = "Storage GUI Overlay" , category = "Skyblock" , subcategory = "GUI Overlays")
    public static boolean storageOverlay = true;

    @Switch(name = "UltraSequencer Solver" , category = "Skyblock" , subcategory = "Enchanting")
    public static boolean ultraSequencer = true;
    // HUDs
}
