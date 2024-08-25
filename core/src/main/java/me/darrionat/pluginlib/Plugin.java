package me.darrionat.pluginlib;

import com.cryptomorin.xseries.XMaterial;
import me.darrionat.pluginlib.bstats.Metrics;
import me.darrionat.pluginlib.enchantments.EnchantmentHandler;
import me.darrionat.pluginlib.enchantments.EnchantmentService;
import me.darrionat.pluginlib.enchantments.LegacyEnchantmentService;
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
    private EnchantmentHandler enchantmentHandler;
    protected Metrics metrics;

    /**
     * Gets the current project of the {@link Plugin}.
     *
     * @return Returns an instance of this {@link Plugin}.
     */
    public static Plugin getProject() {
        return instance;
    }

    /**
     * Ran when the plugin is enabled. This method cannot be overridden by a subclass.
     *
     * @see Plugin#initPlugin()
     */
    @Override
    public final void onEnable() {
        instance = this;
        guiHandler = new GuiManager(this);
        if (legacy())
            enchantmentHandler = new LegacyEnchantmentService();
        else
            enchantmentHandler = new EnchantmentService();
        initPlugin();
    }

    /**
     * {@inheritDoc}
     */
    public final GuiHandler getGuiHandler() {
        return guiHandler;
    }

    public final EnchantmentHandler getEnchantmentHandler() {
        return enchantmentHandler;
    }

    /**
     * Gets the resource id of this project for SpigotMC.
     *
     * @return The project id of the resource on SpigotMC.
     */
    public abstract int getSpigotResourceId();

    /**
     * Gets the resource id on bStats.
     *
     * @return The project id of the resource on bStats
     */
    public abstract int getbStatsResourceId();

    /**
     * Enables bStats Metrics. Requires that {@code getbStatsResourceId} returns a valid resource id.
     */
    public void enableMetrics() {
        this.metrics = new Metrics(this, getbStatsResourceId());
    }

    /**
     * Creates a new {@link SpigotMCUpdateHandler}.
     *
     * @return Returns a {@link SpigotMCUpdateHandler} for this plugin.
     */
    public final SpigotMCUpdateHandler buildUpdateChecker() {
        return new SpigotMCUpdateHandler(this, getSpigotResourceId());
    }

    /**
     * Determines if the server version is {@code pre-1.13}.
     *
     * @return {@code true} if the server version is before {@code 1.13}; {@code false} for {@code 1.13} and beyond.
     */
    public boolean legacy() {
        return !XMaterial.TRIDENT.isSupported();
    }

    /**
     * {@inheritDoc}
     */
    public void log(String s) {
        this.getLogger().info(Utils.toColor(s));
    }
}