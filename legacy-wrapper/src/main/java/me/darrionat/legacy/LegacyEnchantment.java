//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.darrionat.legacy;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Represents a wrapper class that constructs an enchantment with an {@code int} id instead of a {@code NamespacedKey}.
 * <p>
 * This offers support for {@code pre-1.13} custom enchantments.
 */
public class LegacyEnchantment extends Enchantment {
    /**
     * Stores the id and name of all custom {@code LegacyEnchantment}s.
     */
    private static final HashMap<Integer, String> idNameMap = new HashMap<>();
    /**
     * The name of this enchantment.
     */
    private final String name;
    /**
     * The max possible level for this enchantment.
     */
    private final int maxLevel;

    /**
     * Constructs a new {@code LegacyEnchantment}. The id of the enchantment will be determined based upon available
     * ids.
     *
     * @param name     The name of the enchantment.
     * @param maxLevel The max level of the enchantment.
     */
    public LegacyEnchantment(String name, int maxLevel) {
        super(createNewID(name));
        this.name = name;
        this.maxLevel = maxLevel;
    }

    /**
     * Finds an ID to assign to an enchantment with a specific name.
     *
     * @param name The name of the enchantment, not case sensitive.
     * @return The id of the enchantment.
     */
    @SuppressWarnings({"deprecation"})
    private static int createNewID(String name) {
        int id = 101;
        // If the enchantment name already exists, just return it's id
        Enchantment byName = Enchantment.getByName(name);
        if (byName != null && byName.getName().equalsIgnoreCase(name)) {

            id = byName.getId();
            idNameMap.put(id, name);
            return id;
        }
        // If this id already exists, find another one if not this enchantment.
        while (idNameMap.containsKey(id)) {
            if (idNameMap.get(id).equalsIgnoreCase(name)) {
                return id;
            }
            id++;
        }
        // Found ID for enchantment
        idNameMap.put(id, name);
        return id;
    }

    /**
     * Gets the id of an enchantment by its name.
     *
     * @param name The name of the enchantment.
     * @return The assigned id of the enchantment.
     */
    public static int getId(String name) {
        for (Entry<Integer, String> entry : idNameMap.entrySet()) {
            if (entry.getValue().equals(name))
                return entry.getKey();
        }
        return -1;
    }

    @Override
    public String getName() {
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
    public EnchantmentTarget getItemTarget() {
        return null;
    }
}