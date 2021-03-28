package me.darrionat.pluginlib;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.darrionat.pluginlib.commands.SubCommand;

/**
 * The way that errors will be handled within the plugin. Each {@link Plugin}
 * requires an {@link ErrorHandler}.
 * 
 * @see Plugin#getErrorHandler()
 */
public interface ErrorHandler {
	/**
	 * Ran when a {@link Player} does not have a permission.
	 * 
	 * @param p          The player who lacks a permission.
	 * @param permission The permission the player does not have.
	 * @see Player#hasPermission(String)
	 */
	public void noPermissionError(Player p, String permission);

	/**
	 * Ran when a {@link CommandSender} attempts to use a command that only
	 * {@link Player}s can run.
	 * 
	 * @param sender The command sender.
	 */
	public void onlyPlayerCommandError(CommandSender sender);

	/**
	 * Ran when a {@link CommandSender} attempts to run a {@link SubCommand} without
	 * providing enough arguments.
	 * 
	 * @param subCommand The command the sender ran.
	 * @param sender     The sender of the command.
	 * @see SubCommand#getRequiredArgs()
	 */
	public void notEnoughArguments(SubCommand subCommand, CommandSender sender);
}