package me.darrionat.pluginlib.enchantments;

import me.darrionat.pluginlib.Plugin;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Map;

public class EnchantmentService implements EnchantmentHandler {
    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public void unregisterEnchantment(Enchantment enchantment) {
        try {
            Field byKeyField = Enchantment.class.getDeclaredField("byKey");
            Field byNameField = Enchantment.class.getDeclaredField("byName");

            byKeyField.setAccessible(true);
            byNameField.setAccessible(true);

            Map<NamespacedKey, Enchantment> byKey = (Map<NamespacedKey, Enchantment>) byKeyField.get(null);
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);

            byKey.remove(enchantment.getKey());
            byName.remove(enchantment.getName());
            Plugin.getProject().log("Unregistered " + enchantment.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}