package me.darrionat.pluginlib.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.darrionat.pluginlib.Plugin;

/**
 * Represents an {@link UpdateableConfig}.
 */
public class LocalConfig implements UpdateableConfig {
	/**
	 * The {@link Plugin} that this config belongs to
	 */
	private Plugin plugin;
	/**
	 * The File of the config
	 */
	private File file;

	/**
	 * Creates a new {@link Config}. This should be created with the
	 * {@link ConfigBuilder} class.
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
		FileConfiguration config = getFileConfiguration();
		YamlConfiguration jarConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(is));

		for (String key : jarConfig.getKeys(true))
			if (!config.contains(key)) {
				config.createSection(key);
				config.set(key, jarConfig.get(key));
			}

		for (String key : config.getConfigurationSection("").getKeys(true))
			if (!jarConfig.contains(key))
				config.set(key, null);

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