package me.darrionat.pluginlib.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

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
        List<String> list = new ArrayList<>();
        if (args.length == 1)
            command.getSubCommands().forEach(subCommand -> list.add(subCommand.getSubCommand()));

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