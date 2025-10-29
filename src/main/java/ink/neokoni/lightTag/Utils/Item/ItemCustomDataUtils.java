package ink.neokoni.lightTag.Utils.Item;

import ink.neokoni.lightTag.GUIs.Base.ChestMenu;
import org.bukkit.inventory.ItemStack;

public class ItemCustomDataUtils {
    public static int getInt(ItemStack item, ChestMenu menu, String flag) {
        for (int i = 0; i<menu.getInv().getSize(); i++) {
            if (menu.getInv().getItem(i).equals(item)) {
                String data = menu.getCustomData(i);
                if (!data.split(":")[0].equals(flag)) {
                    continue;
                }
                try {
                    return Integer.valueOf(data.split(":")[1]);
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        }
        return -1;
    }

    public static String getString(ItemStack item, ChestMenu menu, String flag) {
        for (int i = 0; i<menu.getInv().getSize(); i++) {
            if (menu.getInv().getItem(i)==null) {
                continue;
            }
            if (menu.getInv().getItem(i).equals(item)) {
                String data = menu.getCustomData(i);
                if (data==null) {
                    continue;
                }
                if (!data.split(":")[0].equals(flag)) {
                    continue;
                }
                try {
                    return data.split(":")[1];
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }
}
