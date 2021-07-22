package me.darrionat.pluginlib.utils;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Small useful methods that don't belong anywhere else.
 */
public class Utils {
    /**
     * Represents the pattern of a hex code.
     */
    private static final Pattern PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

    /**
     * Formats chat to contain color codes using the {@code &}.
     * <p>
     * If the server is using a version equal to {@code 1.16} or greater, hex codes are also supported.
     *
     * @param s The {@link String} to convert.
     * @return Returns a formatted string with color.
     */
    public static String toColor(String s) {
        // 1.16+
        if (XMaterial.NETHERITE_BLOCK.isSupported()) {
            Matcher match = PATTERN.matcher(s);
            while (match.find()) {
                String color = s.substring(match.start(), match.end());
                s = s.replace(color, ChatColor.valueOf(color) + "");
                match = PATTERN.matcher(s);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}