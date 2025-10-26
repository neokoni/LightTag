package ink.neokoni.lightTag.Commands.Functions;

import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.DataStorage.Tags;
import ink.neokoni.lightTag.Utils.TagUtils;
import ink.neokoni.lightTag.Utils.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class GiveTag {
    public GiveTag(CommandSender sender, List<Player> players, int id) {
        YamlConfiguration playerData = PlayerDatas.getPlayerData();
        YamlConfiguration tags = Tags.getTags();
        int added = 0;

        TextReplacementConfig replaceTag = TextReplacementConfig.builder()
                .matchLiteral("{tag}")
                .replacement(TagUtils.getViewById(id))
                .build();

        for (Player player : players) {
            if (playerData.getIntegerList(player.getUniqueId()+".owns").contains(id)) {
                Component exist = TextUtils.getFormatedLang("give.exist", "{player}", player.getName());
                sender.sendMessage(exist.replaceText(replaceTag));
                continue;
            }
            List<Integer> owned = playerData.getIntegerList(player.getUniqueId()+".owns");
            owned.add(id);
            playerData.set(player.getUniqueId()+".owns", owned);
            sender.sendMessage(
                    TextUtils.getFormatedLang("give.successes", "{player}", player.getName())
                    .replaceText(replaceTag));
            player.sendMessage(
                    TextUtils.getFormatedLang("give.added")
                    .replaceText(replaceTag)
            );
            added++;
        }

        PlayerDatas.savePlayerData(playerData);

        sender.sendMessage(
                TextUtils.getFormatedLang("give.result", "{total}", String.valueOf(added))
                .replaceText(replaceTag));
    }
    public GiveTag(CommandSender sender, Player player, int id) {
        YamlConfiguration playerData = PlayerDatas.getPlayerData();
        YamlConfiguration tags = Tags.getTags();
        int added = 0;

        TextReplacementConfig replaceTag = TextReplacementConfig.builder()
                .match("{tag}")
                .replacement(TagUtils.getViewById(id))
                .build();

        if (playerData.getIntegerList(player.getUniqueId()+".owns").contains(id)) {
            Component exist = TextUtils.getFormatedLang("give.exist", "{player}", player.getName());
            sender.sendMessage(exist.replaceText(replaceTag));

            sender.sendMessage(
                    TextUtils.getFormatedLang("give.result", "{total}", String.valueOf(added))
                    .replaceText(replaceTag));
            return;
        }
        List<Integer> owned = playerData.getIntegerList(player.getUniqueId()+".owns");
        owned.add(id);
        sender.sendMessage(
                TextUtils.getFormatedLang("give.successes", "{player}", player.getName())
                        .replaceText(replaceTag));
        player.sendMessage(
                TextUtils.getFormatedLang("give.added")
                        .replaceText(replaceTag)
        );
        added++;

        playerData.set(player.getUniqueId()+".owns", owned);
        PlayerDatas.savePlayerData(playerData);

        sender.sendMessage(
                TextUtils.getFormatedLang("give.result", "{total}", String.valueOf(added))
                        .replaceText(replaceTag));
    }

}
