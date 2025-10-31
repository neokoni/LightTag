package ink.neokoni.lightTag.DataStorage;

import ink.neokoni.lightTag.LightTag;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Templates {
    private static File pluginFolder = LightTag.getInstance().getDataFolder();
    private static YamlConfiguration templates = new YamlConfiguration();
    public static boolean isTemplatesExist() {
        return new File(pluginFolder, "templates.yml").exists();
    }

    public static void loadTemplates() {
        if (!isTemplatesExist()) {
            LightTag.getInstance().saveResource("templates.yml", false);
        }

        File templatesFile = new File(pluginFolder, "templates.yml");
        templates = YamlConfiguration.loadConfiguration(templatesFile);
    }

    public static YamlConfiguration getTemplates() {
        return templates;
    }
}
