package me.darrionat.pluginlib.schematic.files;

import com.cryptomorin.xseries.XMaterial;
import me.darrionat.pluginlib.Plugin;
import me.darrionat.pluginlib.files.FileBuilder;
import me.darrionat.pluginlib.schematic.Clipboard;
import me.darrionat.pluginlib.schematic.MaterialService;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents a file handler for {@link Clipboard}s.
 * <p>
 * All clipboards that are saved end with {@code .build}. The {@code BuildSerializer} creates a folder within the
 * specified plugin's folder as a location to store these files. The {@code BuildSerializer} must be utilized to
 * properly read and write {@code Clipboard}s.
 *
 * @see #saveBuild(Clipboard, String)
 * @see #loadBuild(String)
 */
public class BuildSerializer extends FileBuilder {
    /**
     * The file name of the directory {@code BuildSerializer} represents.
     *
     * @see #getFile()
     */
    public static final String BUILD_FOLDER = File.separator + "builds";
    /**
     * The file extension of all saved {@link Clipboard}s.
     */
    public static final String FILE_EXTENSION = ".build";

    private static final String DIMENSION_SEP = "x";
    private static final String NEW_LINE = "\n";
    private static final String NEW_COLUMN = ";";
    private static final String NEW_BLOCK = "-";
    private static final String DATA_SEP = "%";

    /**
     * Constructs a new {@code BuildSerializer} to handle reading and writing of {@link Clipboard}s.
     *
     * @param plugin The plugin this {@code BuildSerializer} belongs to.
     */
    public BuildSerializer(Plugin plugin) {
        super(plugin, BUILD_FOLDER);
        if (!exists())
            createFile();
    }

    /**
     * Saves the build into the {@link #BUILD_FOLDER}.
     *
     * @param build     The build to save from a clipboard.
     * @param buildName The name of the build to save.
     */
    public void saveBuild(@NotNull Clipboard build, @NotNull String buildName) throws IOException {
        Objects.requireNonNull(build);
        Objects.requireNonNull(buildName);
        File f = new File(getFile(), buildName + FILE_EXTENSION);
        FileWriter writer = new FileWriter(f);

        BlockData[][][] blocks = build.getBlocks();
        int length = build.getLength();
        int height = build.getHeight();
        int width = build.getWidth();

        // Write dimensions so that they can be known for array initialization in read
        writer.write(dimensionsAsString(length, height, width));
        writer.write(NEW_LINE);

        for (int x = 0; x < length; x++) {
            // If it's not the first line, add a new column separator
            if (x != 0) writer.write(NEW_LINE);

            // Writes the build into the file
            for (int y = 0; y < height; y++) {
                // If it's not the first line, add a new line
                if (y != 0) writer.write(NEW_COLUMN);

                for (int z = 0; z < width; z++) {
                    // If it's not the first block in the column, add a new block separator
                    if (z != 0) writer.write(NEW_BLOCK);

                    BlockData data = blocks[x][y][z];
                    // Fetch the hash of this material
                    int hash = MaterialService.hash(data.getMaterial());
                    // Get the block data and hide all unspecified tags to optimize storage
                    String dataString = data.getAsString(true);
                    // Save the data and hash as a string as hash{data}
                    writer.write(hash + DATA_SEP + dataString);
                }
            }
        }
        writer.close();
    }

    /**
     * Loads a build into a {@link Clipboard}.
     *
     * @param buildName The name of the build to be saved.
     * @return The build loaded into a clipboard.
     * @throws FileNotFoundException Thrown when an invalid build name is passed.
     * @throws NullPointerException  Thrown when an invalid material is read.
     */
    public Clipboard loadBuild(@NotNull String buildName) throws FileNotFoundException {
        File f = new File(getFile(), buildName + FILE_EXTENSION);
        Scanner sc = new Scanner(f);
        int length = -1, height = -1, width = -1;
        BlockData[][][] data = null;
        int x = 0;
        while (sc.hasNext()) {
            String line = sc.next();
            if (length == -1) {
                int[] dimensions = parseDimensions(line);
                length = dimensions[0];
                height = dimensions[1];
                width = dimensions[2];
                // x, y, z
                data = new BlockData[length][height][width];
                continue;
            }
            // Parse the block data for this row
            data[x] = parseBlockData(line, height, width);
            x++;
        }
        sc.close();
        return new Clipboard(data);
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
    private BlockData[][] parseBlockData(@NotNull String line, int height, int width) {
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

    /**
     * Gets the dimensions of the build as a string to be stored within the file.
     *
     * @param length The length of the build.
     * @param height The height of the build.
     * @param width  The width of the build.
     * @return Returns the dimension string to be stored within the file.
     */
    private String dimensionsAsString(int length, int height, int width) {
        return "dim=" + length + DIMENSION_SEP + height + DIMENSION_SEP + width;
    }

    /**
     * Parses the dimensions line within the file from a string to an {@code int[]}.
     *
     * @param line The dimensions line.
     * @return Returns the dimensions of the build.
     */
    private int[] parseDimensions(@NotNull String line) {
        int[] toReturn = new int[3];
        line = line.replace("dim=", "");
        String[] arr = line.split(DIMENSION_SEP);
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = Integer.parseInt(arr[i]);
        }
        return toReturn;
    }
}