package me.darrionat.pluginlib.schematic;

import me.darrionat.pluginlib.schematic.files.BuildSerializer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.Objects;

/**
 * Represents a 3-dimensional copy of {@link Block}s. A {@code Clipboard} can be rotated, copied, and pasted into a
 * {@code World}.
 * <p>
 * Loading and saving {@code Clipboard}s should be handled through a {@link BuildSerializer}.
 * <p>
 * Clipboards are immutable.
 *
 * @see #copy(Selection)
 * @see #rotate()
 * @see BuildSerializer
 */
public class Clipboard {
    /**
     * The BlockData held within the clipboard. The data is saved by length, width, height or x,y,z.
     */
    private final BlockData[][][] blocks;
    private final int length;
    private final int height;
    private final int width;

    /**
     * Constructs a new {@code Clipboard} from a {@code Selection}.
     *
     * @param selection The selection to utilize.
     * @throws IllegalArgumentException Thrown when the selection is incomplete.
     */
    public Clipboard(Selection selection) throws IllegalArgumentException {
        this(copy(selection));
    }

    /**
     * Constructs a new {@code Clipboard} from a 3-dimensional array of {@code BlockData} representing a {@link
     * Selection}. All arrays within the same dimension should have the same length.
     *
     * @param blocks The blocks to put into the clipboard.
     * @see #copy(Selection)
     */
    public Clipboard(BlockData[][][] blocks) {
        this(blocks, blocks.length, blocks[0].length, blocks[0][0].length);
    }

    /**
     * Constructs a new {@code Clipboard} from a 3-dimensional array of {@code BlockData} representing a {@link
     * Selection}.
     *
     * @param blocks  The blocks to put into the clipboard.
     * @param xLength The length of the clipboard.
     * @param yLength The height of the clipboard.
     * @param zLength The width of the clipboard.
     */
    private Clipboard(BlockData[][][] blocks, int xLength, int yLength, int zLength) {
        this.blocks = blocks;
        this.length = xLength;
        this.height = yLength;
        this.width = zLength;
    }

    /**
     * Views and captures the states of all blocks between two locations (inclusive). The returned array follows by
     * {@code [x][y][z]}.
     *
     * @param selection The selection to copy.
     * @return Returns a three-dimensional array containing all {@code BlockState}s within that area.
     * @throws IllegalArgumentException Thrown when the selection is not complete.
     */
    public static BlockData[][][] copy(Selection selection) throws IllegalArgumentException {
        if (!selection.complete())
            throw new IllegalArgumentException("Selection is not complete");
        // Differences between each corner
        int xDiff = selection.getXDiff(), yDiff = selection.getYDiff(), zDiff = selection.getZDiff();
        // Init array
        BlockData[][][] blocks = new BlockData[xDiff + 1][yDiff + 1][zDiff + 1];
        int lowX = selection.getLowX();
        int lowY = selection.getLowY();
        int lowZ = selection.getLowZ();
        World world = selection.getWorld();

        for (int x = 0; x <= xDiff; x++) {
            for (int y = 0; y <= yDiff; y++) {
                for (int z = 0; z <= zDiff; z++) {
                    blocks[x][y][z] = world.getBlockAt(lowX + x, lowY + y, lowZ + z).getBlockData();
                }
            }
        }
        return blocks;
    }

    /**
     * Gets all block states of the clipboard.
     *
     * @return All blocks within the clipboard.
     */
    public BlockData[][][] getBlocks() {
        return blocks;
    }

