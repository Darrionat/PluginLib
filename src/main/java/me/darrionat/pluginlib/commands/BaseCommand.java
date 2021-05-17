package me.darrionat.pluginlib.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import me.darrionat.pluginlib.ErrorHandler;
import me.darrionat.pluginlib.Plugin;

/**
 * The {@link BaseCommand} class represents a registered {@link PluginCommand}.
 */
public abstract class BaseCommand implements CommandExecutor, HeritableCommand {
    private ErrorHandler errorHandler;
    private String permission;
    private List<SubCommand> subCommands = new ArrayList<SubCommand>();

    /**
     * Creates and registers a new {@link BaseCommand} object.
     * 
     * @param plugin The {@link Plugin} that the command will be registered to.
     */
    public BaseCommand(Plugin plugin) {
        this.errorHandler = plugin.getErrorHandler();
        this.permission = getCommandLabel() + ".use";
        plugin.getCommand(getCommandLabel()).setExecutor(this);
    }

    /**
     * {@inheritDoc}
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permission
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission(permission)) {
                errorHandler.noPermissionError(p, permission);
                return true;
            }
        }

        // Subcommands
        if (args.length == 0) {
            runNoArgs(sender, command, label, args);
            return true;
        }

        for (SubCommand subCommand : getSubCommands())
            if (args[0].equalsIgnoreCase(subCommand.getSubCommand())) {
                subCommand.run(sender, args);
                return true;
            }
        // No suitable subcommand
        runNoArgs(sender, command, label, args);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    /**
     * {@inheritDoc}
     */
    public void addSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    /**
     * Gets the label of the {@link BaseCommand}.
     * 
     * @return Returns the label of the command as a {@link String}.
     */
    public abstract String getCommandLabel();

    /**
     * Ran when the {@link BaseCommand} has no {@link SubCommand}s that were run.
     * 
     * @param sender  the sender of the command
     * @param command the command that was ran
     * @param label   the label of the command
     * @param args    the arguments that were used
     */
    protected abstract void runNoArgs(CommandSender sender, Command command, String label, String[] args);
}