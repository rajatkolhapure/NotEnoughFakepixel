package org.ginafro.notenoughfakepixel.features.skyblock;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class EntityInfoCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "entityinfo";
    }


    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return super.addTabCompletionOptions(sender, args, pos);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args[0] == null){
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Please Enter Entity Name"));
        }
    }
}
