package ink.neokoni.lightTag.Commands.Functions;

import ink.neokoni.lightTag.LightTag;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class DeserializeItemToFile {
    private final File dataFolder = LightTag.getInstance().getDataFolder();
    public DeserializeItemToFile(CommandSender sender) {
        if(!(sender instanceof Player player)) {
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        File debugFile = new File(dataFolder, "debug.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(debugFile);
        yaml.set(String.valueOf(System.currentTimeMillis()), item);
        try {
            yaml.save(debugFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
