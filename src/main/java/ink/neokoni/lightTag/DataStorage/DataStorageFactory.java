package ink.neokoni.lightTag.DataStorage;

import ink.neokoni.lightTag.LightTag;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Factory class to create appropriate data storage instances based on configuration
 */
public class DataStorageFactory {
    
    /**
     * Storage type enumeration
     */
    public enum StorageType {
        YAML,
        MYSQL
    }

    /**
     * Create a data storage instance for player data
     * @return IDataStorage instance
     */
    public static IDataStorage createPlayerDataStorage() {
        StorageType type = getStorageType();
        
        switch (type) {
            case MYSQL:
                return createMySQLStorage("player_data");
            case YAML:
            default:
                return new YamlDataStorage("PlayerData.yml", false);
        }
    }

    /**
     * Create a data storage instance for tag data
     * @return IDataStorage instance
     */
    public static IDataStorage createTagStorage() {
        StorageType type = getStorageType();
        
        switch (type) {
            case MYSQL:
                return createMySQLStorage("tag_data");
            case YAML:
            default:
                return new YamlDataStorage("tags.yml", true);
        }
    }

    /**
     * Get storage type from configuration
     * @return StorageType enum value
     */
    private static StorageType getStorageType() {
        // Load config to determine storage type
        File configFile = new File(LightTag.getInstance().getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            return StorageType.YAML; // Default to YAML
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        String typeStr = config.getString("storage.type", "yaml").toLowerCase();
        
        try {
            return StorageType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            LightTag.getInstance().getLogger().warning("Invalid storage type: " + typeStr + ", defaulting to YAML");
            return StorageType.YAML;
        }
    }

    /**
     * Create MySQL storage instance using configuration
     * @param tableName The table name to use
     * @return MySQLDataStorage instance
     */
    private static MySQLDataStorage createMySQLStorage(String tableName) {
        File configFile = new File(LightTag.getInstance().getDataFolder(), "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        
        String host = config.getString("storage.mysql.host", "localhost");
        int port = config.getInt("storage.mysql.port", 3306);
        String database = config.getString("storage.mysql.database", "lighttag");
        String username = config.getString("storage.mysql.username", "root");
        String password = config.getString("storage.mysql.password", "");
        
        return new MySQLDataStorage(tableName, host, port, database, username, password);
    }
}
