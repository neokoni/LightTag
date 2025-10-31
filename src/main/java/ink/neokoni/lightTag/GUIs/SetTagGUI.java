package ink.neokoni.lightTag.GUIs;

import ink.neokoni.lightTag.DataStorage.Caches;
import ink.neokoni.lightTag.DataStorage.Templates;
import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.DataStorage.Tags;
import ink.neokoni.lightTag.GUIs.Base.ChestMenu;
import ink.neokoni.lightTag.GUIs.Base.Template;
import ink.neokoni.lightTag.Utils.ItemActionExecutor;
import ink.neokoni.lightTag.Utils.TagUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SetTagGUI {
    private Player player;
    private ChestMenu menu;
    private int cur_page;
    public SetTagGUI(Player player, int page) {
        this.player = player;
        menu = new Template(Templates.getTemplates(), "set").get();
        cur_page = page;

        YamlConfiguration data = PlayerDatas.getPlayerData();
        List<Integer> owns = data.getIntegerList(this.player.getUniqueId()+".owns");

        for (int i : owns) {
            ItemStack tagItem = new ItemStack(Material.NAME_TAG);
            Component tagView = TagUtils.getViewById(i);
            String type = Tags.getTags().getString(i+".type");
            boolean isAnimation = (type != null && type.equals("ANIMATION"));

            ItemMeta meta = tagItem.getItemMeta();
            meta.displayName(tagView);
            if (isAnimation)meta.setEnchantmentGlintOverride(true);
            meta.lore(List.of(
                    MiniMessage.miniMessage().deserialize("称号: ").append(tagView),
                    MiniMessage.miniMessage().deserialize(isAnimation?"<yellow>动态称号":"<red>静态称号"),
                    MiniMessage.miniMessage().deserialize("ID: "+i),
                    MiniMessage.miniMessage().deserialize(""),
                    MiniMessage.miniMessage().deserialize("点击使用")
            ));
            tagItem.setItemMeta(meta);

            menu.setItemActions(tagItem, List.of("SetTag:"+i));
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
        new SetTagGUI(player, cur_page+1);
    }

    public void previous() {
        new SetTagGUI(player, cur_page-1);
    }

}
