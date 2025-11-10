package ink.neokoni.lightTag.Commands.Functions;

import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.Utils.TagUtils;
import ink.neokoni.lightTag.Utils.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GiveTag {
    public GiveTag(CommandSender sender, List<Player> players, int id) {
        int added = 0;

        TextReplacementConfig replaceTag = TextReplacementConfig.builder()
                .matchLiteral("{tag}")
                .replacement(TagUtils.getViewById(id))
                .build();

        for (Player player : players) {
            if (PlayerDatas.getIntegerList(player.getUniqueId()+".owns").contains(id)) {
                Component exist = TextUtils.getFormatedLang("give.exist", "{player}", player.getName());
                sender.sendMessage(exist.replaceText(replaceTag));
                continue;
            }
            List<Integer> owned = new java.util.ArrayList<>(PlayerDatas.getIntegerList(player.getUniqueId()+".owns"));
            owned.add(id);
            PlayerDatas.set(player.getUniqueId()+".owns", owned);
            sender.sendMessage(
                    TextUtils.getFormatedLang("give.successes", "{player}", player.getName())
                    .replaceText(replaceTag));
            player.sendMessage(
                    TextUtils.getFormatedLang("give.added")
                    .replaceText(replaceTag)
            );
            added++;
        }

        sender.sendMessage(
                TextUtils.getFormatedLang("give.result", "{total}", String.valueOf(added))
                .replaceText(replaceTag));
    }
    public GiveTag(CommandSender sender, Player player, int id) {
        int added = 0;

        TextReplacementConfig replaceTag = TextReplacementConfig.builder()
                .match("{tag}")
                .replacement(TagUtils.getViewById(id))
                .build();

        if (PlayerDatas.getIntegerList(player.getUniqueId()+".owns").contains(id)) {
            Component exist = TextUtils.getFormatedLang("give.exist", "{player}", player.getName());
            sender.sendMessage(exist.replaceText(replaceTag));

            sender.sendMessage(
                    TextUtils.getFormatedLang("give.result", "{total}", String.valueOf(added))
                    .replaceText(replaceTag));
            return;
        }
        List<Integer> owned = new java.util.ArrayList<>(PlayerDatas.getIntegerList(player.getUniqueId()+".owns"));
        owned.add(id);
        sender.sendMessage(
                TextUtils.getFormatedLang("give.successes", "{player}", player.getName())
                        .replaceText(replaceTag));
        player.sendMessage(
                TextUtils.getFormatedLang("give.added")
                        .replaceText(replaceTag)
        );
        added++;

        PlayerDatas.set(player.getUniqueId()+".owns", owned);

        sender.sendMessage(
                TextUtils.getFormatedLang("give.result", "{total}", String.valueOf(added))
                        .replaceText(replaceTag));
    }

}
