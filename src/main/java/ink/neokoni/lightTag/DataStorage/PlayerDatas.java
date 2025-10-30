package ink.neokoni.lightTag.DataStorage;

import ink.neokoni.lightTag.LightTag;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerDatas {
    private static File pluginFolder = LightTag.getInstance().getDataFolder();
    private static YamlConfiguration playerData = new YamlConfiguration();
    public static boolean isPlayerDataExist() {
        return new File(pluginFolder, "PlayerData.yml").exists();
    }

    public static void createDataFile() {
        try  {
            File file = new File(pluginFolder, "PlayerData.yml");
            playerData.save(file);
        } catch (IOException e) {
            LightTag.getInstance().getLogger().severe("FAILED TO CREATE PlayerData FILE!");
        }
    }

    public static void loadPlayerData() {
        if (!isPlayerDataExist()) {
            createDataFile();
        }

        File dataFile = new File(pluginFolder, "PlayerData.yml");
        playerData = YamlConfiguration.loadConfiguration(dataFile);
    }

    public static void writeToFile() {
        try {
            File dataFile = new File(pluginFolder, "PlayerData.yml");
            playerData.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void savePlayerData(YamlConfiguration data) {
        playerData = data;
    }

    public static YamlConfiguration getPlayerData() {
        return playerData;
    }
}
