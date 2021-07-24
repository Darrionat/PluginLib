package me.darrionat.pluginlib.enchantments;

import org.bukkit.enchantments.Enchantment;

public interface EnchantmentHandler {
    /**
     * Unregisters an enchantment.
     *
     * @param enchantment The enchantment to unregister.
     */
    void unregisterEnchantment(Enchantment enchantment);
}