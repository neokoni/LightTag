package ink.neokoni.lightTag.DataStorage;

import ink.neokoni.lightTag.LightTag;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Tags {
    private static IDataStorage storage;

    /**
     * Load tag data storage
     */
    public static void loadTags() {
        storage = DataStorageFactory.createTagStorage();
        storage.load();
    }

    /**
     * Save tag data to storage
     */
    public static void writeToFile() {
        if (storage != null) {
            storage.save();
        }
    }

    /**
     * Get a string value from tag data
     * @param path The path to the value, e.g., "{tagId}.type"
     * @return The string value or null if not found
     */
    public static String getString(String path) {
        return storage.getString(path);
    }

    /**
     * Get an integer value from tag data
     * @param path The path to the value
     * @return The integer value or 0 if not found
     */
    public static int getInt(String path) {
        return storage.getInt(path);
    }

    /**
     * Get a double value from tag data
     * @param path The path to the value
     * @return The double value or 0.0 if not found
     */
    public static double getDouble(String path) {
        return storage.getDouble(path);
    }

    /**
     * Get a list of strings from tag data
     * @param path The path to the value
     * @return The list of strings or empty list if not found
     */
    public static List<String> getStringList(String path) {
        return storage.getStringList(path);
    }

    /**
     * Check if a path exists in tag data
     * @param path The path to check
     * @return true if the path exists, false otherwise
     */
    public static boolean isSet(String path) {
        return storage.isSet(path);
    }

    /**
     * Set a value in tag data
     * @param path The path to set
     * @param value The value to set
     */
    public static void set(String path, Object value) {
        storage.set(path, value);
    }

    /**
     * Get the underlying YamlConfiguration for backward compatibility
     * @return YamlConfiguration object
     * @deprecated Use direct methods like getString(), getInt(), etc.
     */
    @Deprecated
    public static YamlConfiguration getTags() {
        if (storage instanceof YamlDataStorage) {
            return ((YamlDataStorage) storage).getYamlConfiguration();
        }
        // Return empty configuration if not YAML storage
        LightTag.getInstance().getLogger().warning("getTags() called on non-YAML storage, returning empty configuration");
        return new YamlConfiguration();
    }

    /**
     * Check if tags file exists (for backward compatibility)
     * @return true if exists
     * @deprecated No longer needed with new storage system
     */
    @Deprecated
    public static boolean isTagsExist() {
        File pluginFolder = LightTag.getInstance().getDataFolder();
        return new File(pluginFolder, "tags.yml").exists();
    }

    /**
     * Get all keys at the root level or under a specific path
     * @param deep Whether to get keys recursively
     * @return Set of keys
     */
    public static java.util.Set<String> getKeys(boolean deep) {
        return storage.getKeys(deep);
    }

    /**
     * Create tags file (for backward compatibility)
     * @deprecated No longer needed with new storage system
     */
    @Deprecated
    public static void createTagsFile() {
        LightTag plugin = LightTag.getInstance();
        plugin.saveResource("tags.yml", false);
    }
}
