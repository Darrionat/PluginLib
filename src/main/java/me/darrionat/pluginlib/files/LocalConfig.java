package me.darrionat.pluginlib.files;

import me.darrionat.pluginlib.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Represents an {@link UpdatableConfig}.
 */
public class LocalConfig implements UpdatableConfig {
    /**
     * The {@link Plugin} that this config belongs to
     */
    private final Plugin plugin;
    /**
     * The File of the config
     */
    private final File file;

    /**
     * Creates a new {@link Config}. This should be created with the {@link ConfigBuilder} class.
     *
     * @param plugin The {@link Plugin} that this config belongs to.
     * @param file   The File of the {@link Config}.
     */
    public LocalConfig(Plugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;
    }

    /**
     * {@inheritDoc}
     */
    public File getFile() {
        return file;
    }

    /**
     * {@inheritDoc}
     */
    public FileConfiguration getFileConfiguration() {
        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * {@inheritDoc}
     */
    public void save(FileConfiguration newConfig) {
        try {
            newConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sync() {
        InputStream is = plugin.getResource(file.getName());
        if (is == null)
            return;
        // The saved file
        FileConfiguration config = getFileConfiguration();
        // The file built in within the .jar
        YamlConfiguration jarConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(is));

        // Add all default keys to the config if nonexistent
        for (String key : jarConfig.getKeys(true))
            if (!config.contains(key)) {
                config.createSection(key);
                config.set(key, jarConfig.get(key));
            }

        // Remove all non-default keys from the config
        for (String key : config.getConfigurationSection("").getKeys(true))
            if (!jarConfig.contains(key))
                config.set(key, null);

        // Adds the version for debug purposes
        config.set("version", plugin.getDescription().getVersion());
        save(config);
    }

    /**
     * {@inheritDoc}
     */
    public boolean exists() {
        return file.exists();
    }

    /**
     * {@inheritDoc}
     */
    public void delete() {
        file.delete();
    }
}