package me.darrionat.pluginlib;

import org.bukkit.plugin.java.JavaPlugin;

import me.darrionat.pluginlib.guis.GuiHandler;
import me.darrionat.pluginlib.guis.GuiManager;
import me.darrionat.pluginlib.utils.Utils;

/**
 * Represents a {@link JavaPlugin}.
 */
public abstract class Plugin extends JavaPlugin implements IPlugin {
	private static Plugin instance;

	private GuiHandler guiHandler;

	/**
	 * Ran when the plugin is enabled. This method cannot be overridden by a
	 * subclass.
	 * 
	 * @see Plugin#initPlugin()
	 */
	@Override
	public final void onEnable() {
		instance = this;
		guiHandler = new GuiManager(this);
		initPlugin();
	}

	/**
	 * {@inheritDoc}
	 */
	public final GuiHandler getGuiHandler() {
		return guiHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	public void log(String s) {
		System.out.println(Utils.chat("[" + getName() + "] " + s));
	}

	/**
	 * Gets the current project of the {@link Plugin}.
	 * 
	 * @return Returns an instance of this {@link Plugin}.
	 */
	public static final Plugin getProject() {
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void onDisable();
}