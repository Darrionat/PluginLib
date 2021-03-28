package me.darrionat.pluginlib.files;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Represents a {@link File} and {@link FileConfiguration} pair.
 */
public interface Config {
	/**
	 * Gets the {@link File} that the {@link Config} belongs to.
	 * 
	 * @return Returns the file of the config.
	 */
	public File getFile();

	/**
	 * Loads the {@link FileConfiguration} that the {@link Config} belongs to.
	 * 
	 * @return Returns the FileConfiguration of the config.
	 */
	public FileConfiguration getFileConfiguration();

	/**
	 * Saves the {@link Config} with a newer {@link FileConfiguration}.
	 * 
	 * @param newConfig The new settings to replace the older settings with.
	 */
	public void save(FileConfiguration newConfig);

	/**
	 * Tests whether the {@link Config} exists or not.
	 * 
	 * @return Returns {@code true} if the {@link File} of the config exists.
	 * @see File#exists()
	 */
	public boolean exists();

	/**
	 * Attempts to delete the {@link Config}.
	 * 
	 * @see File#delete()
	 */
	public void delete();
}