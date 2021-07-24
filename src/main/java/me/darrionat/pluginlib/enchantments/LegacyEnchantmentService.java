package me.darrionat.pluginlib.enchantments;

import me.darrionat.pluginlib.utils.LegacyService;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Map;

public class LegacyEnchantmentService implements EnchantmentHandler {
    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public void unregisterEnchantment(Enchantment enchantment) {
        try {
            Field byIdField = Enchantment.class.getDeclaredField("byId");
            Field byNameField = Enchantment.class.getDeclaredField("byName");

            byIdField.setAccessible(true);
            byNameField.setAccessible(true);

            Map<Integer, Enchantment> byId = (Map<Integer, Enchantment>) byIdField.get(null);
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);

            int removeId = LegacyService.geEnchantmentId(enchantment);
            byId.remove(removeId);
            byName.remove(enchantment.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}