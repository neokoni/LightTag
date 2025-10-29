package ink.neokoni.lightTag.GUIs;

import ink.neokoni.lightTag.DataStorage.Caches;
import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.GUIs.Base.ChestMenu;
import ink.neokoni.lightTag.Utils.Item.ItemCustomDataUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class MainGUI {
    private Player player;
    private ChestMenu menu;
    public MainGUI(Player player) {
        this.player = player;
        menu = new ChestMenu(3);

        YamlConfiguration data = PlayerDatas.getPlayerData();

        ItemStack setTag = new ItemStack(Material.NAME_TAG);
        ItemMeta setTagMeta = setTag.getItemMeta();
        setTagMeta.displayName(Component.text("设置称号"));
        setTagMeta.lore(List.of(
                Component.text("点击选择要设置的称号")
        ));
        setTag.setItemMeta(setTagMeta);

        ItemStack buyTag = new ItemStack(Material.EMERALD);
        ItemMeta buyTagMeta = buyTag.getItemMeta();
        buyTagMeta.displayName(Component.text("购买称号"));
        buyTagMeta.lore(List.of(
                Component.text("浏览目前可购买的所有称号")
        ));
        buyTag.setItemMeta(buyTagMeta);

        ItemStack almanac = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta almanacMeta = almanac.getItemMeta();
        almanacMeta.displayName(Component.text("称号图鉴"));
        almanacMeta.lore(List.of(
                Component.text("浏览此服务器上存在的所有称号")
        ));
        almanac.setItemMeta(almanacMeta);

        ItemStack playerInfo = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta playerInfoMeta = playerInfo.getItemMeta();
        playerInfoMeta.displayName(Component.text("个人信息"));
        playerInfoMeta.lore(List.of(
                Component.text("已拥有X个称号")
        ));
        SkullMeta skullMeta = (SkullMeta) playerInfoMeta;
        skullMeta.setPlayerProfile(player.getPlayerProfile());
        playerInfo.setItemMeta(skullMeta);

        ItemStack clear = new ItemStack(Material.BARRIER);
        ItemMeta clearMeta = clear.getItemMeta();
        clearMeta.displayName(Component.text("停用称号"));
        clearMeta.lore(List.of(
                Component.text("点击取消使用称号")
        ));
        clear.setItemMeta(clearMeta);

        ItemStack panel = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta panelMeta = panel.getItemMeta();
        panelMeta.displayName(Component.text(""));
        panel.setItemMeta(panelMeta);

        menu.put(setTag, 10);
        menu.setCustomData(10, "OpenPage:SetTagGUI");

        menu.put(buyTag, 12);
        menu.setCustomData(12, "OpenPage:BuyTagGUI");

        menu.put(almanac, 14);
        menu.setCustomData(14, "OpenPage:AlmanacGUI");

        menu.put(playerInfo, 16);
        menu.put(clear, 26);

        menu.fillFreeWith(panel);

        menu.setTitle("<yellow>LightTag面板");
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
        if (item==null) {
            return;
        }

        String newPage = ItemCustomDataUtils.getString(item, menu, "OpenPage");
        if (newPage==null)return;

        switch (newPage) {
            case "SetTagGUI": {
                new SetTagGUI(player).open();
                return;
            }
            case "BuyTagGUI": {
                new BuyTagGUI(player).open();
                return;
            }
            case "AlmanacGUI": {
                new AlmanacGUI(player).open();
                return;
            }
        }
    }
}
