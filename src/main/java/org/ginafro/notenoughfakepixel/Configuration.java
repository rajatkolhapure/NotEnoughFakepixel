package org.ginafro.notenoughfakepixel;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
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


    @Header(text = "General" , size = 2)
    public boolean _general = true;
    @Dropdown(name = "Theme" , size = 2 , options = {"Default" , "Dark" , "Ocean"})
    public static int theme = 1;

    // Quality Of Life

    @Header(text = "Quality of Life" , category = "Quality of Life", size = 2)
    public boolean _qol = true;
    @Switch(name = "Disable Hyperion Explosion" , category = "Quality of Life")
    public static boolean disableHyperionExplosions = true;
    @Switch(name = "Disable Thunderlord Bolt" , category = "Quality of Life")
    public static boolean disableThunderlordBolt = true;
    @Switch(name = "Fullbright" , category = "Quality of Life")
    public static boolean fullbright = true;
    @Slider(name = "Brightness" , category = "Quality of Life", min = 0f, max = 10f, step = 1 , instant = true)
    public static float gamma = 1f;
    @Switch(name = "Damage Commas" , category = "Quality of Life")
    public static boolean dmgCommas = true;

    // Dungeons

    @Header(text = "Dungeons", category = "Dungeons", size = 2)
    public boolean _dungeons = true;

    @Switch(name = "Starred Mobs Helper" , category = "Dungeons" , subcategory = "Quality of Life")
    public static boolean starredMobs = true;

    @Switch(name = "Dungeon Map" , category = "Dungeons", subcategory = "Dungeon Map")
    public static boolean dungeonMap = true;
    @Switch(name = "Dungeons Map" , category = "Dungeons", subcategory = "Dungeon Map")
    public static boolean dungeonsMap = true;
    @Slider(name = "Dungeons Map Scale" ,category = "Dungeons" , subcategory = "Dungeon Map" , min = 0.1f,max=10f)
    public static float dungeonMapScale = 1.0f;

    @Switch(name = "Starts With Solver" , category = "Dungeons" , subcategory = "Floor 7")
    public static boolean startsWith = true;
    @Switch(name = "Click In Order Solver" , category = "Dungeons" , subcategory = "Floor 7")
    public static boolean clickInOrder = true;
    @Color(name = "Terminal Overlay Color", category = "Dungeons" , subcategory = "Floor 7")
    public static OneColor terminalColor = new OneColor(0,255,0);

    // Fishing

    @Header(text = "Fishing" , category = "Fishing" , size = 2)
    public boolean _fishing = true;
    @HUD(name = "Kill Death Counter" , category = "Fishing")
    public static KDCounter counter = new KDCounter();
    @Switch(name = "Notify on Great Catch" , category = "Fishing")
    public static boolean greatCatch = true;
    @Switch(name = "Notify on Trophy Fish" , category = "Fishing")
    public static boolean trophyFish = true;

    // Slayer

    @Header(text = "Slayer" , category = "Slayer" , size = 2)
    public boolean _slayer = true;

    // Enchanting

    @Header(text = "Enchanting" , category = "Enchanting" , size = 2)
    public boolean _enchanting = true;

    @Switch(name = "UltraSequencer Solver" , category = "Enchanting" , subcategory = "Enchanting")
    public static boolean ultraSequencer = true;

    // SKYBLOCK
    @Header(text = "Skyblock" , category = "Skyblock" , size = 2)
    public boolean skyblock = true;
    @Switch(name = "Map " , category = "Skyblock" , subcategory = "Quality Of Life" )
    public static boolean sb_map = true;

    @Switch(name = "Jacob/Auction Timer" , category = "Skyblock" , subcategory = "Quality Of Life")
    public static boolean jATimer = true;

    @Switch(name = "Storage GUI Overlay" , category = "Skyblock" , subcategory = "GUI Overlays")
    public static boolean storageOverlay = false;


}
