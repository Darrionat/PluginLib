package me.darrionat.pluginlib;

import me.darrionat.pluginlib.guis.GuiHandler;

public interface IPlugin {
    /**
     * Ran when the plugin is enabled.
     */
    void initPlugin();

    /**
     * Gets the plugin's way of handling errors.
     *
     * @return Returns the {@link ErrorHandler} for the plugin.
     */
    ErrorHandler getErrorHandler();

    /**
     * Gets the plugin's method of handling gui.
     *
     * @return Returns the {@link GuiHandler} for the plugin.
     */
    GuiHandler getGuiHandler();

    /**
     * Logs a {@link String} into console with formatting. Use {@code &} for color codes.
     *
     * @param s The message to print out.
     */
    void log(String s);
}