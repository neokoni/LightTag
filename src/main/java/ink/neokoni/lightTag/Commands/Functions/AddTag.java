package ink.neokoni.lightTag.Commands.Functions;

import ink.neokoni.lightTag.DataStorage.Tags;
import ink.neokoni.lightTag.Utils.TextUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class AddTag {
    public AddTag(String content, CommandSender sender) {
        int maxId = -1;

        for (String cur : Tags.getKeys(false)) {
            int cur_i = Integer.valueOf(cur);

            if(cur_i>maxId)maxId=cur_i;
        }
        int newTagId = maxId+1;

        Tags.set(newTagId+".type", "STATIC");
        Tags.set(newTagId+".content", content);

        sender.sendMessage(TextUtils.getFormatedLang("tag.added"));
    }

    public AddTag(String content, String banner, int delay, CommandSender sender) {
        int maxId = -1;

        for (String cur : Tags.getKeys(false)) {
            int cur_i = Integer.valueOf(cur);

            if(cur_i>maxId)maxId=cur_i;
        }
        int newTagId = maxId+1;

        List<String> tagContent = Arrays.stream(content.split(",")).toList();

        Tags.set(newTagId+".type", "ANIMATION");
        Tags.set(newTagId+".banner", banner);
        Tags.set(newTagId+".delay", delay);
        Tags.set(newTagId+".content", tagContent);

        sender.sendMessage(TextUtils.getFormatedLang("tag.added"));
    }
}
