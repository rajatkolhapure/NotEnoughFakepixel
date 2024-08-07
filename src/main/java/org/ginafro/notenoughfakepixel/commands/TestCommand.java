package org.ginafro.notenoughfakepixel.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ResourceLocation;
import org.ginafro.notenoughfakepixel.gui.impl.ModMenu;

public class TestCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "nef";
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
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        ModMenu.openGUI();

    }
}
