package me.darrionat.pluginlib.guis;

import me.darrionat.pluginlib.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Set;

/**
 * Represents a {@link Gui} that is able to contain animations.
 * <p>
 * Animations are defined as slots within the gui that change items on a timer.
 */
public abstract class AnimatedGui extends Gui {
    /**
     * The animations within the gui.
     * <p>
     * The key is the id of the animation and the value is the animation.
     */
    private final HashMap<Integer, Animation> animations = new HashMap<>();
    /**
     * The amount of animations that have been added. The first animation added to the gui will have an id of {@code 1},
     * the second {@code two}, and on. By consequence, the gui will only be able to ever have {@code
     * Integer.MAX_VALUE}.
     */
    private int addCount = 0;

    /**
     * Creates a new {@link AnimatedGui}.
     *
     * @param plugin The plugin that this gui belongs to.
     * @param name   The title of the gui.
     * @param rows   The amount of rows the {@link Inventory} will have.
     */
    public AnimatedGui(Plugin plugin, String name, int rows) {
        super(plugin, name, rows);
    }

    /**
     * Creates a new animation within the gui.
     *
     * @param slots  The slots affected by the animation.
     * @param from   The first item when the animation is not currently on this slot.
     * @param to     The item to change the slot to when the animation occurs.
     * @param period The period between each cycle of the animation.
     * @return The id of the animation.
     */
    public int createAnimation(int[] slots, ItemStack from, ItemStack to, long period) {
        ItemStack[] toArr = new ItemStack[1];
        toArr[0] = to;
        return createAnimation(slots, from, toArr, period, false);
    }

    /**
     * Creates a new animation within the gui.
     *
     * @param slots  The slots affected by the animation.
     * @param from   The first item when the animation is not currently on this slot
     * @param to     The items to change the slot to when the animation occurs.
     * @param period The period between each cycle of the animation.
     * @param each   If {@code true} the {@code to} item will switch for every slot; otherwise, it will switch after all
     *               slots have been cycled through.
     * @return The id of the animation.
     */
    public int createAnimation(int[] slots, ItemStack from, ItemStack[] to, long period, boolean each) {
        return buildAnimation(++addCount, slots, from, to, period, each);
    }

    /**
     * Creates a new animation within the gui that will begin once the delay has passed.
     *
     * @param slots  The slots affected by the animation.
     * @param from   The first item when the animation is not currently on this slot.
     * @param to     The item to change the slot to when the animation occurs.
     * @param period The period between each cycle of the animation.
     * @param delay  The amount of ticks to wait before beginning the animation.
     * @return The id of the animation.
     */
    public int createDelayedAnimation(int[] slots, ItemStack from, ItemStack to, long period, long delay) {
        ItemStack[] toArr = new ItemStack[1];
        toArr[0] = to;
        return createDelayedAnimation(slots, from, toArr, period, delay, false);
    }

    /**
     * Creates a new animation within the gui that will begin once the delay has passed.
     *
     * @param slots  The slots affected by the animation.
     * @param from   The first item when the animation is not currently on this slot.
     * @param to     The items to change the slot to when the animation occurs.
     * @param period The period between each cycle of the animation.
     * @param delay  The amount of ticks to wait before beginning the animation. * @param each
     * @param each   If {@code true} the {@code to} item will switch for every slot; otherwise, it will switch after all
     *               slots have been cycled through.
     * @return The id of the animation.
     */
    public int createDelayedAnimation(int[] slots, ItemStack from, ItemStack[] to, long period, long delay, boolean each) {
        int id = ++addCount;
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                buildAnimation(id, slots, from, to, period, each), delay);
        return id;
    }

    /**
     * Builds an {@link Animation} within the gui.
     *
     * @param id     The id of the animation.
     * @param slots  The slots affected by the animation.
     * @param from   The first item when the animation is not currently on a slot
     * @param to     The items to change the active slot to when the animation occurs.
     * @param period The period between each cycle of the animation.
     * @param each   If {@code true} the {@code to} item will switch for every slot; otherwise, it will switch after
     *               all.
     * @return Returns the id of the animation.
     */
    private int buildAnimation(int id, int[] slots, ItemStack from, ItemStack[] to, long period, boolean each) {
        Animation toReturn = new Animation(plugin, this, id, slots, from, to, period, each);
        animations.put(id, toReturn);
        toReturn.start();
        return id;
    }

    /**
     * Gets an active animation by its id. If the animation has been scheduled yet not started, {@code null} will be
     * returned instead.
     *
     * @param id The id of the animation.
     * @return The animation contained within this gui with the given id; {@code null} if no such animation exists.
     */
    public Animation getAnimation(int id) {
        return animations.get(id);
    }

    /**
     * Removes and stops an animation in the gui.
     *
     * @param id The id of the animation.
     */
    public void removeAnimation(int id) {
        Animation animation = animations.get(id);
        animation.stop();
        animations.remove(id);
    }

    /**
     * Removes and stops all animations in the gui.
     */
    public void stopAnimations() {
        animations.forEach((id, animation) -> removeAnimation(id));
    }

    /**
     * Gets the set of all animation ids.
     *
     * @return All animation ids contained within the gui.
     */
    public Set<Integer> animationIdSet() {
        return animations.keySet();
    }
}