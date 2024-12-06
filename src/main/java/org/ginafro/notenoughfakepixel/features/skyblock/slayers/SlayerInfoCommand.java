package org.ginafro.notenoughfakepixel.features.skyblock.slayers;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;

import java.util.ArrayList;
import java.util.List;

public class SlayerInfoCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "slayerinfo";
    }

    List<String> options = new ArrayList<>();

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + "<Entity> ";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        options.add(SlayerInfoGUI.ENDERMAN);
        options.add(SlayerInfoGUI.BLAZE);
        options.add(SlayerInfoGUI.ZOMBIE);
        options.add(SlayerInfoGUI.SPIDER);
        options.add(SlayerInfoGUI.WOLF);
        return options;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args[0] == null){
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Please write a boss name"));
            return;
        }
        NotEnoughFakepixel.openGui = new SlayerInfoGUI(args[0]);
    }
}
