package me.darrionat.pluginlib.commands;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

/**
 * An interface used to define which commands are able to have subcommands.
 */
public interface HeritableCommand {
    /**
     * Gets the {@link List} of {@link SubCommand}s that this command has.
     *
     * @return Returns a list of all subcommands this command has.
     */
    Set<String> getSubCommands();

    /**
     * Adds a {@link SubCommand} to the {@link HeritableCommand}.
     *
     * @param subCommand the subcommand to add.
     */
    void addSubCommand(SubCommand subCommand);
}