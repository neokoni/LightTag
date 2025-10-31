package ink.neokoni.lightTag.Commands.Functions;

import ink.neokoni.lightTag.DataStorage.*;
import ink.neokoni.lightTag.PAPIs.PAPIsCore;
import ink.neokoni.lightTag.Utils.TextUtils;
import org.bukkit.command.CommandSender;

public class Reload {
    public Reload(CommandSender sender) {
        Languages.loadLanguage();
        Tags.loadTags();
        Templates.loadTemplates();
        PlayerDatas.loadPlayerData();

        PAPIsCore.clearCache();

        if (sender!=null) {
            sender.sendMessage(TextUtils.getFormatedLang("system.reload-success"));
        }
    }
}
