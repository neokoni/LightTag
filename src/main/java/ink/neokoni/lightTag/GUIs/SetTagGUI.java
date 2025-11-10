package ink.neokoni.lightTag.GUIs;

import ink.neokoni.lightTag.DataStorage.Caches;
import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.GUIs.Base.ChestMenu;
import ink.neokoni.lightTag.GUIs.Base.Template;
import ink.neokoni.lightTag.Utils.ItemActionExecutor;
import ink.neokoni.lightTag.Utils.ItemPagesUtils;
import ink.neokoni.lightTag.Utils.TagUtils;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SetTagGUI {
    private Player player;
    private ChestMenu menu;
    private int cur_page;
    private int maxPage = 0;
    public SetTagGUI(Player player, int page) {
        this.player = player;
        menu = new Template("set", this.player).get();
        cur_page = page;

        List<Integer> owns = PlayerDatas.getIntegerList(this.player.getUniqueId()+".owns");
        if (maxPage==0)maxPage=ItemPagesUtils.getMaxPage(owns, menu.getFreeSlot().size());
        if (page>maxPage)cur_page=maxPage;
        if (page<1)cur_page=1;

        if (!owns.isEmpty()) {
            List<Integer> displayId = ItemPagesUtils.getThisPageIds(owns, menu.getFreeSlot().size(), cur_page);

            for (int i : displayId) {
                Pair<ItemStack, List<String>> tagItem = TagUtils.getTagItem("set", i);

                menu.put(tagItem.first());
                menu.setItemActions(tagItem.first(), tagItem.second());
            }
        }
    }

    public void open() {
        menu.open(player);
        Caches.setTagGUI.put(menu.getInv(), this);
    }

    public ChestMenu getMenu() {
        return menu;
    }

    public void handleClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        ItemStack item = event.getCurrentItem();

        if (item!=null) {
            ItemActionExecutor.run(menu, menu.getItemActions(item), player);
        }
    }

    public void next() {
        if (cur_page+1>maxPage)return;
        if (cur_page+1<1)return;

        new SetTagGUI(player, cur_page+1).open();
    }

    public void previous() {
        if (cur_page-1>maxPage)return;
        if (cur_page-1<1)return;

        new SetTagGUI(player, cur_page-1).open();
    }

}
