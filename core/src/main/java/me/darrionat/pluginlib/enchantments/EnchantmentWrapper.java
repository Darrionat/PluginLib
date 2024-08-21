package me.darrionat.pluginlib.enchantments;

import me.darrionat.pluginlib.Plugin;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a wrapper class that constructs an enchantment with a {@code NamespacedKey} instead of an {@code id}.
 * <p>
 * This offers support for {@code 1.13} and beyond custom enchantments.
 */
public class EnchantmentWrapper extends Enchantment {
    /**
     * The name of this enchantment.
     */
    private final String name;
    /**
     * The max possible level for this enchantment.
     */
    private final int maxLevel;

    /**
     * Key of this enchantment.
     */
    private final NamespacedKey key;

    /**
     * Constructs a new {@code Enchantment} with a given name.
     *
     * @param name     The name of the enchantment.
     * @param maxLevel The max level of the enchantment.
     */
    public EnchantmentWrapper(String name, int maxLevel) {
        super();
        Objects.requireNonNull(name);
        this.key = new NamespacedKey(Plugin.getProject(), name);
        this.name = name;
        this.maxLevel = maxLevel;
    }

    /**
     * Constructs a {@code NamespacedKey} from a string.
     *
     * @param name The name of the enchantment.
     * @return A namespaced key based upon the enchantment name.
     */
    private static NamespacedKey getKey(String name) {
        return new NamespacedKey(Plugin.getProject(), formatName(name));
    }

    /**
     * Formats the name of the enchantment.
     *
     * @param name The name of the enchantment.
     * @return An edited version of the enchantment name for internal use.
     */
    private static String formatName(String name) {
        String s = name.toLowerCase();
        return s.replaceAll(" ", "_");
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment arg0) {
        return false;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    @Override
    public @NotNull String getTranslationKey() {
        return name;
    }
}