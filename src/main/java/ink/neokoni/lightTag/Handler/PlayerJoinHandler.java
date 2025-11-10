package ink.neokoni.lightTag.Handler;

import ink.neokoni.lightTag.DataStorage.Configs;
import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.LightTag;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoinHandler implements Listener {
    public PlayerJoinHandler(LightTag plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        YamlConfiguration config = Configs.getConfigs();

        if (!PlayerDatas.isSet(player.getUniqueId()+".using")) { // set init tag for new player if enabled
            PlayerDatas.set(player.getUniqueId()+".using", config.getInt("init-tag"));
        }

        int using = PlayerDatas.getInt(player.getUniqueId()+".using");
        List<Integer> ownedTags = PlayerDatas.getIntegerList(player.getUniqueId()+".owns");

        if (!ownedTags.contains(using)) { // player is using a not owned tag?
            PlayerDatas.set(player.getUniqueId()+".using", config.getInt("init-tag"));
        }

        if (config.getInt("init-tag") > -1 &&
                // if not own any tags or not have init tag, give then
                (ownedTags.isEmpty() || !ownedTags.contains(config.getInt("init-tag"))) ) {
            List<Integer> updatedTags = new ArrayList<>(ownedTags);
            updatedTags.add(config.getInt("init-tag"));
            PlayerDatas.set(player.getUniqueId()+".owns", updatedTags);
        }
    }
}
