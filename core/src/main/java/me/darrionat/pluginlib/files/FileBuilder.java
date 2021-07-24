package me.darrionat.pluginlib.files;

import me.darrionat.pluginlib.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * A utility to create a {@link File}.
 */
public abstract class
FileBuilder {
    /**
     * The plugin the file being built belongs to.
     */
    protected final Plugin plugin;
    /**
     * The name of the {@link File}.
     */
    protected final String name;

    /**
     * Creates a new {@link FileBuilder}.
     *
     * @param plugin   The plugin the file being built belongs to.
     * @param fileName The name of the {@link File}.
     * @see FileBuilder#getFile()
     * @see FileBuilder#createFile()
     */
    public FileBuilder(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.name = fileName;
    }

    /**
     * Gets the {@link File} that this {@link FileBuilder} represents but does not create the file.
     *
     * @return returns the file that is located within the {@link Plugin} data folder.
     * @see FileBuilder#createFile()
     * @see Plugin#getDataFolder()
     */
    public File getFile() {
        return new File(plugin.getDataFolder(), name);
    }

    /**
     * Determines if the file exists already.
     *
     * @return {@code true} if the file exists; {@code false} otherwise.
     */
    public boolean exists() {
        return getFile().exists();
    }

    /**
     * Creates the {@link File} within the data folder of the {@link Plugin}. Does nothing if the file already exists.
     */
    public void createFile() {
        File file = getFile();
        try {
            file.createNewFile();
            plugin.log("Saving " + file.getName());
        } catch (IOException exe) {
            plugin.log("Failed to create " + file.getName());
            exe.printStackTrace();
        }
    }
}