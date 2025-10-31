package ink.neokoni.lightTag.GUIs;

import ink.neokoni.lightTag.DataStorage.Caches;
import ink.neokoni.lightTag.DataStorage.Tags;
import ink.neokoni.lightTag.GUIs.Base.ChestMenu;
import ink.neokoni.lightTag.Utils.ItemActionExecutor;
import ink.neokoni.lightTag.Utils.TagUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.List;

public class BuyTagGUI {
    private Player player;
    private ChestMenu menu;
    private int cur_page;
    public BuyTagGUI(Player player, int page) {
        this.player = player;
        menu = new ChestMenu(6);
        cur_page = page;

        ItemStack panel = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta panelMeta = panel.getItemMeta();
        panelMeta.displayName(Component.text(""));
        panel.setItemMeta(panelMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.displayName(Component.text("返回上一页"));
        back.setItemMeta(backMeta);

        ItemStack previous = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta previousMeta = (PotionMeta) previous.getItemMeta();
        previousMeta.displayName(Component.text("上一页"));
        previousMeta.setBasePotionType(PotionType.SLOW_FALLING);
        previous.setItemMeta(previousMeta);
        previous.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);

        ItemStack next = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta nextMeta = (PotionMeta) next.getItemMeta();
        nextMeta.displayName(Component.text("下一页"));
        nextMeta.setBasePotionType(PotionType.WIND_CHARGED);
        next.setItemMeta(nextMeta);
        next.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);

        for (int i =0;i <9; i++) {
            menu.put(panel);
        }
        List<Integer> panels = List.of(9, 17, 18, 26, 27, 35, 36, 44, 46, 47, 49, 51, 52,53, 54);
        for (int i =0;i <menu.getSize(); i++) {
            if (panels.contains(i)) {
                menu.put(panel, i);
            }
        }

        menu.put(back, 45);
        menu.setItemActions(back, List.of("OpenPage:MainGUI"));
        menu.put(previous, 48);
        menu.put(next, 50);

        YamlConfiguration tags = Tags.getTags();
        for (String s : tags.getKeys(false)) {
            int id = Integer.valueOf(s);
            Component tagView = TagUtils.getViewById(id);
            String type = Tags.getTags().getString(id+".type");
            boolean isAnimation = (type != null && type.equals("ANIMATION"));

            ItemStack tagItem = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = tagItem.getItemMeta();
            meta.displayName(tagView);
            if (isAnimation)meta.setEnchantmentGlintOverride(true);
            meta.lore(List.of(
                    MiniMessage.miniMessage().deserialize("称号: ").append(tagView),
                    MiniMessage.miniMessage().deserialize(isAnimation?"<yellow>动态称号":"<red>静态称号"),
                    MiniMessage.miniMessage().deserialize("ID: "+id)));
            tagItem.setItemMeta(meta);
            menu.put(tagItem);
        }

        menu.setTitle("<yellow> 购买称号");

    }
    public void open() {
        menu.open(player);
        Caches.buyTagGUI.put(menu.getInv(), this);
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
        new BuyTagGUI(player, cur_page+1).open();
    }

    public void previous() {
        new BuyTagGUI(player, cur_page-1).open();
    }
}
