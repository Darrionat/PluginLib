package me.darrionat.pluginlib.commands;

import me.darrionat.pluginlib.ErrorHandler;
import me.darrionat.pluginlib.Plugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The {@link SubCommand} represents a command that has arguments. SubCommands require to be within a parent command,
 * {@link BaseCommand}.
 *
 * @see SubCommand#run(CommandSender, String[])
 */
public abstract class SubCommand {
    /**
     * The {@link ErrorHandler} of the plugin.
     */
    private final ErrorHandler errorHandler;
    /**
     * The parent command.
     */
    private final BaseCommand parentCommand;
    /**
     * The permission of this SubCommand.
     */
    private final String permission;

    /**
     * Creates a new {@link SubCommand} object and adds it the the {@link BaseCommand}'s subcommands.
     *
     * @param parentCommand The parent command.
     * @param plugin        The plugin that the subcommand belongs to.
     */
    public SubCommand(BaseCommand parentCommand, Plugin plugin) {
        this.parentCommand = parentCommand;
        this.errorHandler = plugin.getErrorHandler();
        this.permission = parentCommand.getCommandLabel() + "." + getSubCommand();
        parentCommand.addSubCommand(this);
    }

    /**
     * Gets the parent command.
     *
     * @return Returns the {@link BaseCommand} that this {@link SubCommand} belongs to.
     */
    public BaseCommand getParentCommand() {
        return parentCommand;
    }

    /**
     * Gets the argument of the {@link SubCommand} represented as {@link String}.
     *
     * @return Returns the subcommand.
     */
    public abstract String getSubCommand();

    /**
     * Gets the permission that is required to run this {@link SubCommand}.
     *
     * @return Returns the permission of the subcommand.
     * @see Player#hasPermission(String)
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Ran when the parent {@link BaseCommand} is run and has this {@link SubCommand}.
     *
     * @param sender The person who ran the command.
     * @param args   The arguments of the command. {@link SubCommand#getSubCommand()} will always be equal to {@code
     *               args[0]}
     */
    public void run(CommandSender sender, String[] args) {
        if (args.length < getRequiredArgs()) {
            errorHandler.notEnoughArguments(this, sender);
            return;
        }

        if (!(sender instanceof Player) && onlyPlayers()) {
            errorHandler.onlyPlayerCommandError(sender);
            return;
        }

        if (sender instanceof Player p) {
            if (!p.hasPermission(permission)) {
                errorHandler.noPermissionError(p, permission);
                return;
            }
        }
        runCommand(sender, args);
    }

    /**
     * Gets the amount of arguments this {@link SubCommand} needs to be ran.
     *
     * @return Returns an amount of arguments that is required for the command to be ran. This amount includes the
     * {@link SubCommand} itself as an argument.
     * @see #getSubCommand()
     */
    public abstract int getRequiredArgs();

    /**
     * Gets if the {@link SubCommand} can be ran by {@link CommandSender}s that are not {@link Player}s.
     *
     * @return Returns {@code true} if only {@link Player}s can run this command.
     */
    public abstract boolean onlyPlayers();

    /**
     * Executes the {@link SubCommand} and its effects.
     *
     * @param sender The person who ran the command.
     * @param args   The arguments of the command.
     */
    protected abstract void runCommand(CommandSender sender, String[] args);
}