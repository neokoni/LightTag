package ink.neokoni.lightTag.Utils;

import ink.neokoni.lightTag.DataStorage.Languages;
import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.DataStorage.Tags;
import ink.neokoni.lightTag.DataStorage.Templates;
import ink.neokoni.lightTag.LightTag;
import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TagUtils {
    public static Component[] getTagContentById(int id) {
        YamlConfiguration tagsInfo = Tags.getTags();
        if (tagsInfo.getString(id+".content")==null) {
            return null;
        }

        String tagType = tagsInfo.getString(id+".type");
        switch (tagType) {
            case "STATIC" :{
                return new Component[]{LegacyComponentSerializer.legacyAmpersand().deserialize(tagsInfo.getString(id+".content"))};
            }
            case "ANIMATION" : {
                List<String> frame = tagsInfo.getStringList(id+".content");
                Component frameComponent[] = new Component[frame.size()];
                for (int i =0;i< frame.size();i++) {
                    frameComponent[i] = LegacyComponentSerializer.legacyAmpersand().deserialize(frame.get(i));
                }
                return frameComponent;
            }
            default: {
                List<String> s = new ArrayList<>();
                String singleContent = tagsInfo.getString(id+".content");

                if (singleContent != null) {
                    return new Component[]{LegacyComponentSerializer.legacyAmpersand().deserialize(singleContent)};
                }

                s = tagsInfo.getStringList(id+".content");
                Component components[] = new Component[s.size()];
                for (int i = 0; i < s.size(); i++) {
                    components[i] = LegacyComponentSerializer.legacyAmpersand().deserialize(s.get(i));
                }
                return components;
            }
        }
    }
    
    public static String getTypeById(int id) {
        String type = Tags.getTags().getString(id+".type");
        return type==null?"null" : type;
    }
    
    public static Component getViewById(int id) {
        String type = getTypeById(id);
        switch (type) {
            case "STATIC": {
                return LegacyComponentSerializer.legacyAmpersand().deserialize(Tags.getTags().getString(id+".content"));
            }
            case "ANIMATION": {
                return LegacyComponentSerializer.legacyAmpersand().deserialize(Tags.getTags().getString(id+".banner"));
            }
            default: {
                String banner = Tags.getTags().getString(id+".banner");
                try {
                    banner = Tags.getTags().getStringList(id+".content").getFirst();
                } catch (NoSuchElementException e) {
                    LightTag.getInstance().getLogger().warning("Tag id: "+id+" not have type and not animation");
                }
                if (banner==null) {
                    banner = Tags.getTags().getString(id+".content");
                }
                if (banner!=null) {
                    return LegacyComponentSerializer.legacyAmpersand().deserialize(banner);
                }
                return Component.text("null"); // i not want null pointer
            }
        }
    }

    public static Pair<ItemStack, List<String>> getTagItem(String template, int id) {
        ConfigurationSection templateInfo = Templates.getTemplates().getConfigurationSection(template + ".tag");
        ItemStack item;
        List<String> actions = new ArrayList<>();

        if (templateInfo.isSet("item")) {
            item = templateInfo.getItemStack("item");
        } else {
            item = new ItemStack(Material.valueOf(templateInfo.getString("material")));
        }
        if(item==null)item=new ItemStack(Material.NAME_TAG);

        ItemMeta meta = item.getItemMeta();
        List<String> lore_ori = templateInfo.getStringList("lore");
        List<Component> lore = new ArrayList<>();

        String animationTag = Languages.getLanguages().getString("tag.type-animation");
        String staticTag = Languages.getLanguages().getString("tag.type-static");
        if (animationTag==null)animationTag="null";
        if (staticTag==null)staticTag="null";

        Component tagType = getTypeById(id).equals("ANIMATION")?
                MiniMessage.miniMessage().deserialize(animationTag) :
                MiniMessage.miniMessage().deserialize(staticTag);
        TextReplacementConfig replaceType = TextReplacementConfig.builder().matchLiteral("{TagType}").replacement(tagType).build();
        TextReplacementConfig replaceView = TextReplacementConfig.builder().matchLiteral("{TagView}").replacement(getViewById(id)).build();
        TextReplacementConfig replaceId = TextReplacementConfig.builder().matchLiteral("{TagId}").replacement(String.valueOf(id)).build();
        TextReplacementConfig replacePrice = TextReplacementConfig.builder().matchLiteral("{TagPrice}").replacement(String.valueOf(getPrice(id))).build();

        lore_ori.forEach(s-> {
            Component result = MiniMessage.miniMessage().deserialize(s);
            lore.add(result.replaceText(replaceType).replaceText(replaceView).replaceText(replaceId).replaceText(replacePrice));
        });
        Component name = MiniMessage.miniMessage().deserialize(templateInfo.getString("title"))
                .replaceText(replaceType).replaceText(replaceId).replaceText(replaceView);
        meta.displayName(name);
        meta.lore(lore);
        if (getTypeById(id).equals("ANIMATION"))meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        if (templateInfo.isSet("actions")) {
            templateInfo.getStringList("actions").forEach(s -> {
                actions.add(s.replace("{TagId}", String.valueOf(id)));
            });
        }

        return Pair.of(item, actions);
    }

    public static int getPlayerTotal(Player player) {
        return PlayerDatas.getPlayerData().getIntegerList(player.getUniqueId()+".owns").stream().filter(i->i> -1).toList().size();
    }

    public static boolean canBuy(int id) {
        return Tags.getTags().isSet(id+".price");
    }

    public static double getPrice(int id) {
        if (!canBuy(id))return 0.00;
        return Tags.getTags().getDouble(id+".price");
    }
}


