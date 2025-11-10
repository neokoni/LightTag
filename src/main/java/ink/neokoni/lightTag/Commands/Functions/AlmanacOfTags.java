package ink.neokoni.lightTag.Commands.Functions;

import ink.neokoni.lightTag.DataStorage.Languages;
import ink.neokoni.lightTag.DataStorage.Tags;
import ink.neokoni.lightTag.Utils.TagUtils;
import ink.neokoni.lightTag.Utils.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class AlmanacOfTags {
    public AlmanacOfTags(CommandSender sender) {
        sender.sendMessage(
                TextUtils.getFormatedLang("almanac.overview", "{total}",
                        String.valueOf(Tags.getKeys(false).size())));

        for (String i : Tags.getKeys(false)) {
            int cur = Integer.valueOf(i);
            Component tagView = TagUtils.getViewById(cur);

            String type = Tags.getString(i+".type");
            boolean isAnimation = (type != null && type.equals("ANIMATION"));
            Component tagType = isAnimation?MiniMessage.miniMessage().deserialize(
                    Languages.getLanguages().getString("tag.type-animation")) :
                    MiniMessage.miniMessage().deserialize(Languages.getLanguages().getString("tag.type-static"));

            TextReplacementConfig replaceId = TextReplacementConfig.builder().matchLiteral("{TagId}").replacement(i).build();
            TextReplacementConfig replaceType = TextReplacementConfig.builder().matchLiteral("{TagType}").replacement(tagType).build();
            TextReplacementConfig replaceView = TextReplacementConfig.builder().matchLiteral("{TagView}").replacement(tagView).build();

            StringBuilder hover_ori = new StringBuilder();
            for (String s : Languages.getLanguages().getStringList("almanac.hover")) {
                hover_ori.append(s);
            }

            Component hover = MiniMessage.miniMessage().deserialize(hover_ori.toString())
                    .replaceText(replaceId).replaceText(replaceType).replaceText(replaceView);

            sender.sendMessage(MiniMessage.miniMessage().deserialize(Languages.getLanguages().getString("almanac.format"))
                    .replaceText(replaceId).replaceText(replaceType).replaceText(replaceView)
                            .hoverEvent(HoverEvent.showText(hover)));
        }
    }
}
