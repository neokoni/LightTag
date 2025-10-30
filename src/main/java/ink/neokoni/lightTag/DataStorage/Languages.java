package ink.neokoni.lightTag.DataStorage;

import ink.neokoni.lightTag.LightTag;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Languages {
    private static File pluginFolder = LightTag.getInstance().getDataFolder();
    private static YamlConfiguration language = new YamlConfiguration();
    public static boolean isLanguageExist() {
        return new File(pluginFolder, "lang.yml").exists();
    }

    public static void loadLanguage() {
        if (!isLanguageExist()) {
            LightTag.getInstance().saveResource("lang.yml", false);
        }

        File langFile = new File(pluginFolder, "lang.yml");
        language = YamlConfiguration.loadConfiguration(langFile);
    }

    public static YamlConfiguration getLanguages() {
        return language;
    }
}
