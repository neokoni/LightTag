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
        Inventory inv = e.getInventory();
        if (!isOurInv(inv)) {
            return;
        }
        e.setCancelled(true);

        if (Caches.almanacGUI.containsKey(inv)) {
            Caches.almanacGUI.get(inv).handleClick(e);
        } else if (Caches.setTagGUI.containsKey(inv)) {
            Caches.setTagGUI.get(inv).handleClick(e);
        } else if (Caches.mainGUI.containsKey(inv)) {
            Caches.mainGUI.get(inv).handleClick(e);
        } else if (Caches.buyTagGUI.containsKey(inv)) {
            Caches.buyTagGUI.get(inv).handleClick(e);
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
