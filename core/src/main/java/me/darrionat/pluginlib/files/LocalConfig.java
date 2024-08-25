package me.darrionat.pluginlib.files;

import me.darrionat.pluginlib.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

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
        if (is == null) return;
        // The saved file
        FileConfiguration config = getFileConfiguration();
        // The file built in within the .jar
        YamlConfiguration jarConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(is));

        // Add all default keys to the config if nonexistent
        for (String keyToAdd : keysToBeAdded(config, jarConfig)) {
            config.createSection(keyToAdd);
            config.set(keyToAdd, jarConfig.get(keyToAdd));
        }
        for (String keyToRemove : keysToBeRemoved(config, jarConfig)) {
            config.set(keyToRemove, null);
        }

        // Adds the version for debug purposes
        config.set("version", plugin.getDescription().getVersion());
        save(config);
    }

    /**
     * Compares the currently saved config and the config saved in the plugin .jar,
     * and returns the keys that are present in the saved config but not present in the plugin's JAR file.
     *
     * @param savedConfig The config currently saved in the plugin's folder.
     * @param jarConfig   The config stored in the plugin's Java archive file.
     * @return Returns the keys that are present in the saved config and not present in the config present in the plugin's JAR file.
     */
    private Set<String> keysToBeRemoved(FileConfiguration savedConfig, FileConfiguration jarConfig) {
        Set<String> savedConfigKeys = savedConfig.getKeys(true);
        Set<String> jarConfigKeys = jarConfig.getKeys(true);
        // Set difference: savedConfigKeys - jarConfigKeys
        savedConfigKeys.removeAll(jarConfigKeys);
        return savedConfigKeys;
    }

    /**
     * Compares the currently saved config and the config saved in the plugin .jar,
     * and returns the keys that are not present in the saved config but are present in the plugin's JAR file.
     *
     * @param savedConfig The config currently saved in the plugin's folder.
     * @param jarConfig   The config stored in the plugin's Java archive file.
     * @return Returns the keys that are not present in the saved config but are present in the plugin's JAR file.
     */
    private Set<String> keysToBeAdded(FileConfiguration savedConfig, FileConfiguration jarConfig) {
        Set<String> savedConfigKeys = savedConfig.getKeys(true);
        Set<String> jarConfigKeys = jarConfig.getKeys(true);
        // Set difference: jarConfigKeys - savedConfigKeys
        jarConfigKeys.removeAll(savedConfigKeys);
        return jarConfigKeys;
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