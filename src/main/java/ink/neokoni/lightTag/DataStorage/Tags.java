package ink.neokoni.lightTag.DataStorage;

import ink.neokoni.lightTag.LightTag;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Tags {
    private static File pluginFolder = LightTag.getInstance().getDataFolder();
    private static LightTag plugin = LightTag.getInstance();
    private static YamlConfiguration tags = new YamlConfiguration();
    public static boolean isTagsExist() {
        return new File(pluginFolder, "tags.yml").exists();
    }

    public static void createTagsFile() {
        plugin.saveResource("tags.yml", false);
    }

    public static void loadTags() {
        if (!isTagsExist()) {
            createTagsFile();
        }

        File tagsFile = new File(pluginFolder, "tags.yml");
        tags = YamlConfiguration.loadConfiguration(tagsFile);
    }


    public static YamlConfiguration getTags() {
        return tags;
    }

    public static void writeToFile() {
        File tagsFile = new File(pluginFolder, "tags.yml");
        try {
            tags.save(tagsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
