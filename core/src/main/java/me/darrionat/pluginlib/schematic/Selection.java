package me.darrionat.pluginlib.schematic;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents two {@link Location}s within the same {@link World} that inclusively encapsulate a 3-dimensional volume.
 */
public class Selection {
    private static final String ILLEGAL_WORLD = "Selection worlds must be equal";
    private static final String INCOMPLETE = "Selection is not complete";
    private Location loc1;
    private Location loc2;

    /**
     * Creates a blank selection.
     */
    public Selection() {
    }

    /**
     * Creates a selection with two given locations.
     *
     * @param loc1 The first location.
     * @param loc2 The second location.
     */
    public Selection(@Nullable Location loc1, @Nullable Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    /**
     * Gets the first location of this selection.
     *
     * @return The first point.
     */
    @Nullable
    public Location getPointOne() {
        return loc1;
    }

    /**
     * Gets the second location of this selection.
     *
     * @return The second point.
     */
    @Nullable
    public Location getPointTwo() {
        return loc2;
    }

    /**
     * Sets the first point for the {@code Selection}.
     *
     * @param loc The location to set.
     * @throws IllegalArgumentException Thrown if {@code loc} is not within the same world as the second point, <i>if
     *                                  defined</i>.
     */
    public void setPointOne(Location loc) {
        if (validateLocations(loc, loc2))
            throw new IllegalArgumentException(ILLEGAL_WORLD);
        this.loc1 = loc;
    }

    /**
     * Sets the second point for the {@code Selection}.
     *
     * @param loc The location to set.
     * @throws IllegalArgumentException Thrown if {@code loc} is not within the same world as the first point, <i>if
     *                                  defined</i>.
     */
    public void setPointTwo(Location loc) {
        if (!validateLocations(loc, loc1))
            throw new IllegalArgumentException(ILLEGAL_WORLD);
        this.loc2 = loc;
    }

    /**
     * Determines if both locations of the selection are valid.
     *
     * @return Returns {@code true} if both locations are non-null; {@code false} otherwise.
     */
    public boolean complete() {
        return loc1 != null && loc2 != null;
    }

    /**
     * Gets the world of this selection.
     *
     * @return The world of this selection.
     * @throws IllegalStateException Thrown when the selection is incomplete.
     */
    @NotNull
    public World getWorld() {
        requireComplete();
        // World is never null, see validateLocations(Location, Location)
        return loc1.getWorld();
    }

    /**
     * Gets the difference between the two locations' x coordinates.
     *
     * @return The difference between both points on the {@code x-axis}.
     * @throws IllegalStateException Thrown when the selection is incomplete.
     */
    public int getXDiff() {
        requireComplete();
        return Math.abs(loc1.getBlockX() - loc2.getBlockX());
    }

    /**
     * Gets the difference between the two locations' y coordinates.
     *
     * @return The difference between both points on the {@code y-axis}.
     * @throws IllegalStateException Thrown when the selection is incomplete.
     */
    public int getYDiff() {
        requireComplete();
        return Math.abs(loc1.getBlockY() - loc2.getBlockY());
    }

    /**
     * Gets the difference between the two locations' z coordinates.
     *
     * @return The difference between both points on the {@code z-axis}.
     * @throws IllegalStateException Thrown when the selection is incomplete.
     */
    public int getZDiff() {
        requireComplete();
        return Math.abs(loc1.getBlockZ() - loc2.getBlockZ());
    }

    /**
     * Gets the smaller x coordinate between the two selection points.
     *
     * @return The lowest value of both points on the {@code x-axis}.
     * @throws IllegalStateException Thrown when the selection is incomplete.
     */
    public int getLowX() {
        requireComplete();
        return Math.min(loc1.getBlockX(), loc2.getBlockX());
    }

    /**
     * Gets the smaller y coordinate between the two selection points.
     *
     * @return The lowest value of both points on the {@code y-axis}.
     * @throws IllegalStateException Thrown when the selection is incomplete.
     */
    public int getLowY() {
        requireComplete();
        return Math.min(loc1.getBlockY(), loc2.getBlockY());
    }

    /**
     * Gets the smaller z coordinate between the two selection points.
     *
     * @return The lowest value of both points on the {@code z-axis}.
     * @throws IllegalStateException Thrown when the selection is incomplete.
     */
    public int getLowZ() {
        requireComplete();
        return Math.min(loc1.getBlockZ(), loc2.getBlockZ());
    }

    /**
     * Gets the larger x coordinate between the two selection points.
     *
     * @return The highest value of both points on the {@code x-axis}.
     * @throws IllegalStateException Thrown when the selection is incomplete.
     */
    public int getHighX() {
        requireComplete();
        return Math.max(loc1.getBlockX(), loc2.getBlockX());
    }

    /**
     * Gets the larger y coordinate between the two selection points.
     *
     * @return The highest value of both points on the {@code y-axis}.
     * @throws IllegalStateException Thrown when the selection is incomplete.
     */
    public int getHighY() {
        requireComplete();
        return Math.max(loc1.getBlockY(), loc2.getBlockY());
    }

    /**
     * Gets the larger z coordinate between the two selection points.
     *
     * @return The highest value of both points on the {@code z-axis}.
     * @throws IllegalStateException Thrown when the selection is incomplete.
     */
    public int getHighZ() {
        requireComplete();
        return Math.max(loc1.getBlockZ(), loc2.getBlockZ());
    }

    /**
     * Calculates the volume of this selection.
     *
     * @return The volume of the selection in metres cubed.
     * @throws IllegalStateException Thrown when the selection is incomplete.
     */
    public int getVolume() {
        return getXDiff() * getYDiff() * getZDiff();
    }

    /**
     * Validates that two locations are compatible.
     * <p>
     * Returns {@code true} if any locations are {@code null}. If neither location is null, they must both be within the
     * same {@code World}; otherwise {@code false}.
     *
     * @param a The first location.
     * @param b The second location.
     * @return Returns {@code true} if the locations can be used together; {@code false} otherwise.
     */
    private boolean validateLocations(Location a, Location b) {
        if (a == null || b == null)
            return true;
        if (a.getWorld() == null || b.getWorld() == null)
            return false;
        return a.getWorld().getName().equals(b.getWorld().getName());
    }

    /**
     * Checks that this selection is complete. This is mainly used to prevent {@code NullPointerException}s.
     *
     * @throws IllegalStateException Thrown when the selection is incomplete.
     */
    private void requireComplete() {
        if (!complete())
            throw new IllegalStateException(INCOMPLETE);
    }
}