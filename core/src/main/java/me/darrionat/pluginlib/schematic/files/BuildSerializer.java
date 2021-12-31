package me.darrionat.pluginlib.schematic.files;

import com.cryptomorin.xseries.XMaterial;
import me.darrionat.pluginlib.schematic.Clipboard;
import me.darrionat.pluginlib.schematic.MaterialService;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a file handler for {@link Clipboard}s.
 * <p>
 * The {@code BuildSerializer} offers a way to efficiently read and write {@code Clipboard}s.
 *
 * @see #parseBuild(String)
 * @see #buildToString(Clipboard)
 */
public class BuildSerializer {
    private static final String DIMENSION_SEP = "x";
    private static final String NEW_LINE = "\n";
    private static final String NEW_COLUMN = ";";
    private static final String NEW_BLOCK = "-";
    private static final String DATA_SEP = "%";

    /**
     * Converts the build into a String.
     *
     * @param build The build to save from a clipboard.
     * @return The {@link Clipboard} as a string.
     */
    public static String buildToString(@NotNull Clipboard build) {
        Objects.requireNonNull(build);

        BlockData[][][] blocks = build.getBlocks();
        int length = build.getLength();
        int height = build.getHeight();
        int width = build.getWidth();

        // Write dimensions so that they can be known for array initialization in read
        StringBuilder builder = new StringBuilder();
        builder.append(dimensionsAsString(length, height, width)).append(NEW_LINE);

        for (int x = 0; x < length; x++) {
            // If it's not the first line, add a new column separator
            if (x != 0) builder.append(NEW_LINE);

            // Writes the build into the file
            for (int y = 0; y < height; y++) {
                // If it's not the first line, add a new line
                if (y != 0) builder.append(NEW_COLUMN);

                for (int z = 0; z < width; z++) {
                    // If it's not the first block in the column, add a new block separator
                    if (z != 0) builder.append(NEW_BLOCK);

                    BlockData data = blocks[x][y][z];
                    // Fetch the hash of this material
                    int hash = MaterialService.hash(data.getMaterial());
                    // Get the block data and hide all unspecified tags to optimize storage
                    String dataString = data.getAsString(true);
                    // Save the data and hash as a string as hash{data}
                    builder.append(hash).append(DATA_SEP).append(dataString);
                }
            }
        }
        return builder.toString();
    }

    /**
     * Loads a String of build data into a {@link Clipboard}.
     *
     * @param buildData The data of the build to be loaded in.
     * @return The build loaded into a clipboard.
     */
    public static Clipboard parseBuild(@NotNull String buildData) {
        int length = -1, height = -1, width = -1;
        BlockData[][][] data = null;
        String[] lines = buildData.split(NEW_LINE);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            // First iteration, load dimensions
            if (i == 0) {
                int[] dimensions = parseDimensions(line);
                length = dimensions[0];
                height = dimensions[1];
                width = dimensions[2];
                // x, y, z
                data = new BlockData[length][height][width];
                continue;
            }
            // Parse the block data for this row
            data[i - 1] = parseBlockData(line, height, width);
        }
        return new Clipboard(data);
    }

    /**
     * Gets the dimensions of the build as a string to be stored within the file.
     *
     * @param length The length of the build.
     * @param height The height of the build.
     * @param width  The width of the build.
     * @return Returns the dimension string to be stored within the file.
     */
    private static String dimensionsAsString(int length, int height, int width) {
        return "dim=" + length + DIMENSION_SEP + height + DIMENSION_SEP + width;
    }

    /**
     * Parses the dimensions line within the file from a string to an {@code int[]}.
     *
     * @param line The dimensions line.
     * @return Returns the dimensions of the build.
     */
    private static int[] parseDimensions(@NotNull String line) {
        int[] toReturn = new int[3];
        line = line.replace("dim=", "");
        String[] arr = line.split(DIMENSION_SEP);
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = Integer.parseInt(arr[i]);
        }
        return toReturn;
    }

    /**
     * Parses a {@code y-z} plane of block data from a build file.
     *
     * @param line   The line of data representing a 2-dimensional plane.
     * @param height The height of the build.
     * @param width  The width of the build.
     * @return Returns a 2-dimensional array representing a {@code y-z} plane of block data from a build file.
     * @throws NullPointerException Thrown when an unknown material is passed.
     */
    private static BlockData[][] parseBlockData(@NotNull String line, int height, int width) {
        BlockData[][] toReturn = new BlockData[height][width];
        // Splits the columns
        String[] cols = line.split(NEW_COLUMN);

        for (int y = 0; y < cols.length; y++) {
            String col = cols[y];
            // Blocks within the column
            String[] blocks = col.split(NEW_BLOCK);
            for (int z = 0; z < cols.length; z++) {
                // hash + DATA_SEP + blockData
                String[] splitData = blocks[z].split(DATA_SEP);
                int hash = Integer.parseInt(splitData[0]);
                String data = splitData[1];
                // Find the type
                XMaterial type = MaterialService.findMaterial(hash);
                if (type == null)
                    throw new NullPointerException("Invalid Material");
                // Get the block data
                BlockData blockData = Bukkit.createBlockData(type.parseMaterial(), data);
                toReturn[y][z] = blockData;
            }
        }
        return toReturn;
    }
}