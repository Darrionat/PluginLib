package me.darrionat.pluginlib.files;

import java.io.File;
import java.io.IOException;

import me.darrionat.pluginlib.Plugin;

/**
 * A utility to create a {@link File}.
 */
public abstract class FileBuilder {
	/**
	 * The plugin the file being built belongs to.
	 */
	protected Plugin plugin;
	/**
	 * The name of the {@link File}.
	 */
	protected String name;

	/**
	 * Creates a new {@link FileBuilder}.
	 * 
	 * @param plugin   The plugin the file being built belongs to.
	 * @param fileName The name of the {@link File}.
	 * 
	 * @see FileBuilder#getFile()
	 * @see FileBuilder#createFile()
	 */
	public FileBuilder(Plugin plugin, String fileName) {
		this.plugin = plugin;
		this.name = fileName;
	}

	/**
	 * Gets the {@link File} that this {@link FileBuilder} represents but does not
	 * create the file.
	 * 
	 * @return returns the file that is located within the {@link Plugin} data
	 *         folder.
	 * 
	 * @see FileBuilder#createFile()
	 * @see Plugin#getDataFolder()
	 */
	public File getFile() {
		return new File(plugin.getDataFolder(), name);
	}

	/**
	 * Creates the {@link File} within the data folder of the {@link Plugin}. Does
	 * nothing if the file already exists.
	 */
	public void createFile() {
		File file = getFile();
		if (file.exists())
			return;
		try {
			file.createNewFile();
			plugin.log("Saving " + file.getName());
		} catch (IOException exe) {
			plugin.log("Failed to create " + file.getName());
			exe.printStackTrace();
		}
	}
}