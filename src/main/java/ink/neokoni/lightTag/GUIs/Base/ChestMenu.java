package ink.neokoni.lightTag.GUIs.Base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChestMenu {
    private Component title = Component.text("Untitled");
    private int size = 9;
    private ItemStack[] items;
    private Inventory menu;
    private Map<ItemStack, List<String>> itemActions = new HashMap<>();
    public ChestMenu(int row) {
        size = row*9;
        items = new ItemStack[size];
    }

    public void setTitle(String title) {
        this.title = MiniMessage.miniMessage().deserialize(title);
    }

    public void put(ItemStack item, int index) {
        items[index] = item;
    }

    public void put(ItemStack... Items) {
        int cur_slot = 0;
        for (ItemStack item :Items) {
            while(items[cur_slot]!=null&&cur_slot<55) {
                cur_slot++;
            }
            items[cur_slot] = item;
            cur_slot++;
        }
    }

    public void setItemActions(Map<ItemStack, List<String>> actions) {
        itemActions = actions;
    }

    public void setItemActions(ItemStack item, List<String> actions) {
        itemActions.put(item, actions);
    }

    public void fillFreeWith(ItemStack item) {
        for (int i = 0; i < size; i++) {
            if (items[i]!=null) {
                continue;
            }

            items[i] = item;
        }
    }

    public void open(Player player) {
        menu = Bukkit.createInventory(null, size, title);
        for (int i = 0; i < size; i++) {
            menu.setItem(i, items[i]);
        }

        player.openInventory(menu);
    }

    public Inventory getInv() {
        return menu;
    }

    public int getSize() {
        return size;
    }

    public List<String> getItemActions(ItemStack item) {
        return itemActions.get(item);
    }
}
