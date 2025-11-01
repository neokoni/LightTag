package ink.neokoni.lightTag.Commands.Functions;

import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.PAPIs.PAPIsCore;
import ink.neokoni.lightTag.Utils.TextUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SetTag {
    public SetTag(CommandSender sender, int id) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(TextUtils.getFormatedLang("system.player-only"));
            return;
        }

        if (!player.hasPermission("lighttag.set")) {
            player.sendMessage(TextUtils.getFormatedLang("system.no-perms"));
            return;
        }

        YamlConfiguration playerData = PlayerDatas.getPlayerData();
        if (!playerData.getIntegerList(player.getUniqueId()+".owns").contains(id)&&id>=0) {
            player.sendMessage(TextUtils.getFormatedLang("tag.not-have"));
            return;
        }

        if (id < -1)id=-1;

        playerData.set(player.getUniqueId()+".using", id);
        PAPIsCore.clear(player);
        player.sendMessage(TextUtils.getFormatedLang("tag.successes"));
    }
}
