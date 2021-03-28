package me.darrionat.pluginlib.guis;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import me.darrionat.pluginlib.Plugin;

/**
 * The way that {@link Gui}s are handled within a {@link Plugin}.
 * 
 * @see Plugin#getGuiHandler()
 */
public interface GuiHandler {
	/**
	 * Registers a {@link Gui} and its click event.
	 * 
	 * @param gui The gui to register.
	 * @see Gui#clicked(Player, int, ClickType)
	 */
	public void registerGui(Gui gui);

	/**
	 * Unregisters a {@link Gui} and its click event.
	 * 
	 * @param gui The gui to unregister.
	 * @see Gui#clicked(Player, int, ClickType)
	 */
	public void unregisterGui(Gui gui);

	/**
	 * Displays a {@link Gui} to a player.
	 * 
	 * @param p   The player to open the gui for.
	 * @param gui The gui to open.
	 */
	public void openGui(Player p, Gui gui);
}