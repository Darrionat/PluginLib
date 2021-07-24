package me.darrionat.pluginlib.prompts;

/**
 * Represents a prompt which is handled by chat.
 */
public class ChatPrompt extends Prompt {
    public ChatPrompt(Task task) {
        super(task);
    }

    @Override
    public void openPrompt() {
        p.closeInventory();
        p.sendMessage(task.promptText());
        ChatPromptListener.add(task);
    }
}