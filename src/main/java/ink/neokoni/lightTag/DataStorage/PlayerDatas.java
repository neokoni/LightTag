package ink.neokoni.lightTag.DataStorage;

import ink.neokoni.lightTag.LightTag;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlayerDatas {
    private static IDataStorage storage;

    /**
     * Load player data storage
     */
    public static void loadPlayerData() {
        storage = DataStorageFactory.createPlayerDataStorage();
        storage.load();
    }

    /**
     * Save player data to storage
     */
    public static void writeToFile() {
        if (storage != null) {
            storage.save();
        }
    }

    /**
     * Get a string value from player data
     * @param path The path to the value, e.g., "{playerUUID}.using"
     * @return The string value or null if not found
     */
    public static String getString(String path) {
        return storage.getString(path);
    }

    /**
     * Get an integer value from player data
     * @param path The path to the value
     * @return The integer value or 0 if not found
     */
    public static int getInt(String path) {
        return storage.getInt(path);
    }

    /**
     * Get a list of integers from player data
     * @param path The path to the value
     * @return The list of integers or empty list if not found
     */
    public static List<Integer> getIntegerList(String path) {
        return storage.getIntegerList(path);
    }

    /**
     * Check if a path exists in player data
     * @param path The path to check
     * @return true if the path exists, false otherwise
     */
    public static boolean isSet(String path) {
        return storage.isSet(path);
    }

    /**
     * Set a value in player data
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
    public static YamlConfiguration getPlayerData() {
        if (storage instanceof YamlDataStorage) {
            return ((YamlDataStorage) storage).getYamlConfiguration();
        }
        // Return empty configuration if not YAML storage
        LightTag.getInstance().getLogger().warning("getPlayerData() called on non-YAML storage, returning empty configuration");
        return new YamlConfiguration();
    }

    /**
     * Save player data (deprecated, use writeToFile instead)
     * @param data YamlConfiguration to save
     * @deprecated Use set() methods and writeToFile() instead
     */
    @Deprecated
    public static void savePlayerData(YamlConfiguration data) {
        // This method is kept for backward compatibility but does nothing
        // The new storage system handles data internally
        LightTag.getInstance().getLogger().warning("savePlayerData(YamlConfiguration) is deprecated and does nothing in new storage system");
    }

    /**
     * Check if player data file exists (for backward compatibility)
     * @return true if exists
     * @deprecated No longer needed with new storage system
     */
    @Deprecated
    public static boolean isPlayerDataExist() {
        File pluginFolder = LightTag.getInstance().getDataFolder();
        return new File(pluginFolder, "PlayerData.yml").exists();
    }

    /**
     * Create data file (for backward compatibility)
     * @deprecated No longer needed with new storage system
     */
    @Deprecated
    public static void createDataFile() {
        try {
            File pluginFolder = LightTag.getInstance().getDataFolder();
            File file = new File(pluginFolder, "PlayerData.yml");
            YamlConfiguration playerData = new YamlConfiguration();
            playerData.save(file);
        } catch (IOException e) {
            LightTag.getInstance().getLogger().severe("FAILED TO CREATE PlayerData FILE!");
        }
    }
}
