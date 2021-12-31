package me.darrionat.pluginlib.schematic;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;

import java.util.HashMap;

/**
 * The {@code MaterialService} is a handler for all materials contained within the {@link XMaterial} enum.
 * <p>
 * This service operates by a {@link HashMap}. The map stores all material hashes with their associated material as the
 * value for quick reverse lookup.
 *
 * @see #findMaterial(int)
 */
public class MaterialService {
    /**
     * A HashMap implementation of all {@link XMaterial}s.
     * <p>
     * The map of {@code <Integer, XMaterial>} allows quick reverse lookup so that an {@code XMaterial} can be found
     * from its hash.
     */
    public static final HashMap<Integer, XMaterial> MATERIAL_HASH_MAP = new HashMap<>();

    // Statically initializes the map
    static {
        for (XMaterial material : XMaterial.values()) {
            MATERIAL_HASH_MAP.put(material.hashCode(), material);
        }
    }

    /**
     * Fetches the {@code XMaterial} that has a specific hash.
     *
     * @param hash The hash of the material data being searched.
     * @return Returns the found {@code XMaterial} matching the given hash; {@code null} if not found.
     */
    public static XMaterial findMaterial(int hash) {
        return MATERIAL_HASH_MAP.get(hash);
    }

    /**
     * Matches this {@code Material} to a {@code XMaterial} and gets the hash for the given type.
     *
     * @param material The material to be hashed.
     * @return The hash of the material, for quick lookup.
     */
    public static int hash(Material material) {
        return XMaterial.matchXMaterial(material).hashCode();
    }

    /**
     * Gets the hash for the given {@code XMaterial}.
     *
     * @param material The material to be hashed.
     * @return The hash of the material, for quick lookup.
     * @deprecated Use {@code material.hashCode()} instead.
     */
    @Deprecated
    public static int hash(XMaterial material) {
        return material.hashCode();
    }
}