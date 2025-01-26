package org.ginafro.notenoughfakepixel.commands;
;
import cc.polyfrost.oneconfig.utils.gui.GuiUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;

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
        NotEnoughFakepixel.config.openGui();
    }
}
