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

        this.addListener("_debug", () -> _debug2 = false);
        this.addListener("_debug2", () -> _debug = false);
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
    @Switch(name = "No Hurt Camera" , category = "Quality of Life")
    public static boolean noHurtCam = true;
    @Switch(name = "No Nausea Effect" , category = "Quality of Life")
    public static boolean noNausea = true;
    @Switch(name = "1.12 Crops height" , category = "Quality of Life")
    public static boolean cropsHeight = false;
    @Switch(name = "Disable Watchdog & Info message" , category = "Quality of Life", subcategory = "Chat")
    public static boolean disableWatchdogInfo = false;
    @Switch(name = "Disable Friend > joined/left message" , category = "Quality of Life", subcategory = "Chat")
    public static boolean disableFriendJoin = false;
    @Switch(name = "Damage Commas" , category = "Quality of Life", subcategory = "Damage Formatter")
    public static boolean dmgCommas = true;
    @Switch(name = "Damage Formatter" , category = "Quality of Life", subcategory = "Damage Formatter" , description = "Formats the damage. (ie. 167k instead of 167000)")
    public static boolean dmgFormatter = true;

    // Dungeons

    @Header(text = "Dungeons", category = "Dungeons", size = 2)
    public boolean _dungeons = true;

    @Switch(name = "Starred Mobs Helper" , category = "Dungeons", subcategory = "Starred Mobs")
    public static boolean starredMobs = true;
    @Color(name = "Starred Mobs Color", category = "Dungeons", subcategory = "Starred Mobs")
    public static OneColor starredBoxColor = new OneColor(92, 154, 255);
    @Switch(name = "Bat Mobs Display" , category = "Dungeons", subcategory = "Starred Mobs")
    public static boolean batMobs = true;
    @Color(name = "Bat Mob Color", category = "Dungeons", subcategory = "Starred Mobs")
    public static OneColor batColor = new OneColor(92, 154, 255);
    @Switch(name = "Fel Mobs Display" , category = "Dungeons", subcategory = "Starred Mobs")
    public static boolean felMob = true;
    @Color(name = "Fel Mob Color", category = "Dungeons", subcategory = "Starred Mobs")
    public static OneColor felColor = new OneColor(92, 154, 255);


    @Switch(name = "Three Weirdos Solver" , category = "Dungeons")
    public static boolean threeWeirdos = true;

    @Switch(name = "Dungeons Map" , category = "Dungeons", subcategory = "Dungeon Map")
    public static boolean dungeonsMap = true;
    @Slider(name = "Dungeons Map Scale" ,category = "Dungeons" , subcategory = "Dungeon Map" , min = 0.1f,max=10f)
    public static float dungeonMapScale = 1.0f;

    @Switch(name = "Starts With Solver" , category = "Dungeons" , subcategory = "Floor 7")
    public static boolean startsWith = true;
    @Switch(name = "Click In Order Solver" , category = "Dungeons" , subcategory = "Floor 7")
    public static boolean clickInOrder = true;
    @Switch(name = "Select colors Solver" , category = "Dungeons" , subcategory = "Floor 7")
    public static boolean selectColors = true;

    @Color(name = "Terminal Overlay Color", category = "Dungeons" , subcategory = "Floor 7")
    public static OneColor terminalColor = new OneColor(0,255,0);

    // Fishing

    @Header(text = "Fishing" , category = "Fishing" , size = 2)
    public boolean _fishing = true;
    @Switch(name = "Notify Legendary Creatures" , category = "Fishing")
    public static boolean legendaryCreatures = true;
    //@Switch(name = "Notify on Great Catch (not working?)" , category = "Fishing")
    public static boolean greatCatch = true;
    @Switch(name = "Notify on Trophy Fish" , category = "Fishing")
    public static boolean trophyFish = true;

    // Slayer

    @Header(text = "Slayer" , category = "Slayer" , size = 2)
    public boolean _slayer = true;

    @Switch(name = "Slayer Minibosses Display" , category = "Slayer" , subcategory = "Slayer Mobs")
    public static boolean slayerMinibosses = true;
    @Color(name = "Slayer Mobs Color", category = "Slayer" , subcategory = "Slayer Mobs")
    public static OneColor slayerColor = new OneColor(92, 154, 255);
    @Switch(name = "Slayer Bosses Display" , category = "Slayer" , subcategory = "Slayer Mobs")
    public static boolean slayerBosses = true;
    @Color(name = "Slayer Bosses Color", category = "Slayer" , subcategory = "Slayer Mobs")
    public static OneColor slayerBossColor = new OneColor(92, 154, 255);



    // Enchanting

    @Header(text = "Enchanting" , category = "Enchanting" , size = 2)
    public boolean _enchanting = true;

    @Header(text = "Coming soon..." , category = "Enchanting" , size = 1)
    public static boolean ultraSequencer = true;

    // Mining

    @Header(text = "Mining" , category = "Mining" , size = 2)
    public boolean _mining = true;
    @Switch(name = "Enable mining ability Notifier" , category = "Mining", subcategory = "")
    public static boolean miningAbilityNotifier = true;
    @Switch(name = "Disable Don Espresso messages" , category = "Mining", subcategory = "")
    public static boolean disableDonEspresso = true;


    @Switch(name = "Drill Fuel Overlay" , category = "Mining", subcategory = "Mining Overlay" )
    public static boolean drillFuel = true;
    @Switch(name = "Mithril Powder Overlay" , category = "Mining", subcategory = "Mining Overlay" )
    public static boolean mithrilPowder = true;
    @Switch(name = "Ability Cooldown Overlay" , category = "Mining", subcategory = "Mining Overlay")
    public static boolean abilityCooldown = true;
    @HUD(name = "Commision overlay" , category = "Mining", subcategory = "Mining Overlay")
    public static MiningOverlay miningOverlay = new MiningOverlay();

    // SKYBLOCK

//    @Header(text = "Skyblock" , category = "Skyblock" , size = 2)
//    public boolean skyblock = true;
//    @Switch(name = "Jacob/Auction Timer" , category = "Skyblock")
    public static boolean jATimer = true;
//
//    @Switch(name = "Storage GUI Overlay" , category = "Skyblock" , subcategory = "GUI Overlays")
    public static boolean storageOverlay = false;

    // DEBUG

    @Header(text = "Debug" , category = "Debug" , size = 2)
    public boolean _debugHead = false;
    @Info(text = "Ignore this if you don't know what you are doing." , type = InfoType.WARNING , category = "Debug")
    public boolean _debugInfo = false;

    @Switch(name = "one" , category = "Debug")
    public static boolean _debug = false;
    @Switch(name = "two" , category = "Debug")
    public static boolean _debug2 = false;
    @Info(text = "info test \u00a7cTEST", type = InfoType.INFO, category = "Debug", subcategory = "Info")
    public static boolean _info = true;
    @Info(text = "info test \u00a7eTEST", type = InfoType.WARNING, category = "Debug", subcategory = "Info")
    public static boolean _info2 = true;
    @Info(text = "info test \u00a7cTEST", type = InfoType.ERROR, category = "Debug", subcategory = "Info")
    public static boolean _info3 = true;

}
