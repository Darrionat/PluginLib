package me.darrionat.pluginlib.prompts;

import org.bukkit.entity.Player;

/**
 * Represents a way to request information from a player.
 */
public abstract class Prompt {
    /**
     * The task to be run when the prompt is complete.
     */
    protected final Task task;
    protected final Player p;

    /**
     * Constructs a new prompt.
     *
     * @param task The task of this prompt.
     */
    public Prompt(Task task) {
        this.task = task;
        this.p = task.getPlayer();
    }

    /**
     * The task of this prompt.
     *
     * @return The task.
     */
    public Task getTask() {
        return task;
    }

    /**
     * The player of this prompt.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return p;
    }

    /**
     * Opens the prompt for the player with this prompt's task.
     */
    public abstract void openPrompt();
}