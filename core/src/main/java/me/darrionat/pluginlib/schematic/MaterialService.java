package me.darrionat.pluginlib.schematic;

import com.cryptomorin.xseries.XMaterial;
import me.darrionat.pluginlib.datastructures.MaterialData;
import me.darrionat.pluginlib.datastructures.RBNode;
import me.darrionat.pluginlib.datastructures.RBTree;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;

/**
 * The {@code MaterialService} is a handler for all materials contained within the {@link XMaterial} enum.
 * <p>
 * This service operates by a {@link RBTree<MaterialData>}. The tree stores all materials and their hashes within an
 * object called {@link MaterialData}.
 * <p>
 * When the material hash is known but the material itself is not known, the tree performs a binary search to find the
 *
 * @see #findMaterial(int)
 * @see #hash(XMaterial)
 */
public class MaterialService {
    /**
     * A Red-Black tree implementation of all {@link XMaterial}s. The tree is of type {@link MaterialData}.
     * <p>
     * The tree of {@code MaterialData} quick reverse lookup so that an {@code XMaterial} can be found from its hash.
     * <p>
     * Hashes can be determined with {@link #hash(XMaterial)}.
     */
    public static final RBTree<MaterialData> TREE = new RBTree<>();

    // Statically initializes the tree
    static {
        for (XMaterial material : XMaterial.values()) {
            TREE.add(new MaterialData(material));
        }
    }

    /**
     * Performs a binary search for {@code XMaterial} containing a specific hash.
     *
     * @param hash The hash of the material data being searched.
     * @return Returns the found {@code XMaterial} matching the given hash; {@code null} if not found.
     */
    public static XMaterial findMaterial(int hash) {
        // Search from the root
        return findMaterial(TREE.getRoot(), hash);
    }

    /**
     * Performs multiple binary searches to match all passed {@code hashes} to their {@code XMaterial}.
     *
     * @param hashes The hashes of the materials being searched.
     * @return Returns the found {@code XMaterial}s paired the given hash; {@code null} if not found.
     */
    public static HashMap<Integer, XMaterial> findAll(@NotNull Collection<Integer> hashes) {
        HashMap<Integer, XMaterial> toReturn = new HashMap<>();
        for (Integer hash : hashes) {
            // If already found, don't search again
            if (toReturn.containsKey(hash)) continue;
            // Search for material
            toReturn.put(hash, findMaterial(hash));
        }
        return toReturn;
    }

    /**
     * Performs a binary search for {@code XMaterial} containing a specific hash from a defined root.
     *
     * @param node The root to search from.
     * @param hash The hash of the material data being searched.
     * @return Returns the found {@code XMaterial} matching the given hash; {@code null} if not found.
     */
    private static XMaterial findMaterial(RBNode<MaterialData> node, int hash) {
        while (node != null && node.getData().getHash() != hash) {
            if (hash < node.getData().getHash()) {
                node = node.getLeft();
            } else {
                node = node.getRight();
            }
        }
        return node == null ? null : node.getData().getType();
    }

    /**
     * Matches this {@code Material} to a {@code XMaterial} and gets the hash for the given type.
     *
     * @param material The material to be hashed.
     * @return The hash of the material, for quick lookup.
     */
    public static int hash(Material material) {
        return hash(XMaterial.matchXMaterial(material));
    }

    /**
     * Gets the hash for the given {@code XMaterial}.
     *
     * @param material The material to be hashed.
     * @return The hash of the material, for quick lookup.
     */
    public static int hash(XMaterial material) {
        return material.hashCode();
    }
}