    /**
     * Gets the length of the x-axis of this clipboard.
     *
     * @return The length of this clipboard.
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the length of the y-axis of this clipboard.
     *
     * @return The height of this clipboard.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the length of the z-axis of this clipboard.
     *
     * @return The width of this clipboard.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Creates and returns a 90 degrees clockwise rotation.
     * <p>
     * This clipboard is not rotated, but instead, a rotated version of this clipboard is returned.
     *
     * @return Returns a 90 degrees clockwise rotation of this clipboard.
     */
    public Clipboard rotate() {
        BlockData[][][] rotation = new BlockData[width][height][length];
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < width; z++) {
                    rotation[z][y][x] = this.blocks[x][y][z];
                }
            }
        }
        return new Clipboard(rotation);
    }

    /**
     * Pastes this clipboard at a given location and direction. All previous blocks at the pasted location will be
     * removed.
     *
     * @param loc       The location to paste at, representing the origin of a paste
     * @param direction The direction of which to paste. If {@code Direction.NORTH}, the build will paste northeast of
     *                  the player. If  {@code Direction.EAST}, then the build will paste southeast of the player, and
     *                  so on
     * @return Returns the previous state of the pasted location
     */
    public Clipboard paste(Location loc, Direction direction) {
        return pasteData(loc, direction, true);
    }

    /**
     * Pastes this clipboard at a given location and direction. All previous blocks at the pasted location will be
     * removed and not saved. Use only if copying the previous state is too computationally intensive.
     *
     * @param loc       The location to paste at, representing the origin of a paste
     * @param direction The direction of which to paste. If {@code Direction.NORTH}, the build will paste northeast of
     *                  the player. If  {@code Direction.EAST}, then the build will paste southeast of the player, and
     *                  so on
     */
    public void hardPaste(Location loc, Direction direction) {
        pasteData(loc, direction, false);
    }

    /**
     * Pastes this clipboard at a given location and direction. All previous blocks at the pasted location will be
     * removed.
     *
     * @param loc              The location to paste at, representing the origin of a paste
     * @param direction        The direction of which to paste. If {@code Direction.NORTH}, the build will paste
     *                         northeast of the player. If  {@code Direction.EAST}, then the build will paste southeast
     *                         of the player, and so on
     * @param getPreviousState If {@code true}, then the state before pasting will be saved into a clipboard, the
     *                         opposite can be said for {@code false}
     * @return If {@param getPreviousState} is {@code true} returns the previous state; otherwise {@code null}.
     */
    private Clipboard pasteData(Location loc, Direction direction, boolean getPreviousState) {
        Objects.requireNonNull(loc, "Location is null");
        World world = loc.getWorld();
        Objects.requireNonNull(world, "World is null");
        // Default direction is arbitrarily NORTH
        if (direction == null)
            direction = Direction.NORTH;

        // Behaviors of the direction
        int incX = direction.increasesX().intValue(), incZ = direction.increasesZ().intValue();

        // Follows a directional pattern to get the difference from the location.
        // Possible values of xDiff and zDiff are -1, 0, or 1.
        int xDiff = incX == 0 ? direction.getNextDirection().increasesX().intValue() : incX;
        int zDiff = incZ == 0 ? direction.getNextDirection().increasesZ().intValue() : incZ;

        // Copies the previous state into a new clipboard
        Clipboard previousState = null;
        if (getPreviousState)
            previousState = getCurrentState(loc, xDiff, zDiff);

        int blockX = loc.getBlockX(), blockY = loc.getBlockY(), blockZ = loc.getBlockZ();

        // Pasting into world
        for (int x = 0; Math.abs(x) < length; x += xDiff) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; Math.abs(z) < width; z += zDiff) {
                    // Build the location
                    Location pasteLoc = new Location(world, blockX + x, blockY + y, blockZ + z);
                    // Paste block data
                    world.getBlockAt(pasteLoc).setBlockData(blocks[Math.abs(x)][y][Math.abs(z)]);
                }
            }
        }
        return previousState;
    }

    /**
     * Copies the area that this paste action will cover into a new clipboard.
     *
     * @param loc1  The origin.
     * @param xDiff The step of the x-axis, defining what direction to go
     * @param zDiff The step of the z-axis, defining what direction to go
     * @return The current state of the selection
     */
    private Clipboard getCurrentState(Location loc1, int xDiff, int zDiff) {
        // Subtract 1 from x,y,z to not include loc1 twice
        Location loc2 = new Location(loc1.getWorld(),
                loc1.getBlockX() + xDiff * length,
                loc1.getBlockY() + height,
                loc1.getBlockZ() + zDiff * width)
                .subtract(1, 1, 1);
        Selection selection = new Selection(loc1, loc2);
        return new Clipboard(selection);
    }
}