package me.darrionat.pluginlib.schematic.files;

import me.darrionat.pluginlib.schematic.Clipboard;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Handles saving and loading {@link Clipboard} objects. This handler utilizes GZIP compression for high data
 * compression ratios.
 *
 * @see #saveCompressedBuild(Clipboard, File)
 * @see #loadCompressedBuild(File)
 */
public class CompressionHandler {
    /**
     * Saves a {@link Clipboard} object to a given file and utilizes GZIP compression.
     *
     * @param build       The clipboard to save.
     * @param destination The file to save the build to.
     * @throws IOException Thrown when an IOException occurs.
     */
    public static void saveCompressedBuild(Clipboard build, File destination) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
        ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut);
        // Write build to object output
        objectOut.writeObject(BuildSerializer.buildToString(build));
        objectOut.close();
        // Write build to file
        FileOutputStream fos = new FileOutputStream(destination);
        baos.writeTo(fos);
        // Close things
        gzipOut.finish();
        gzipOut.close();
        baos.close();
    }

    /**
     * Loads an uncompressed {@link Clipboard} object from a given file.
     *
     * @param source The file to read from.
     * @throws IOException Thrown when an IOException occurs.
     */
    public static Clipboard loadCompressedBuild(File source) throws IOException {
        FileInputStream fin = new FileInputStream(source);
        byte[] fileContent = new byte[(int) source.length()];
        // Reads up to certain bytes of data from this input stream into an array of bytes.
        fin.read(fileContent);

        ByteArrayInputStream bais = new ByteArrayInputStream(fileContent);
        GZIPInputStream gzipIn = new GZIPInputStream(bais);
        ObjectInputStream objectIn = new ObjectInputStream(gzipIn);

        String buildData;
        try {
            buildData = (String) objectIn.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        objectIn.close();
        return BuildSerializer.parseBuild(buildData);
    }
}