package ink.neokoni.lightTag.Commands.Functions;

import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.PAPIs.PAPIsCore;
import ink.neokoni.lightTag.Utils.TextUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearTag {
    public ClearTag(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(TextUtils.getFormatedLang("system.player-only"));
            return;
        }

        if (!player.hasPermission("lighttag.clear")) {
            player.sendMessage(TextUtils.getFormatedLang("system.no-perms"));
            return;
        }

        PlayerDatas.set(player.getUniqueId()+".using", -1);

        PAPIsCore.clear(player);
        player.sendMessage(TextUtils.getFormatedLang("tag.cleared"));
    }
}
