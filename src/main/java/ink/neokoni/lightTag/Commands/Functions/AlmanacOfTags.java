package ink.neokoni.lightTag.Commands.Functions;

import ink.neokoni.lightTag.DataStorage.Tags;
import ink.neokoni.lightTag.Utils.TagUtils;
import ink.neokoni.lightTag.Utils.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class AlmanacOfTags {
    public AlmanacOfTags(CommandSender sender) {
        YamlConfiguration tags = Tags.getTags();

        sender.sendMessage(
                TextUtils.getFormatedLang("almanac.overview", "{total}",
                        String.valueOf(tags.getKeys(false).size())));

        for (String i : tags.getKeys(false)) {
            int cur = Integer.valueOf(i);
            Component tagView = TagUtils.getViewById(cur);

            String type = Tags.getTags().getString(i+".type");
            boolean isAnimation = (type != null && type.equals("ANIMATION"));

            Component hover = MiniMessage.miniMessage().deserialize("称号: ").append(tagView).appendNewline()
                    .append(MiniMessage.miniMessage().deserialize(isAnimation?"<yellow>动态称号":"<red>静态称号").appendNewline())
                    .append(MiniMessage.miniMessage().deserialize("ID: "+i));

            sender.sendMessage(Component.text("- ID:"+i+" ").append(
                    TagUtils.getViewById(cur)
                            .hoverEvent(HoverEvent.showText(hover))
            ));
        }
    }
}
