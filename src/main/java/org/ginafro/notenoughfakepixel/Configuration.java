package org.ginafro.notenoughfakepixel;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.*;
import org.ginafro.notenoughfakepixel.features.skyblock.mining.MiningOverlay;
import org.ginafro.notenoughfakepixel.features.skyblock.mining.RemoveGhostInvis;

public class Configuration extends Config {

    public Configuration(){
        super(new Mod("NotEnoughFakepixel", ModType.UTIL_QOL, "assets/notenoughfakepixel/logo.png"), "config.json");
        initialize();

        //this.addDependency("debug", "Cant be enabled with debug2", () -> !debug2);
        //this.addDependency("debug2", "Cant be enabled with debug", () -> !debug);

        this.addListener("dmgFormatter", () -> {
            if (dmgFormatter) {
                dmgCommas = true;
            }
        });

        this.addListener("showGhosts", () -> {
            if (!showGhosts) {
                RemoveGhostInvis.resetGhostInvis();
            }
        });

        this.addListener("_debug", () -> _debug2 = false);
        this.addListener("_debug2", () -> _debug = false);

        save();
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
    @Switch(name = "Disable Potion Effects in Inventory" , category = "Quality of Life")
    public static boolean disablePotionEffects = true;

    @Switch(name = "Show pet equipped" , category = "Quality of Life", subcategory = "Pets")
    public static boolean showPetEquipped = true;
    @Color(name = "Pet Equipped Color", category = "Quality of Life", subcategory = "Pets")
    public static OneColor petEquippedColor = new OneColor(190, 255, 190);

    @Switch(name = "Disable Watchdog & Info message" , category = "Quality of Life", subcategory = "Chat")
    public static boolean disableWatchdogInfo = false;
    @Switch(name = "Disable Friend > joined/left message" , category = "Quality of Life", subcategory = "Chat")
    public static boolean disableFriendJoin = false;
    //@Switch(name = "Chat Cleaner" , category = "Quality of Life", subcategory = "Chat")
    public static boolean chatCleaner = false;


    @Switch(name = "Damage Commas" , category = "Quality of Life", subcategory = "Damage Formatter")
    public static boolean dmgCommas = false;
    @Switch(name = "Damage Formatter" , category = "Quality of Life", subcategory = "Damage Formatter" , description = "Formats the damage. (ie. 167k instead of 167000)")
    public static boolean dmgFormatter = false;

    // Dungeons

    @Header(text = "Dungeons", category = "Dungeons", size = 2)
    public boolean _dungeons = true;

    @Switch(name = "Auto Ready Dungeon" , category = "Dungeons", subcategory = "QOL", description = "Automatically ready up in dungeons.")
    public static boolean autoReadyDungeon = true;
    @Text(name = "Auto Ready Name (if nicked)" , category = "Dungeons", subcategory = "QOL", description = "The name this will search for when you are nicked")
    public static String autoReadyName = "your nicked name";

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

    @Switch(name = "Three Weirdos Solver" , category = "Dungeons", subcategory = "Puzzles")
    public static boolean threeWeirdos = true;

    @Switch(name = "Dungeons Map" , category = "Dungeons", subcategory = "Dungeon Map")
    public static boolean dungeonsMap = true;
    @Slider(name = "Dungeons Map Scale" ,category = "Dungeons" , subcategory = "Dungeon Map" , min = 0.1f,max=10f)
    public static float dungeonMapScale = 1.0f;

    @Switch(name = "Starts With Solver" , category = "Dungeons" , subcategory = "Floor 7")
    public static boolean startsWith = true;
    //@Switch(name = "Click In Order Solver" , category = "Dungeons" , subcategory = "Floor 7")
    public static boolean clickInOrder = true;
    @Switch(name = "Select colors Solver" , category = "Dungeons" , subcategory = "Floor 7")
    public static boolean selectColors = true;

    @Color(name = "Terminal Overlay Color", category = "Dungeons" , subcategory = "Floor 7")
    public static OneColor terminalColor = new OneColor(0,255,0);

    // Fishing

    @Header(text = "Fishing" , category = "Fishing" , size = 2)
    public boolean _fishing = true;
    @Switch(name = "Notify Legendary Creatures" , category = "Fishing", description = "Notifies you when a legendary creature is catched.")
    public static boolean legendaryCreatures = true;
    //@Switch(name = "Notify on Great Catch (not working?)" , category = "Fishing")
    public static boolean greatCatch = true;
    @Switch(name = "Notify on Trophy Fish" , category = "Fishing")
    public static boolean trophyFish = true;

    // Slayer

    @Header(text = "Slayer" , category = "Slayer" , size = 2)
    public boolean _slayer = true;

    @Switch(name = "Slayer Minibosses Display" , category = "Slayer" , subcategory = "Slayer Mobs", description = "Draws a box around slayer minibosses.")
    public static boolean slayerMinibosses = true;
    @Color(name = "Slayer Minibosses Color", category = "Slayer" , subcategory = "Slayer Mobs", description = "Color of the slayer minibosses.")
    public static OneColor slayerColor = new OneColor(92, 154, 255);
    @Switch(name = "Slayer Bosses Display" , category = "Slayer" , subcategory = "Slayer Mobs", description = "Draws a box around slayer bosses.")
    public static boolean slayerBosses = true;
    @Color(name = "Slayer Bosses Color", category = "Slayer" , subcategory = "Slayer Mobs", description = "Color of the slayer bosses.")
    public static OneColor slayerBossColor = new OneColor(92, 154, 255);

    // Enchanting

    @Header(text = "Enchanting" , category = "Enchanting" , size = 2)
    public boolean _enchanting = true;

    @Header(text = "Coming soon..." , category = "Enchanting" , size = 2, subcategory = "")

    //@Switch(name = "Ultra Sequencer" , category = "Enchanting" , subcategory = "Enchanting")
    public static boolean ultraSequencerSolver = true;
    //@Switch(name = "Chronomatron Solver" , category = "Enchanting" , subcategory = "Enchanting")
    public static boolean chronomatronSolver = true;

    // Mining

    @Header(text = "Mining" , category = "Mining" , size = 2)
    public boolean _mining = true;
    @Switch(name = "Enable mining ability Notifier" , category = "Mining", subcategory = "", description = "Notifies you when your mining ability is ready.")
    public static boolean miningAbilityNotifier = true;
    @Switch(name = "Disable Don Espresso messages" , category = "Mining", subcategory = "", description = "Disables Don Espresso event messages.")
    public static boolean disableDonEspresso = true;
    @Switch(name = "Fix Drill Animation Reset" , category = "Mining", subcategory = "" , description = "Fixes drill animation resetting when the fuel updates.")
    public static boolean drillFix = true;
    @Switch(name = "Puzzler solver" , category = "Mining", subcategory = "" , description = "Solves the Puzzler block.")
    public static boolean puzzlerSolver = true;
    @Switch(name = "Remove Ghosts invisibility" , category = "Mining", subcategory = "" , description = "Removes the invisibility of the ghosts")
    public static boolean showGhosts = true;

    @Switch(name = "Drill Fuel Overlay" , category = "Mining", subcategory = "Mining Overlay", description = "Shows the drill fuel in overlay.")
    public static boolean drillFuel = true;
    @Switch(name = "Mithril Powder Overlay" , category = "Mining", subcategory = "Mining Overlay", description = "Shows the mithril powder in overlay.")
    public static boolean mithrilPowder = true;
    @Switch(name = "Ability Cooldown Overlay" , category = "Mining", subcategory = "Mining Overlay", description = "Shows the ability cooldown in overlay.")
    public static boolean abilityCooldown = true;
    @HUD(name = "Commission overlay" , category = "Mining", subcategory = "Mining Overlay")
    public static MiningOverlay miningOverlay = new MiningOverlay();

    // SKYBLOCK

//    @Header(text = "Skyblock" , category = "Skyblock" , size = 2)
//    public boolean skyblock = true;
//    @Switch(name = "Jacob/Auction Timer" , category = "Skyblock")
    public static boolean jATimer = true;

    //@Switch(name = "Storage GUI Overlay" , category = "Skyblock" , subcategory = "GUI Overlays")
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
    @Switch(name = "Enable logs" , category = "Debug", subcategory = "Logs")
    public static boolean logs = false;

}
