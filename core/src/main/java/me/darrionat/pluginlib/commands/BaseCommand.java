package me.darrionat.pluginlib.commands;

import me.darrionat.pluginlib.ErrorHandler;
import me.darrionat.pluginlib.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The {@link BaseCommand} class represents a registered {@link PluginCommand}.
 */
public abstract class BaseCommand implements CommandExecutor, HeritableCommand {
    private final ErrorHandler errorHandler;
    private final String permission;
    //    private final List<SubCommand> subCommands = new ArrayList<>();
    private final Map<String, SubCommand> subCommandMap = new HashMap<>();

    /**
     * Creates and registers a new {@link BaseCommand} object.
     *
     * @param plugin The {@link Plugin} that the command will be registered to.
     */
    public BaseCommand(Plugin plugin) {
        this.errorHandler = plugin.getErrorHandler();
        this.permission = getCommandLabel() + ".use";
        PluginCommand command = plugin.getCommand(getCommandLabel());
        command.setExecutor(this);
        command.setTabCompleter(new CommandTabCompleter(this));
    }

    /**
     * {@inheritDoc}
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permission
        if (sender instanceof Player p) {
            if (!playerHasPermission(p)) {
                errorHandler.noPermissionError(p, permission);
                return true;
            }
        }

        // Subcommands
        if (args.length == 0) {
            runNoArgs(sender, command, label, args);
            return true;
        }

        if (subCommandMap.containsKey(args[0])) {
            subCommandMap.get(args[0]).run(sender, args);
        } else {
            // No suitable subcommand
            runNoArgs(sender, command, label, args);
        }
        return true;
    }

    /**
     * Checks whether or not the player has permission for this command.
     *
     * @param p The player.
     * @return Returns {@code true} if the player has permission to use this command, {@code false} otherwise.
     */
    public boolean playerHasPermission(Player p) {
        return p.hasPermission(permission);
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> getSubCommands() {
        return subCommandMap.keySet();
    }

    public SubCommand getSubCommand(String name) {
        return subCommandMap.get(name);
    }

    /**
     * {@inheritDoc}
     */
    public void addSubCommand(SubCommand subCommand) {
        subCommandMap.put(subCommand.getSubCommand(), subCommand);
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