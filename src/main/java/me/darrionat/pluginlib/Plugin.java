package me.darrionat.pluginlib;

import com.cryptomorin.xseries.XMaterial;
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
     * Creates a new {@link SpigotMCUpdateHandler}.
     *
     * @param resourceId The project id of the resource on SpigotMC.
     * @return Returns a {@link SpigotMCUpdateHandler} for this plugin.
     */
    public final SpigotMCUpdateHandler buildUpdateChecker(int resourceId) {
        return new SpigotMCUpdateHandler(this, resourceId);
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
        System.out.println(Utils.toColor("[" + getName() + "] " + s));
    }
}