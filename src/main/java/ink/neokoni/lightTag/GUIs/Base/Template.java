package ink.neokoni.lightTag.GUIs.Base;

import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.DataStorage.Templates;
import ink.neokoni.lightTag.Utils.TagUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class Template {
    private String title = "Untitled";
    private List<String> ui = new ArrayList<>();
    private int row = 3;
    private Map<String, ItemStack> items = new HashMap<>();
    private Map<ItemStack, List<String>> itemActions = new HashMap<>();
    private ChestMenu menu;

    private Component usingTagView;
    public Template(String template, Player player) {
        YamlConfiguration templates = Templates.getTemplates();
        if (!templates.getKeys(false).contains(template)) { // this gui not defined
            return;
        }
        title = templates.getString(template+".title");
        ui = templates.getStringList(template+".ui");
        row = ui.size();
        menu = new ChestMenu(row);

        if (PlayerDatas.getInt(player.getUniqueId()+".using")>=0) {
            usingTagView = TagUtils.getViewById(PlayerDatas.getInt(player.getUniqueId()+".using"));
        } else {
            usingTagView = Component.text("");
        }

        TextReplacementConfig replaceUsingTagView = TextReplacementConfig
                .builder().matchLiteral("{PlayerUsingTagView}").replacement(usingTagView).build();

        Set<String> names = templates.getConfigurationSection(template+".items").getKeys(false);
        for (String s:  names) {
            Material material = Material.valueOf(templates.getString(template+".items."+s+".material"));
            int amount = templates.getInt(template+".items."+s+".amount");
            List<String> lore_ori = templates.getStringList(template+".items."+s+".lore");
            String name = templates.getString(template+".items."+s+".title");
            List<String> flags = templates.getStringList(template+".items."+s+".flags");
            List<String> actions = templates.getStringList(template+".items."+s+".actions");

            ItemStack item;
            if (templates.isSet(template+".items."+s+".item")) {
                item = templates.getItemStack(template+".items."+s+".item");
            } else {
                item = new ItemStack(material);
                item.setAmount(amount);
            }

            ItemMeta meta = item.getItemMeta();
            List<Component> lore = new ArrayList<>();
            lore_ori.forEach(l -> {
                lore.add(MiniMessage.miniMessage().deserialize(
                        l.replace("{PlayerTagTotal}", String.valueOf(TagUtils.getPlayerTotal(player))))
                        .replaceText(replaceUsingTagView));
            });
            meta.lore(lore);
            meta.displayName(MiniMessage.miniMessage().deserialize(name));
            item.setItemMeta(meta);

            if (templates.getBoolean(template+".items."+s+".head")) {
                SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                skullMeta.setPlayerProfile(player.getPlayerProfile());
                item.setItemMeta(skullMeta);
            }

            flags.forEach(f -> {
                item.addItemFlags(ItemFlag.valueOf(f));
            });
            items.put(s, item);
            itemActions.put(item, actions);
        }

        for (int i = 0; i < ui.size(); i++) {
            String s_row = ui.get(i);
            for (int j = 0; j < 9; j++) {
                String s = String.valueOf(s_row.charAt(j));
                int cur_slot = i*9+j;

                if (s==null||s.equals("")||s.equals("-")) { //
                    continue;
                }

                if (items.containsKey(s)) {
                    menu.put(items.get(s), cur_slot);
                }
            }
        }

        menu.setTitle(title);
        menu.setItemActions(itemActions);
    }

    public ChestMenu get() {
        return menu;
    }
}
