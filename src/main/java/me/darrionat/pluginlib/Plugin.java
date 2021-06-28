package me.darrionat.pluginlib;

import me.darrionat.pluginlib.guis.GuiHandler;
import me.darrionat.pluginlib.guis.GuiManager;
import me.darrionat.pluginlib.utils.SpigotMCUpdateHandler;
import me.darrionat.pluginlib.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents a {@link JavaPlugin}.
 */
public abstract class Plugin extends JavaPlugin implements IPlugin {
    private static Plugin instance;

    private GuiHandler guiHandler;

    /**
     * Gets the current project of the {@link Plugin}.
     *
     * @return Returns an instance of this {@link Plugin}.
     */
    public static Plugin getProject() {
        return instance;
    }

    /**
     * Ran when the plugin is enabled. This method cannot be overridden by a
     * subclass.
     *
     * @see Plugin#initPlugin()
     */
    @Override
    public final void onEnable() {
        instance = this;
        guiHandler = new GuiManager(this);
        initPlugin();
    }

    /**
     * {@inheritDoc}
     */
    public final GuiHandler getGuiHandler() {
        return guiHandler;
    }

    /**
     * Creates a new {@link SpigotMCUpdateHandler}.
     *
     * @param resourceId The project id of the resource on SpigotMC.
     * @return Returns a {@link SpigotMCUpdateHandler} for this plugin.
     */
    public final SpigotMCUpdateHandler buildUpdateChecker(int resourceId) {
        return new SpigotMCUpdateHandler(this, resourceId);
    }

    /**
     * {@inheritDoc}
     */
    public void log(String s) {
        System.out.println(Utils.chat("[" + getName() + "] " + s));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void onDisable();
}