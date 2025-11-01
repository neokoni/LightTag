package ink.neokoni.lightTag.GUIs;

import ink.neokoni.lightTag.DataStorage.Caches;
import ink.neokoni.lightTag.DataStorage.Tags;
import ink.neokoni.lightTag.GUIs.Base.ChestMenu;
import ink.neokoni.lightTag.GUIs.Base.Template;
import ink.neokoni.lightTag.Utils.ItemActionExecutor;
import ink.neokoni.lightTag.Utils.ItemPagesUtils;
import ink.neokoni.lightTag.Utils.TagUtils;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AlmanacGUI {
    private Player player;
    private ChestMenu menu;
    private int cur_page = 1;
    private int maxPage = 0;
    public AlmanacGUI(Player player, int page) {
        this.player = player;
        menu = new Template("almanac", this.player).get();
        cur_page = page;

        YamlConfiguration tags = Tags.getTags();
        Set<String> keys = tags.getKeys(false);
        List<Integer> allTags = new ArrayList<>();
        keys.forEach(s-> {
            allTags.add(Integer.valueOf(s));
        });

        if (maxPage==0)maxPage=ItemPagesUtils.getMaxPage(allTags, menu.getFreeSlot().size());
        if (page>maxPage)cur_page=maxPage;
        if (page<1)cur_page=1;

        if (!allTags.isEmpty()) {
            List<Integer> displayId = ItemPagesUtils.getThisPageIds(allTags, menu.getFreeSlot().size(), cur_page);

            for (int i : displayId) {
                Pair<ItemStack, List<String>> tagItem = TagUtils.getTagItem("buy", i);

                menu.put(tagItem.first());
                menu.setItemActions(tagItem.first(), tagItem.second());
            }
        }
    }

    public void open() {
        menu.open(player);
        Caches.almanacGUI.put(menu.getInv(), this);
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

    public void next() {
        if (cur_page+1>maxPage)return;
        if (cur_page+1<1)return;

        new AlmanacGUI(player, cur_page+1).open();
    }

    public void previous() {
        if (cur_page-1>maxPage)return;
        if (cur_page-1<1)return;

        new AlmanacGUI(player, cur_page-1).open();
    }
}
