package ink.neokoni.lightTag.GUIs;

import ink.neokoni.lightTag.DataStorage.Caches;
import ink.neokoni.lightTag.GUIs.Base.ChestMenu;
import ink.neokoni.lightTag.GUIs.Base.Template;
import ink.neokoni.lightTag.Utils.ItemActionExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MainGUI {
    private Player player;
    private ChestMenu menu;
    public MainGUI(Player player) {
        this.player = player;
        menu = new Template("main", this.player).get();
    }

    public void open() {
        menu.open(player);
        Caches.mainGUI.put(menu.getInv(), this);
    }

    public void handleClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) {
            return;
        }
        ItemStack item = e.getCurrentItem();
        if (item!=null) {
            ItemActionExecutor.run(menu, menu.getItemActions(item), player);
        }
    }
}
