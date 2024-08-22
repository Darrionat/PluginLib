package me.darrionat.pluginlib.files;

import me.darrionat.pluginlib.Plugin;

import java.io.File;

/**
 * A utility to create a {@link Config}.
 */
public class ConfigBuilder extends FileBuilder {
    /**
     * Sync the saved file with the default settings. Removes all YAML comments.
     */
    private boolean update = false;
    /**
     * Whether this is a default resource in the plugin or not. This determines how the config will be created.
     */
    private boolean builtIn = false;

    /**
     * Creates a {@link ConfigBuilder} object which can be used to create {@link Config} objects.
     *
     * @param plugin   The plugin the {@link Config} being built belongs to.
     * @param fileName The name of the {@link File} that the config represents.
     */
    public ConfigBuilder(Plugin plugin, String fileName) {
        super(plugin, fileName);
    }

    /**
     * Changes the default method of creating a new file to using a default resource within the {@link Plugin}.
     *
     * @return Returns the {@link ConfigBuilder}.
     * @see Plugin#saveResource(String, boolean)
     */
    public ConfigBuilder useBuiltInFile() {
        builtIn = true;
        return this;
    }

    /**
     * Sets the {@link Config} to update to the default settings.
     *
     * @return Returns the {@link ConfigBuilder}.
     */
    public ConfigBuilder updateConfig() {
        this.update = true;
        return this;
    }

    /**
     * Builds the {@link Config}.
     * <p>
     * The config will be built from a built in resource instead of creating a new {@link File} if {@link
     * #useBuiltInFile()} was ran.
     * <p>
     * The config will be updated to the newest resource located within the {@link Plugin} if {@link #updateConfig()}
     * was ran.
     *
     * @return Returns the config which has been created within the {@link Plugin}'s directory.
     * @see ConfigBuilder#useBuiltInFile
     * @see ConfigBuilder#updateConfig
     */
    public Config build() {
        if (!exists()) {
            if (builtIn) {
                plugin.saveResource(name, false);
            } else {
                createFile();
            }
        }
        LocalConfig config = new LocalConfig(plugin, getFile());
        if (update)
            config.sync();
        return config;
    }
}