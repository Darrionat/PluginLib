package me.darrionat.pluginlib.files;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * Represents a {@link File} and {@link FileConfiguration} pair.
 */
public interface Config {
    /**
     * Gets the {@link File} that the {@link Config} belongs to.
     *
     * @return Returns the file of the config.
     */
    File getFile();

    /**
     * Loads the {@link FileConfiguration} that the {@link Config} belongs to.
     *
     * @return Returns the FileConfiguration of the config.
     */
    FileConfiguration getFileConfiguration();

    /**
     * Saves the {@link Config} with a newer {@link FileConfiguration}.
     *
     * @param newConfig The new settings to replace the older settings with.
     */
    void save(FileConfiguration newConfig);

    /**
     * Tests whether the {@link Config} exists or not.
     *
     * @return Returns {@code true} if the {@link File} of the config exists.
     * @see File#exists()
     */
    boolean exists();

    /**
     * Attempts to delete the {@link Config}.
     *
     * @see File#delete()
     */
    void delete();
}