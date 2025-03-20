package org.ginafro.notenoughfakepixel.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class Waypoint extends CommandBase {
    @Override
    public String getCommandName() {
        return "fairysouls";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/fairysouls <show/hide>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        String action = args[0].toLowerCase();
        if (!action.equals("show") && !action.equals("hide")) {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        boolean show = action.equals("show");
        // Toggle fairy soul waypoints visibility
        // Implementation will depend on your waypoint system
        sender.addChatMessage(new ChatComponentText(
            EnumChatFormatting.GREEN + "Fairy Soul waypoints " + 
            (show ? "enabled" : "disabled")
        ));
    }
}
