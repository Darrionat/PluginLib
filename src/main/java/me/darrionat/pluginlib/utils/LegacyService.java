package me.darrionat.pluginlib.utils;

import me.darrionat.legacy.LegacyEnchantment;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;

/**
 * A class that is used to statically access older API of Spigot.
 */
public class LegacyService {
    /**
     * Stores legacy enchantments and their ids.
     */
    private static final HashMap<Enchantment, Integer> ENCHANTMENT_IDS = new HashMap<>();

    /**
     * Creates a new enchantment object that is created from the LegacyEnchantmentWrapper in order to support {@code
     * pre-1.13}
     *
     * @param name     the name of the enchantment
     * @param maxLevel the max level of the enchantment
     * @return returns an {@code Enchantment} constructed with the {@code 1.8-1.12} Enchantment class
     */
    public static Enchantment createEnchantment(String name, int maxLevel) {
        LegacyEnchantment ench = new LegacyEnchantment(name, maxLevel);
        ENCHANTMENT_IDS.put(ench, LegacyEnchantment.getId(ench.getName()));
        return ench;
    }

    /**
     * Gets the id of an enchantment.
     *
     * @param enchantment The id of an enchantment.
     * @return The assigned id of the enchantment.
     */
    public static int geEnchantmentId(Enchantment enchantment) {
        return ENCHANTMENT_IDS.get(enchantment);
    }
}