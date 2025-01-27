package org.ginafro.notenoughfakepixel.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;

public class NefCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "nef";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " <Category>";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args == null || args.length == 0){
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"+System.getProperty("os.name"));
            if (System.getProperty("os.name").contains("Android") || System.getProperty("os.name").contains("Linux")) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Usage: " + EnumChatFormatting.RESET + EnumChatFormatting.BOLD+EnumChatFormatting.GRAY + "/nef category "+EnumChatFormatting.RESET+EnumChatFormatting.GRAY+"- Enable/disable one entire category. Possible categories: qol, dungeons, fishing, diana, slayer, experimentation, mining, crimson.\n        " + EnumChatFormatting.BOLD+EnumChatFormatting.GRAY + "/nef default "+EnumChatFormatting.RESET+EnumChatFormatting.GRAY+"- Resets all settings to default\n        " + EnumChatFormatting.BOLD+EnumChatFormatting.GRAY + "/nef help"+EnumChatFormatting.RESET+EnumChatFormatting.GRAY+" - Display all commands from NEF"));
            } else {
                try {
                    NotEnoughFakepixel.config.openGui();
                } catch (Exception e) { sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Usage: " + EnumChatFormatting.RESET + EnumChatFormatting.BOLD+EnumChatFormatting.GRAY + "/nef category "+EnumChatFormatting.RESET+EnumChatFormatting.GRAY+"- Enable/disable one entire category. Possible categories: qol, dungeons, fishing, diana, slayer, experimentation, mining, crimson.\n        " + EnumChatFormatting.BOLD+EnumChatFormatting.GRAY + "/nef default "+EnumChatFormatting.RESET+EnumChatFormatting.GRAY+"- Resets all settings to default\n        " + EnumChatFormatting.BOLD+EnumChatFormatting.GRAY + "/nef help"+EnumChatFormatting.RESET+EnumChatFormatting.GRAY+" - Display all commands from NEF"));}
            }
        } else if(args.length == 1){
            switch (args[0]) {
                case "help":
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Usage: "+EnumChatFormatting.RESET+EnumChatFormatting.BOLD+EnumChatFormatting.GRAY+"/nef category "+EnumChatFormatting.RESET+EnumChatFormatting.GRAY+"- Enable/disable one entire category. Possible categories: qol, dungeons, fishing, diana, slayer, experimentation, mining, crimson.\n        "+EnumChatFormatting.BOLD+EnumChatFormatting.GRAY+"/nef default "+EnumChatFormatting.RESET+EnumChatFormatting.GRAY+"- Resets all settings to default\n        "+EnumChatFormatting.BOLD+EnumChatFormatting.GRAY+"/nef help"+EnumChatFormatting.RESET+EnumChatFormatting.GRAY+" - Display all commands from NEF"));
                    break;
                case "qol":
                    Configuration._qol = !Configuration._qol;
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "QOL configuration changed to " + Configuration._qol));
                    Configuration.customFilters = Configuration._qol;
                    Configuration.disableHyperionExplosions = Configuration._qol;
                    Configuration.disableThunderlordBolt = Configuration._qol;
                    Configuration.fullbright = Configuration._qol;
                    Configuration.noHurtCam = Configuration._qol;
                    Configuration.cropsHeight = Configuration._qol;
                    Configuration.disablePotionEffects = Configuration._qol;
                    Configuration.disableRain = Configuration._qol;
                    Configuration.shortcutWardrobe = Configuration._qol;
                    Configuration.showEnchantLevel = Configuration._qol;
                    Configuration.middleClickChests = Configuration._qol;
                    Configuration.showBestUpgrade = Configuration._qol;
                    Configuration.chocolateEggWaypoints = Configuration._qol;
                    Configuration.showPetEquipped = Configuration._qol;
                    Configuration.disableWatchdogInfo = Configuration._qol;
                    Configuration.disableFriendJoin = Configuration._qol;
                    Configuration.disableSellingRanks = Configuration._qol;
                    Configuration.disableZombieRareDrops = Configuration._qol;
                    Configuration.scrollableTooltips = Configuration._qol;
                    Configuration.disableJerryChineGunSounds = Configuration._qol;
                    Configuration.disableAoteSounds = Configuration._qol;
                    Configuration.disableMidaStaffAnimation = Configuration._qol;
                    Configuration.dmgCommas = Configuration._qol;
                    Configuration.dmgFormatter = Configuration._qol;
                    break;
                case "dungeons":
                    Configuration._dungeons = !Configuration._dungeons;
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Dungeon configuration changed to " + Configuration._dungeons));
                    Configuration.autoReadyDungeon = Configuration._dungeons;
                    Configuration.autoCloseChests = Configuration._dungeons;
                    Configuration.starredMobs = Configuration._dungeons;
                    Configuration.batMobs = Configuration._dungeons;
                    Configuration.felMob = Configuration._dungeons;
                    Configuration.threeWeirdos = Configuration._dungeons;
                    Configuration.dungeonsMap = Configuration._dungeons;
                    Configuration.startsWith = Configuration._dungeons;
                    Configuration.clickInOrder = Configuration._dungeons;
                    Configuration.selectColors = Configuration._dungeons;
                    //Configuration.secretOverlay = Configuration._dungeons;
                    Configuration.sPlusReminder = Configuration._dungeons;
                    break;
                case "fishing":
                    Configuration._fishing = !Configuration._fishing;
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Fishing configuration changed to " + Configuration._fishing));
                    Configuration.legendaryCreatures = Configuration._fishing;
                    Configuration.greatCatch = Configuration._fishing;
                    Configuration.trophyFish = Configuration._fishing;
                    break;
                case "diana":
                    Configuration._diana = !Configuration._diana;
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Diana configuration changed to " + Configuration._diana));
                    Configuration.dianaShowWaypointsBurrows = Configuration._diana;
                    Configuration.dianaGaiaConstruct = Configuration._diana;
                    Configuration.dianaSiamese = Configuration._diana;
                    Configuration.dianaMinosInquisitorAlert = Configuration._diana;
                    Configuration.dianaWaypointSounds = Configuration._diana;
                    Configuration.disableDianaExplosionSounds = Configuration._diana;
                    Configuration.dianaCancelCooldownSpadeMessage = Configuration._diana;
                    break;
                case "slayer":
                    Configuration._slayer = !Configuration._slayer;
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Slayer configuration changed to " + Configuration._slayer));
                    Configuration.slayerMinibosses = Configuration._slayer;
                    Configuration.slayerBosses = Configuration._slayer;
                    Configuration.slayerShowBeaconPath = Configuration._slayer;
                    break;
                case "experimentation":
                    Configuration._enchanting = !Configuration._enchanting;
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Experimentation configuration changed to " + Configuration._enchanting));
                    Configuration.chronomatronSolver = Configuration._enchanting;
                    Configuration.ultraSequencerSolver = Configuration._enchanting;
                    Configuration.preventMissclicksExperimentation = Configuration._enchanting;
                    break;
                case "mining":
                    Configuration._mining = !Configuration._mining;
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Mining configuration changed to " + Configuration._mining));
                    Configuration.miningAbilityNotifier = Configuration._mining;
                    Configuration.disableDonEspresso = Configuration._mining;
                    Configuration.drillFix = Configuration._mining;
                    Configuration.puzzlerSolver = Configuration._mining;
                    Configuration.showGhosts = Configuration._mining;
                    Configuration.drillFuel = Configuration._mining;
                    Configuration.mithrilPowder = Configuration._mining;
                    Configuration.abilityCooldown = Configuration._mining;
                    //Configuration.miningOverlayEnabled = Configuration._mining;
                    break;
                case "crimson":
                    Configuration._crimson = !Configuration._crimson;
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Crimson configuration changed to " + Configuration._crimson));
                    Configuration.bladesoulNotifier = Configuration._crimson;
                    Configuration.mageOutlawNotifier = Configuration._crimson;
                    Configuration.ashfangNotifier = Configuration._crimson;
                    Configuration.barbarianDukeXNotifier = Configuration._crimson;
                    Configuration.ashfangWaypoint = Configuration._crimson;
                    Configuration.gravityOrbWaypoint = Configuration._crimson;
                    Configuration.ashfangHitboxes = Configuration._crimson;
                    Configuration.ashfangMuteChat = Configuration._crimson;
                    Configuration.ashfangMuteSound = Configuration._crimson;
                    Configuration.ashfangHurtSound = Configuration._crimson;
                    //Configuration.ashfangOverlay = Configuration._crimson;
                    break;
                case "default":
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Configuration changed to default"));
                    // QOL
                    Configuration._qol = true;
                    Configuration.customFilters = true;
                    Configuration.disableHyperionExplosions = true;
                    Configuration.disableThunderlordBolt = true;
                    Configuration.fullbright = true;
                    Configuration.noHurtCam = true;
                    Configuration.cropsHeight = false;
                    Configuration.disablePotionEffects = true;
                    Configuration.disableRain = true;
                    Configuration.shortcutWardrobe = true;
                    Configuration.showEnchantLevel = true;
                    Configuration.middleClickChests = true;
                    Configuration.showBestUpgrade = true;
                    Configuration.chocolateEggWaypoints = true;
                    Configuration.showPetEquipped = true;
                    Configuration.disableWatchdogInfo = false;
                    Configuration.disableFriendJoin = false;
                    Configuration.chatCleaner = false;
                    Configuration.disableSellingRanks = false;
                    Configuration.disableZombieRareDrops = true;
                    Configuration.scrollableTooltips = true;
                    Configuration.disableJerryChineGunSounds = true;
                    Configuration.disableAoteSounds = false;
                    Configuration.disableMidaStaffAnimation = false;
                    Configuration.dmgCommas = false;
                    Configuration.dmgFormatter = false;
                    // Dungeons
                    Configuration._dungeons = true;
                    Configuration.autoReadyDungeon = true;
                    Configuration.autoCloseChests = true;
                    Configuration.autoDropItems = false;
                    Configuration.starredMobs = true;
                    Configuration.batMobs = true;
                    Configuration.felMob = true;
                    Configuration.threeWeirdos = true;
                    Configuration.dungeonsMap = true;
                    Configuration.startsWith = true;
                    Configuration.clickInOrder = true;
                    Configuration.selectColors = true;
                    Configuration.secretOverlay = false;
                    Configuration.sPlusReminder = true;
                    // Fishing
                    Configuration._fishing = true;
                    Configuration.legendaryCreatures = true;
                    Configuration.greatCatch = true;
                    Configuration.trophyFish = true;
                    // Diana
                    Configuration.dianaShowWaypointsBurrows = true;
                    Configuration.dianaGaiaConstruct = true;
                    Configuration.dianaSiamese = false;
                    Configuration.dianaMinosInquisitorAlert = true;
                    Configuration.dianaWaypointSounds = true;
                    Configuration.disableDianaExplosionSounds = false;
                    Configuration.dianaCancelCooldownSpadeMessage = true;
                    // Slayer
                    Configuration._slayer = true;
                    Configuration.slayerMinibosses = true;
                    Configuration.slayerBosses = true;
                    Configuration.slayerShowBeaconPath = true;
                    // Experimentation Table
                    Configuration._enchanting = true;
                    Configuration.chronomatronSolver = true;
                    Configuration.ultraSequencerSolver = true;
                    Configuration.preventMissclicksExperimentation = true;
                    // Mining
                    Configuration._mining = true;
                    Configuration.miningAbilityNotifier = true;
                    Configuration.disableDonEspresso = true;
                    Configuration.drillFix = true;
                    Configuration.puzzlerSolver = true;
                    Configuration.showGhosts = true;
                    Configuration.drillFuel = true;
                    Configuration.mithrilPowder = true;
                    Configuration.abilityCooldown = true;
                    Configuration.miningOverlayEnabled = false;
                    // Crimson
                    Configuration._crimson = true;
                    Configuration.bladesoulNotifier = true;
                    Configuration.mageOutlawNotifier = true;
                    Configuration.ashfangNotifier = true;
                    Configuration.barbarianDukeXNotifier = true;
                    Configuration.ashfangWaypoint = true;
                    Configuration.gravityOrbWaypoint = true;
                    Configuration.ashfangHitboxes = true;
                    Configuration.ashfangMuteChat = true;
                    Configuration.ashfangMuteSound = true;
                    Configuration.ashfangHurtSound = true;
                    Configuration.ashfangOverlay = false;
            }
        }
    }
}
