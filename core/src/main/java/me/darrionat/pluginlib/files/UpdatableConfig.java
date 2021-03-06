package me.darrionat.pluginlib.files;

import me.darrionat.pluginlib.Plugin;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Represents a {@link Config} that can be updated to the default config within a {@link Plugin}.
 */
public interface UpdatableConfig extends Config {
    /**
     * Sync the saved file with the default settings. Removes all YAML comments. Does nothing if there is no default
     * resource within the {@link Plugin}.
     *
     * @see FileConfiguration#set(String, Object)
     */
    void sync();
}