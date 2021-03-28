package me.darrionat.pluginlib.files;

import org.bukkit.configuration.file.FileConfiguration;

import me.darrionat.pluginlib.Plugin;

/**
 * Represents a {@link Config} that can be updated to the default config within
 * a {@link Plugin}.
 */
public interface UpdateableConfig extends Config {
	/**
	 * Sync the saved file with the default settings. Removes all YAML comments.
	 * Does nothing if there is no default resource within the {@link Plugin}.
	 * 
	 * @see FileConfiguration#set(String, Object)
	 */
	public void sync();
}