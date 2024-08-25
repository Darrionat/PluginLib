package me.darrionat.pluginlib.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a simple tab completer for a {@link BaseCommand}.
 * <p>
 * Tab completion is based upon the subcommands of the command.
 * <p>
 * Autofill is implemented within the tab completer.
 */
public class CommandTabCompleter implements TabCompleter {
    private final BaseCommand command;

    /**
     * Constructs a new {@code CommandTabCompleter}.
     * <p>
     * The tab completer uses autofill and is based off of the subcommands of the given command.
     *
     * @param command The command to autofill for.
     */
    public CommandTabCompleter(BaseCommand command) {
        this.command = command;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (sender instanceof Player p) {
            if (!command.playerHasPermission(p))
                return null;
        }
        List<String> list = new ArrayList<>();

        // If user is typing out first argument, give them all possible subcommands
        if (args.length == 1) {
            list.addAll(command.getSubCommands());
        }

        // If user is typing out a subcommand, ask subcommand for tab complete
        if (args.length >= 2) {
            String subCommandString = args[0];
            SubCommand subCommand = command.getSubCommand(subCommandString);
            // Ignores players without permission
            if (sender instanceof Player p) {
                if (!subCommand.playerHasPermission(p)) {
                    return null;
                }
            }
            // Get tab completion from subcommand logic
            List<String> subTabComplete = subCommand.getTabComplete(args);
            if (subTabComplete != null && !subTabComplete.isEmpty())
                list.addAll(subTabComplete);
        }

        // Removes arguments that the player isn't typing out.
        // This makes the list automatically shorter and adapt to autofill.
        List<String> finalList = new ArrayList<>();
        for (String s : list) {
            if (!s.toUpperCase().contains(args[args.length - 1].toUpperCase())) {
                continue;
            }
            finalList.add(s.toLowerCase());
        }
        return finalList;
    }
}