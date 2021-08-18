package me.darrionat.pluginlib.datastructures;

import com.cryptomorin.xseries.XMaterial;
import me.darrionat.pluginlib.schematic.MaterialService;

/**
 * Represents a {@link XMaterial} and its hash.
 */
public class MaterialData implements Comparable<MaterialData> {
    /**
     * The {@code XMaterial} of this material.
     */
    private final XMaterial type;
    /**
     * The hash of the {@code XMaterial}.
     */
    private final int hash;

    /**
     * Constructs a new {@code MaterialData}.
     *
     * @param material The material to be represented.
     */
    public MaterialData(XMaterial material) {
        this.type = material;
        this.hash = MaterialService.hash(material);
    }

    /**
     * The name of this material.
     *
     * @return The name of this material.
     */
    public XMaterial getType() {
        return type;
    }

    /**
     * Gets the hash of the material associated with this node.
     *
     * @return The hash of the material.
     */
    public int getHash() {
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(MaterialData o) {
        return Integer.compare(this.hash, o.hash);
    }
}