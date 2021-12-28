package me.darrionat.pluginlib.schematic;

import me.darrionat.pluginlib.utils.TriState;
import org.bukkit.entity.Player;

/**
 * Represents the coordinate directions. Each value also holds two {@link TriState} values to determine if it increases
 * that coordinate in its direction.
 */
public enum Direction {
    // NORTH AND SOUTH FOR Z DIRECTION
    NORTH(TriState.NONE, TriState.FALSE),
    SOUTH(TriState.NONE, TriState.TRUE),
    // EAST AND WEST FOR X DIRECTION
    EAST(TriState.TRUE, TriState.NONE),
    WEST(TriState.FALSE, TriState.NONE);

    private final TriState increasesX;
    private final TriState increasesZ;

    /**
     * Defines how a cardinal direction interacts with the coordinate system.
     * <p>
     * Each TriState parameter determines how that axis is changed.
     * <ul>
     *     <li> TRUE means the coordinate increases
     *     <li> FALSE means the coordinate decreases
     *     <li> NONE means the coordinate is unchanged
     * </ul>
     *
     * @param increasesX A TriState value for the x-axis
     * @param increasesZ A TriState value for the z-axis
     */
    Direction(TriState increasesX, TriState increasesZ) {
        this.increasesX = increasesX;
        this.increasesZ = increasesZ;
    }

    /**
     * Gets the direction that the player is facing.
     *
     * @param p The player
     * @return The players direction
     */
    public static Direction getPlayerDirection(Player p) {
        // yaw in degrees
        float yaw = p.getLocation().getYaw();
        // degrees from Location.getYaw() documentation
        if (yaw < 45)
            return SOUTH;
        if (yaw < 135)
            return WEST;
        if (yaw < 225)
            return NORTH;
        if (yaw < 315)
            return EAST;
        return SOUTH;
    }

    /**
     * Gets the TriState value for the x-axis.
     * <ul>
     * <li> TRUE means the coordinate increases
     * <li> FALSE means the coordinate decreases
     * <li> NONE means the coordinate is unchanged
     * </ul>
     *
     * @return The Tristate value for the x-axis
     */
    public TriState increasesX() {
        return increasesX;
    }

    /**
     * Gets the TriState value for the z-axis.
     * <ul>
     * <li> TRUE means the coordinate increases
     * <li> FALSE means the coordinate decreases
     * <li> NONE means the coordinate is unchanged
     * </ul>
     *
     * @return The Tristate value for the z-axis
     */
    public TriState increasesZ() {
        return increasesZ;
    }
}