package me.darrionat.pluginlib.schematic;

import me.darrionat.pluginlib.schematic.files.BuildSerializer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

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
    /*
    public void paste(Location loc, Direction direction) {
        if (direction == null)
            direction = Direction.NORTH;
        // TODO implement
    }
     */
}