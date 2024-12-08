package org.ginafro.notenoughfakepixel;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.*;
import org.ginafro.notenoughfakepixel.features.duels.KDCounter;
import org.ginafro.notenoughfakepixel.features.skyblock.mining.MiningOverlay;

public class Configuration extends Config {

    public Configuration(){
        super(new Mod("NotEnoughFakepixel", ModType.UTIL_QOL, "assets/notenoughfakepixel/logo.png"), "config.json");
        initialize();

        //this.addDependency("debug", "Cant be enabled with debug2", () -> !debug2);
        //this.addDependency("debug2", "Cant be enabled with debug", () -> !debug);

        this.addListener("debug", () -> debug2 = false);
        this.addListener("debug2", () -> debug = false);
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

    @Header(text = "General" , category = "Quality of Life", size = 2)
    public boolean _qol = true;
    @Switch(name = "Disable Hyperion Explosion" , category = "Quality of Life")
    public static boolean disableHyperionExplosions = true;
    @Switch(name = "Disable Thunderlord Bolt" , category = "Quality of Life")
    public static boolean disableThunderlordBolt = true;
    @Switch(name = "Fullbright" , category = "Quality of Life")
    public static boolean fullbright = true;
    @Switch(name = "Damage Commas" , category = "Quality of Life", subcategory = "Damage Formatter")
    public static boolean dmgCommas = true;
    @Switch(name = "Damage Formatter" , category = "Quality of Life", subcategory = "Damage Formatter" , description = "Formats the damage. (ie. 167k instead of 167000)")
    public static boolean dmgFormatter = true;

    // Dungeons

    @Header(text = "Dungeons", category = "Dungeons", size = 2)
    public boolean _dungeons = true;

    @Switch(name = "Starred Mobs Helper" , category = "Dungeons")
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
    //@HUD(name = "Kill Death Counter" , category = "Fishing")
    //public static KDCounter counter = new KDCounter();
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

    @Switch(name = "UltraSequencer Solver" , category = "Enchanting")
    public static boolean ultraSequencer = true;

    // Mining

    @Header(text = "Mining" , category = "Mining" , size = 2)
    public boolean _mining = true;
    @Switch(name = "Drill Fuel Overlay" , category = "Mining", subcategory = "Mining Overlay" )
    public static boolean drillFuel = true;
    @Switch(name = "Mithril Powder Overlay" , category = "Mining", subcategory = "Mining Overlay" )
    public static boolean mithrilPowder = true;

    @HUD(name = "Commision overlay" , category = "Mining", subcategory = "Mining Overlay")
    public static MiningOverlay miningOverlay = new MiningOverlay();

    // SKYBLOCK

    @Header(text = "Skyblock" , category = "Skyblock" , size = 2)
    public boolean skyblock = true;
    @Switch(name = "Jacob/Auction Timer" , category = "Skyblock")
    public static boolean jATimer = true;

    @Switch(name = "Storage GUI Overlay" , category = "Skyblock" , subcategory = "GUI Overlays")
    public static boolean storageOverlay = false;

    // DEBUG

    @Header(text = "Debug" , category = "Debug" , size = 2)
    public boolean _debug = false;
    @Switch(name = "one" , category = "Debug")
    public static boolean debug = false;
    @Switch(name = "two" , category = "Debug")
    public static boolean debug2 = false;

}
