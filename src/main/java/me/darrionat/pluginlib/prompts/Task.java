package me.darrionat.pluginlib.prompts;

import org.bukkit.entity.Player;

/**
 * Represents a way to request and store information from a player.
 */
public abstract class Task {
    protected final Player p;

    public Task(Player p) {
        this.p = p;
    }

    /**
     * Gets the player this task belongs to.
     *
     * @return The active player of this task.
     */
    public Player getPlayer() {
        return p;
    }

    /**
     * Executed once the prompt is completed.
     *
     * @throws IllegalStateException thrown when the task is ran when not completed.
     * @see #complete()
     */
    public final void run() throws IllegalStateException {
        if (!complete())
            throw new IllegalStateException("Task is not complete");
        runTask();
    }

    /**
     * The action to take once the task is completed.
     */
    protected abstract void runTask();

    /**
     * The text that is prompted to the player when they first open the prompt.
     *
     * @return The task prompt text.
     */
    public abstract String promptText();

    /**
     * Executed once the prompt fails.
     *
     * @return The fail message.
     */
    public abstract String onFail();

    /**
     * Determines if an input is a valid input for the task currently.
     * <p>
     * If the required conditions of the task have been met.
     *
     * @param input The input text.
     * @return {@code true} if the task met its base conditions; otherwise {@code false}.
     */
    public abstract boolean valid(String input);

    /**
     * Determines if the task is complete.
     *
     * @return {@code true} if teh task is finished; otherwise {@code false}.
     */
    public abstract boolean complete();
}