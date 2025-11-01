package ink.neokoni.lightTag.Commands.Functions;

import ink.neokoni.lightTag.DataStorage.Languages;
import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.DataStorage.Tags;
import ink.neokoni.lightTag.Utils.TagUtils;
import ink.neokoni.lightTag.Utils.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SendPlayerTagList {
    public SendPlayerTagList(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(TextUtils.getFormatedLang("system.player-only"));
            return;
        }

        List<Integer> originData = PlayerDatas.getPlayerData().getIntegerList(player.getUniqueId()+".owns");
        List<Integer> ownedTags = new ArrayList<>();

        for (int i : originData) {
            if (i > -1) {
                ownedTags.add(i);
            }
        }

        Component head = TextUtils.getFormatedLang("list.head", "{total}", String.valueOf(ownedTags.size()));

        if (ownedTags.isEmpty()) {
            player.sendMessage(TextUtils.getFormatedLang("list.nothing"));
            return;
        }

        player.sendMessage(head);
        for (int i : ownedTags) {
            Component tagView = TagUtils.getViewById(i);
            String type = Tags.getTags().getString(i+".type");
            boolean isAnimation = (type != null && type.equals("ANIMATION"));
            Component tagType = isAnimation?MiniMessage.miniMessage().deserialize(
                    Languages.getLanguages().getString("tag.type-animation")) :
                    MiniMessage.miniMessage().deserialize(Languages.getLanguages().getString("tag.type-static"));

            TextReplacementConfig replaceId = TextReplacementConfig.builder().matchLiteral("{TagId}").replacement(String.valueOf(i)).build();
            TextReplacementConfig replaceType = TextReplacementConfig.builder().matchLiteral("{TagType}").replacement(tagType).build();
            TextReplacementConfig replaceView = TextReplacementConfig.builder().matchLiteral("{TagView}").replacement(tagView).build();

            StringBuilder hover_ori = new StringBuilder();
            for (String s : Languages.getLanguages().getStringList("list.hover")) {
                hover_ori.append(s);
            }

            Component hover = MiniMessage.miniMessage().deserialize(hover_ori.toString())
                    .replaceText(replaceId).replaceText(replaceType).replaceText(replaceView);

            player.sendMessage(MiniMessage.miniMessage().deserialize(Languages.getLanguages().getString("list.format"))
                            .replaceText(replaceId).replaceText(replaceType).replaceText(replaceView)
                            .hoverEvent(HoverEvent.showText(hover))
                            .clickEvent(ClickEvent.runCommand("/lighttag:ltag set "+i)));
        }
    }
}
