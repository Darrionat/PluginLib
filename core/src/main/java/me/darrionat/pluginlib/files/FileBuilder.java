package me.darrionat.pluginlib.files;

import me.darrionat.pluginlib.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * A utility to create a {@link java.io.File}.
 */
public abstract class FileBuilder {
    /**
     * The plugin the file being built belongs to.
     */
    protected final Plugin plugin;
    /**
     * The name of the {@link java.io.File}.
     */
    protected final String name;

    /**
     * The subdirectory of the plugin that the file will be in.
     */
    protected final String directory;

    /**
     * Creates a new {@link FileBuilder}.
     *
     * @param plugin   The plugin the file being built belongs to.
     * @param fileName The name of the {@link java.io.File}.
     * @see FileBuilder#getFile()
     * @see FileBuilder#createFile()
     */
    public FileBuilder(Plugin plugin, String fileName) {
        this(plugin, fileName, null);
    }

    /**
     * Creates a new {@link FileBuilder}.
     *
     * @param plugin       The plugin the file being built belongs to.
     * @param fileName     The name of the {@link java.io.File}.
     * @param subDirectory A subdirectory within the plugin's folder to contain the file.
     * @see FileBuilder#getFile()
     * @see FileBuilder#createFile()
     */
    public FileBuilder(Plugin plugin, String fileName, String subDirectory) {
        this.plugin = plugin;
        this.name = fileName;
        this.directory = subDirectory;
    }

    /**
     * Gets the {@link File} that this {@link FileBuilder} represents but does not create the file.
     *
     * @return returns the file that is located within the {@link Plugin} data folder.
     * @see FileBuilder#createFile()
     * @see Plugin#getDataFolder()
     */
    public File getFile() {
        if (directory == null) return new File(plugin.getDataFolder(), name);
        else return new File(plugin.getDataFolder() + File.separator + directory, name);
    }

    /**
     * Determines if the file exists already.
     *
     * @return {@code true} if the file exists; {@code false} otherwise.
     */
    public boolean exists() {
        return getFile().exists();
    }

    private void setupSubdirectory() {
        File dir = new File(plugin.getDataFolder() + File.separator + directory);
        // Only attempt to create if it doesn't already exist
        if (!dir.exists()) {
            if (dir.mkdir()) {
                plugin.log("Plugin folder " + directory + " created");
            } else {
                plugin.log("Failed to create " + directory + " directory inside of plugin folder");
            }
        }
    }

    /**
     * Creates the {@link File} within the data folder of the {@link Plugin}. Does nothing if the file already
     * exists.
     */
    public void createFile() {
        if (directory != null) {
            setupSubdirectory();
        }
        File file = getFile();
        try {
            file.createNewFile();
            plugin.log("Created " + file.getName());
        } catch (IOException exe) {
            plugin.log("Failed to create " + file.getName());
            exe.printStackTrace();
        }
    }
}