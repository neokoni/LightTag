package ink.neokoni.lightTag.Handler;

import ink.neokoni.lightTag.DataStorage.Caches;
import ink.neokoni.lightTag.LightTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import org.bukkit.inventory.Inventory;

public class TagsInventoryHandlers implements Listener {
    public TagsInventoryHandlers(LightTag plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onClickInventory(InventoryClickEvent e) {
        Inventory clicked = e.getClickedInventory();
        if (clicked==null) { // not click inside inventory
            return;
        }
        if (!isOurInv(clicked)) {
            return;
        }
        e.setCancelled(true);

        if (Caches.almanacGUI.containsKey(clicked)) {
            Caches.almanacGUI.get(clicked).handleClick(e);
        } else if (Caches.setTagGUI.containsKey(clicked)) {
            Caches.setTagGUI.get(clicked).handleClick(e);
        } else if (Caches.mainGUI.containsKey(clicked)) {
            Caches.mainGUI.get(clicked).handleClick(e);
        } else if (Caches.buyTagGUI.containsKey(clicked)) {
            Caches.buyTagGUI.get(clicked).handleClick(e);
        }
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent e) {
        Inventory closed = e.getInventory();

        Caches.setTagGUI.remove(closed);
    }

    private boolean isOurInv(Inventory inv) {
        return Caches.setTagGUI.containsKey(inv) ||
                Caches.mainGUI.containsKey(inv) ||
                Caches.almanacGUI.containsKey(inv) ||
                Caches.buyTagGUI.containsKey(inv);
    }
}
