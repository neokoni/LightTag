package ink.neokoni.lightTag.Commands.Functions;

import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.LightTag;
import ink.neokoni.lightTag.Utils.TagUtils;
import ink.neokoni.lightTag.Utils.TextUtils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class BuyTag {
    public BuyTag(Player player, int id) {
        if (!LightTag.hasEco) { // not install vault plugin, we cant use it
            player.sendMessage(TextUtils.getFormatedLang("system.no-vault"));
            return;
        }
        if (!TagUtils.canBuy(id)) { // this tag not for sale
            player.sendMessage(TextUtils.getFormatedLang("buy.no-sale"));
            return;
        }
        if (TagUtils.isPlayerHave(player, id)) { // player already have this tag
            player.sendMessage(TextUtils.getFormatedLang("buy.have"));
            return;
        }
        double price = TagUtils.getPrice(id);
        Economy eco = LightTag.getEcon();
        if (!eco.has(player, price)) { // player have not enough money to buy
            player.sendMessage(TextUtils.getFormatedLang("buy.no-money"));
            return;
        }
        EconomyResponse response = eco.withdrawPlayer(player, price);
        if (response.equals(EconomyResponse.ResponseType.NOT_IMPLEMENTED)) { // vault not be manage?
            player.sendMessage(TextUtils.getFormatedLang("buy.no-implement-error"));
        } else if (response.equals(EconomyResponse.ResponseType.FAILURE)) {
            player.sendMessage(TextUtils.getFormatedLang("buy.unknow-error"));
        } else if (response.equals(EconomyResponse.ResponseType.SUCCESS)) {
            YamlConfiguration playerData = PlayerDatas.getPlayerData();
            List<Integer> owns = playerData.getIntegerList(player.getUniqueId()+"owns");
            owns.add(id);
            playerData.set(player.getUniqueId()+".owns", owns);
            PlayerDatas.savePlayerData(playerData);
            player.sendMessage(TextUtils.getFormatedLang("buy.success"));
        }

    }
}
