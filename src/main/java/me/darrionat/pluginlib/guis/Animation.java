package me.darrionat.pluginlib.guis;

import me.darrionat.pluginlib.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a series of actions that are taken within an {@link AnimatedGui} based upon instructions.
 * <p>
 * An animation can vary in speed, items, slots, and more.
 */
public class Animation {
    /**
     * The plugin that carries out this animation.
     */
    private final Plugin plugin;
    /**
     * The gui that the animation takes place in.
     */
    private final AnimatedGui gui;
    /**
     * The id of the animation.
     */
    private final int id;
    /**
     * The slots affected by the animation.
     */
    private final int[] slots;
    /**
     * The first item when the animation is not currently on a slot.
     */
    private final ItemStack from;
    /**
     * The items to change the active slot to when the animation occurs.
     */
    private final ItemStack[] to;
    /**
     * The period between each cycle of the animation.
     */
    private final long period;
    /**
     * If {@code true} the {@code to} item will switch for every slot; otherwise, it will switch after all.
     */
    private final boolean each;
    /**
     * The name of {@code from}.
     *
     * @see #from
     */
    private String fromName;
    /**
     * The lore of {@code from}.
     *
     * @see #from
     */
    private List<String> fromLore;
    /**
     * The id of the task running the animation.
     */
    private int taskId = -1;
    /**
     * Used for iterating slots.
     */
    private int currSlot = 0;
    /**
     * Used for iterating items.
     */
    private int currItem = 0;

    /**
     * Creates a new animation within a gui.
     * <p>
     * Animations should not be constructed outside of an {@link AnimatedGui}.
     *
     * @param plugin The plugin the animation belongs to.
     * @param gui    The gui the animation takes place within.
     * @param id     The id of this animation.
     * @param slots  The slots affected by the animation.
     * @param from   The first item when the animation is not currently on a slot
     * @param to     The items to change the active slot to when the animation occurs.
     * @param period The period between each cycle of the animation.
     * @param each   If {@code true} the {@code to} item will switch for every slot; otherwise, it will switch after
     *               all.
     */
    Animation(Plugin plugin, AnimatedGui gui, int id, int[] slots, ItemStack from, ItemStack[] to, long period, boolean each) {
        requireNonNull(plugin, gui, slots, from, to);
        this.plugin = plugin;
        this.gui = gui;
        this.id = id;
        this.slots = slots;
        this.from = from;
        this.to = to;
        this.period = period;
        this.each = each;
        ItemMeta meta = from.getItemMeta();
        // Set the name and lore of from item
        if (meta != null) {
            fromName = meta.getDisplayName();
            fromLore = meta.getLore();
        }
        if (fromName == null)
            fromName = " ";
        if (fromLore == null)
            fromLore = new ArrayList<>();

    }

    /**
     * Requires that all passed objects are not null.
     *
     * @param obj The objects to check for nullity.
     */
    private void requireNonNull(Object... obj) {
        for (Object o : obj)
            Objects.requireNonNull(o);
    }

    /**
     * Gets the id of this animation.
     *
     * @return The id of this animation within the {@link AnimatedGui}.
     */
    public int getAnimationId() {
        return id;
    }

    /**
     * The {@link AnimatedGui} that this animation is within.
     *
     * @return The gui the animation takes place in.
     */
    public AnimatedGui getGui() {
        return gui;
    }

    /**
     * Starts the animation within the {@link AnimatedGui}.
     * <p>
     * No action is taken if the animation is already running.
     *
     * @see #running()
     * @see #stop()
     */
    public void start() {
        if (running()) return;
        taskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this::animate, 0L, period);
    }

    /**
     * Changes the slots of the animation and handles item cycling.
     */
    private void animate() {
        // Iterate through animation slots
        if (currSlot >= slots.length) {
            // Back to start, new slot cycle
            currSlot = 0;
            // If it's not on each slot, change to item due to new slot cycle
            if (!each) currItem++;
        }
        if (currItem >= to.length) currItem = 0;
        updateGui();
        // If change item on each slot, bump
        if (each) currItem++;
    }

    /**
     * Updates the items within the {@link AnimatedGui}.
     */
    private void updateGui() {
        // New item
        ItemStack item = to[currItem];
        ItemMeta meta = item.getItemMeta();
        String name = null;
        List<String> lore = null;
        if (meta != null) {
            name = meta.getDisplayName();
            lore = meta.getLore();
        }
        // Set previous slot back
        gui.createItem(from, slots[currSlot], fromName, fromLore);
        // Set new slot
        gui.createItem(item, slots[currSlot++], name, lore);
    }

    /**
     * Stops the animation within the {@link AnimatedGui}.
     * <p>
     * Action is only taken if the animation is running.
     *
     * @see #running()
     * @see #start()
     */
    public void stop() {
        if (!running()) return;
        Bukkit.getScheduler().cancelTask(taskId);
        taskId = -1;
        for (int slot : slots)
            gui.createItem(from, slot, fromName, fromLore);
    }

    /**
     * Determines if the animation is running.
     *
     * @return Returns {@code true} if the animation is currently running; {@code false} otherwise.
     */
    public boolean running() {
        return taskId != -1;
    }
}