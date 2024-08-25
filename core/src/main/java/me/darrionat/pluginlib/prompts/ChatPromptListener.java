package me.darrionat.pluginlib.prompts;

import me.darrionat.pluginlib.Plugin;
import me.darrionat.pluginlib.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a {@link Listener} that handles chat prompts.
 */
public class ChatPromptListener implements Listener {
    private static final Set<Task> ACTIVE_TASKS = new HashSet<>();
    private static boolean registered = false;
    private final Plugin plugin;

    public ChatPromptListener(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        registered = true;
    }

    /**
     * Adds an incomplete task to the list of active tasks.
     *
     * @param task The task to add.
     * @throws IllegalStateException Thrown when the task argument is completed.
     */
    public static void add(Task task) {
        if (!registered)
            new ChatPromptListener(Plugin.getProject());
        if (task.complete())
            throw new IllegalStateException("A completed task cannot be added");
        // Remove other tasks that the player is in, at most there can only be 1 other
        Player p = task.getPlayer();
        Task toRemove = null;
        for (Task t : ACTIVE_TASKS)
            if (t.getPlayer() == p) {
                toRemove = t;
                break;
            }
        if (toRemove != null) ACTIVE_TASKS.remove(toRemove);
        ACTIVE_TASKS.add(task);
    }

    /**
     * Gets the active of the player given.
     *
     * @param p The player to get the task of.
     * @return The active task the player is involved in. If the player does not have an active task {@code null} will
     * be returned.
     */
    private Task getPlayerTask(Player p) {
        for (Task task : ACTIVE_TASKS)
            if (task.getPlayer() == p)
                return task;
        return null;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        Task task = getPlayerTask(p);
        if (task == null) return;

        String text = ChatColor.stripColor(e.getMessage());
        if (!task.valid(text)) {
            p.sendMessage(Utils.toColor(task.onFail()));
            return;
        }
        if (task.complete()) {
            Bukkit.getScheduler().runTask(plugin, task::run);
            ACTIVE_TASKS.remove(task);
        } else
            p.sendMessage(Utils.toColor(task.promptText()));
    }
}