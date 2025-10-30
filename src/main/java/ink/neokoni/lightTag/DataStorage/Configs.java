package ink.neokoni.lightTag.DataStorage;

import ink.neokoni.lightTag.LightTag;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Configs {
    private static File pluginFolder = LightTag.getInstance().getDataFolder();
    private static LightTag plugin = LightTag.getInstance();
    private static YamlConfiguration configs = new YamlConfiguration();
    public static boolean isConfigExist() {
        return new File(pluginFolder, "config.yml").exists();
    }

    public static void createConfigFile() {
        plugin.saveResource("config.yml", false);
    }

    public static void loadConfig() {
        if (!isConfigExist()) {
            createConfigFile();
        }

        File configFile = new File(pluginFolder, "config.yml");
        configs = YamlConfiguration.loadConfiguration(configFile);
    }


    public static YamlConfiguration getConfigs() {
        return configs;
    }
}
