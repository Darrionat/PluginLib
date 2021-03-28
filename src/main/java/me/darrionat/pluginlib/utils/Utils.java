package me.darrionat.pluginlib.utils;

import org.bukkit.ChatColor;

/**
 * Small useful methods that don't belong anywhere else.
 */
public class Utils {
	/**
	 * Formats chat to contain color codes using the {@code &}.
	 * 
	 * @param toColor The {@link String} to convert.
	 * @return Returns a formatted string with color.
	 */
	public static String chat(String toColor) {
		return ChatColor.translateAlternateColorCodes('&', toColor);
	}
}