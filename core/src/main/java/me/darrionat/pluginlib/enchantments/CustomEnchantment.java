package me.darrionat.pluginlib.enchantments;

import me.darrionat.pluginlib.Plugin;
import me.darrionat.pluginlib.utils.LegacyService;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;

/**
 * Represents a way to have custom enchantments across both legacy and recent versions.
 *
 * @see #getEnchantment()
 */
public abstract class CustomEnchantment {
    /**
     * Stores custom enchantments by name. All names should be entered in all lower case.
     */
    private static final HashMap<String, CustomEnchantment> byName = new HashMap<>();
    /**
     * The plugin that this enchantment belongs to.
     */
    protected final Plugin plugin;
    /**
     * The {@link Enchantment} of this {@code CustomEnchantment}.
     */
    protected final Enchantment enchantment;
    /**
     * The name of this enchantment.
     */
    private final String name;

    /**
     * Constructs a new {@code CustomEnchantment} with a given name.
     */
    public CustomEnchantment(String name) {
        this.name = name;
        plugin = Plugin.getProject();
        enchantment = initEnchantment();
    }

    /**
     * Gets a {@link CustomEnchantment} based upon a given name.
     *
     * @param name The name of the enchantment to fetch, non-case sensitive.
     * @return The enchantment with this name; {@code null} if no results.
     */
    public static CustomEnchantment getByName(String name) {
        return byName.get(name.toLowerCase());
    }

    /**
     * Creates and wraps the enchantment based upon the version of the server.
     *
     * @return The {@link Enchantment} associated with this {@link CustomEnchantment}.
     */
    private Enchantment initEnchantment() {
        Enchantment toReturn;
        // 1.13+
        if (!plugin.legacy())
            toReturn = new EnchantmentWrapper(getName(), getMaxLevel());
        else // Pre 1.13
            toReturn = LegacyService.createEnchantment(getName(), getMaxLevel());
        byName.put(getName().toLowerCase(), this);
        return toReturn;
    }

    /**
     * The name of the enchantment.
     *
     * @return The internal name of the enchantment.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the {@link Enchantment} of this custom enchantment.
     *
     * @return The Bukkit enchantment.
     */
    public Enchantment getEnchantment() {
        return enchantment;
    }

    /**
     * Gets the maximum level of the custom enchantment.
     *
     * @return The highest level the enchantment can go to.
     */
    public abstract int getMaxLevel();
}