package ink.neokoni.lightTag.GUIs;

import ink.neokoni.lightTag.DataStorage.Caches;
import ink.neokoni.lightTag.DataStorage.Templates;
import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.GUIs.Base.ChestMenu;
import ink.neokoni.lightTag.GUIs.Base.Template;
import ink.neokoni.lightTag.Utils.ItemActionExecutor;
import ink.neokoni.lightTag.Utils.ItemPagesUtils;
import ink.neokoni.lightTag.Utils.TagUtils;
import org.bukkit.configuration.file.YamlConfiguration;
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
        menu = new Template(Templates.getTemplates(), "set").get();
        cur_page = page;

        YamlConfiguration data = PlayerDatas.getPlayerData();
        List<Integer> owns = data.getIntegerList(this.player.getUniqueId()+".owns");
        if (maxPage==0)maxPage=ItemPagesUtils.getMaxPage(owns, menu.getFreeSlot().size());
        if (page>maxPage)cur_page=maxPage;
        if (page<1)cur_page=1;

        if (!owns.isEmpty()) {
            List<Integer> displayId = ItemPagesUtils.getThisPageIds(owns, menu.getFreeSlot().size(), cur_page);

            for (int i : displayId) {
                ItemStack tagItem = TagUtils.getTagItem("set", i);

                menu.put(tagItem);
                menu.setItemActions(tagItem, List.of("SetTag:"+i));
            }
        }

        menu.setTitle("<yellow>设置称号");
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
        new SetTagGUI(player, cur_page+1).open();
    }

    public void previous() {
        new SetTagGUI(player, cur_page-1).open();
    }

}
