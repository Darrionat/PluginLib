package me.darrionat.pluginlib.guis;

import me.darrionat.pluginlib.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
     * The player involved in the animation.
     */
    private final Player p;
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
     * @param gui    The gui the animation takes place within.
     * @param p      The player involved in the animation.
     * @param id     The id of this animation.
     * @param slots  The slots affected by the animation.
     * @param from   The first item when the animation is not currently on a slot
     * @param to     The items to change the active slot to when the animation occurs.
     * @param period The period between each cycle of the animation.
     * @param each   If {@code true} the {@code to} item will switch for every slot; otherwise, it will switch after
     *               all.
     */
    Animation(AnimatedGui gui, Player p, int id, int[] slots, ItemStack from, ItemStack[] to, long period, boolean each) {
        Plugin plugin = Plugin.getProject();
        requireNonNull(plugin, gui, slots, from, to);
        this.plugin = plugin;
        this.gui = gui;
        this.p = p;
        this.id = id;
        this.slots = slots;
        this.from = from;
        this.to = to;
        this.period = period;
        this.each = each;
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
     * Gets the player that is being shown the animation.
     *
     * @return Returns the player who has the {@link AnimatedGui} open.
     */
    public Player getPlayer() {
        return p;
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
        // New item
        ItemStack item;
        if (currSlot == 0 && !each)
            item = nextItem();
        else
            item = currentItem();
        // Set previous slot back
        gui.createItem(p, from, currentSlot());
        // Set new slot
        gui.createItem(p, item, nextSlot());
    }

    /**
     * Gets the active slot of the animation.
     *
     * @return The slot being shown in the animation.
     */
    private int currentSlot() {
        return slots[currSlot];
    }

    /**
     * Iterates and gets the next slot in the animation.
     *
     * @return The next slot to be shown in the animation.
     */
    private int nextSlot() {
        currSlot++;
        if (currSlot >= slots.length)
            currSlot = 0;
        return currentSlot();
    }

    /**
     * Gets the active item of the animation.
     *
     * @return The item being shown in the animation.
     */
    private ItemStack currentItem() {
        return to[currItem];
    }

    /**
     * Iterates and gets the next item in the animation.
     *
     * @return The next item to be shown in the animation.
     */
    private ItemStack nextItem() {
        currItem++;
        if (currItem >= to.length)
            currItem = 0;
        return currentItem();
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
            gui.createItem(p, from, slot);
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