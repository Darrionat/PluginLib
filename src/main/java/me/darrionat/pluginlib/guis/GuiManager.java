package me.darrionat.pluginlib.guis;

import me.darrionat.pluginlib.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a {@link List} of all {@link Gui}s within a {@link Plugin} and
 * determines the outcome of an {@link InventoryClickEvent}.
 */
public class GuiManager implements GuiHandler, Listener {
    private final Set<Gui> registeredGuis = new HashSet<Gui>();
    private final Set<Gui> toRegisterGuis = new HashSet<Gui>();

    public GuiManager(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * {@inheritDoc}
     */
    public void registerGui(Gui gui) {
        try {
            registeredGuis.add(gui);
        } catch (ConcurrentModificationException e) {
            toRegisterGuis.add(gui);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void unregisterGui(Gui gui) {
        registeredGuis.remove(gui);
    }

    /**
     * {@inheritDoc}
     */
    public void openGui(Player p, Gui gui) {
        p.openInventory(gui.getInventory(p));
    }

    /**
     * Determines what to do when an {@link InventoryClickEvent} is ran.
     *
     * @param e The Event that is passed.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null)
            return;

        String title = e.getView().getTitle();
        for (Gui gui : registeredGuis) {
            if (!gui.getName().equals(title))
                return;
            if (!gui.allowsClick())
                e.setCancelled(true);
            if (!e.getInventory().equals(e.getClickedInventory()))
                return;
            gui.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getClick());
        }
        registeredGuis.addAll(toRegisterGuis);
        toRegisterGuis.clear();
    }
}