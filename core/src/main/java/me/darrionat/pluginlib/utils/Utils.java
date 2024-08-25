package me.darrionat.pluginlib.utils;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Small useful methods that don't belong anywhere else.
 */
public class Utils {
    /**
     * Represents the pattern of a hex code.
     */
    private static final Pattern PATTERN = Pattern.compile("(#[a-fA-F0-9]{6})");
//    Pattern pattern = Pattern.compile("(#[a-fA-F0-9]{6})");

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
            Matcher matcher = PATTERN.matcher(s);
            while (matcher.find()) {
                String hexCode = s.substring(matcher.start(), matcher.end());
                String replaceSharp = hexCode.replace('#', 'x');

                char[] ch = replaceSharp.toCharArray();
                StringBuilder builder = new StringBuilder();
                for (char c : ch) {
                    builder.append("&").append(c);
                }

                s = s.replace(hexCode, builder.toString());
                matcher = PATTERN.matcher(s);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Creates an item from given information.
     *
     * @param material   The material of the item.
     * @param amount     The amount of the item.
     * @param name       The name of the item. If {@code null}, the item's display name will be a single space.
     * @param loreString The lore of the item represented by multiple strings. If {@code null}, item's lore will be
     *                   empty.
     * @return The created item.
     */
    public static ItemStack buildItem(XMaterial material, int amount, String name, String... loreString) {
        List<String> lore = new ArrayList<>();
        for (String s : loreString)
            lore.add(Utils.toColor(s));
        return buildItem(material, amount, name, lore);
    }

    /**
     * Creates an item from given information.
     *
     * @param material The material of the item.
     * @param amount   The amount of the item.
     * @param name     The name of the item. If {@code null}, the item's display name will be a single space.
     * @param lore     The lore of the item. If {@code null}, item's lore will be empty.
     * @return The created item.
     */
    public static ItemStack buildItem(XMaterial material, int amount, String name, List<String> lore) {
        ItemStack item = material.parseItem();
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        if (name == null)
            name = " ";
        meta.setDisplayName(Utils.toColor(name));
        if (meta != null)
            meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}