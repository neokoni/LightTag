package ink.neokoni.lightTag.DataStorage;

import ink.neokoni.lightTag.LightTag;

import java.util.Set;

/**
 * Utility class for migrating data between different storage types
 */
public class DataMigrationUtil {
    
    /**
     * Migrate data from one storage to another
     * @param source Source storage
     * @param target Target storage
     * @param storageType "player" or "tag"
     */
    public static void migrateData(IDataStorage source, IDataStorage target, String storageType) {
        LightTag.getInstance().getLogger().info("Starting data migration for " + storageType + " data...");
        
        // Load source data
        source.load();
        
        // Get all keys from source
        Set<String> keys = source.getKeys(true);
        
        int migratedCount = 0;
        for (String key : keys) {
            // Get value from source
            Object value = getValueFromSource(source, key);
            
            // Set value in target
            if (value != null) {
                target.set(key, value);
                migratedCount++;
            }
        }
        
        // Save target
        target.save();
        
        LightTag.getInstance().getLogger().info("Migration completed: " + migratedCount + " entries migrated.");
    }
    
    /**
     * Get value from source storage by trying different types
     * @param source Source storage
     * @param key Key to get
     * @return Value object
     */
    private static Object getValueFromSource(IDataStorage source, String key) {
        // Try to determine the type and get the value
        // First check if it's set
        if (!source.isSet(key)) {
            return null;
        }
        
        // Try to get as different types and return the first non-null/non-default value
        String strValue = source.getString(key);
        if (strValue != null && !strValue.isEmpty()) {
            // Check if it might be a number
            try {
                // Try integer
                if (strValue.matches("-?\\d+")) {
                    return source.getInt(key);
                }
                // Try double
                if (strValue.matches("-?\\d+\\.\\d+")) {
                    return source.getDouble(key);
                }
                // Try boolean
                if (strValue.equalsIgnoreCase("true") || strValue.equalsIgnoreCase("false")) {
                    return source.getBoolean(key);
                }
            } catch (Exception e) {
                // If parsing fails, just return the string
            }
            return strValue;
        }
        
        // Try list types
        java.util.List<?> listValue = source.getIntegerList(key);
        if (listValue != null && !listValue.isEmpty()) {
            return listValue;
        }
        
        listValue = source.getStringList(key);
        if (listValue != null && !listValue.isEmpty()) {
            return listValue;
        }
        
        // Default to string
        return strValue;
    }
    
    /**
     * Migrate player data from YAML to MySQL
     */
    public static void migratePlayerDataYamlToMysql() {
        IDataStorage yamlStorage = new YamlDataStorage("PlayerData.yml", false);
        IDataStorage mysqlStorage = DataStorageFactory.createPlayerDataStorage();
        
        migrateData(yamlStorage, mysqlStorage, "player");
    }
    
    /**
     * Migrate tag data from YAML to MySQL
     */
    public static void migrateTagDataYamlToMysql() {
        IDataStorage yamlStorage = new YamlDataStorage("tags.yml", true);
        IDataStorage mysqlStorage = DataStorageFactory.createTagStorage();
        
        migrateData(yamlStorage, mysqlStorage, "tag");
    }
    
    /**
     * Migrate player data from MySQL to YAML
     */
    public static void migratePlayerDataMysqlToYaml() {
        IDataStorage mysqlStorage = DataStorageFactory.createPlayerDataStorage();
        IDataStorage yamlStorage = new YamlDataStorage("PlayerData.yml", false);
        
        migrateData(mysqlStorage, yamlStorage, "player");
    }
    
    /**
     * Migrate tag data from MySQL to YAML
     */
    public static void migrateTagDataMysqlToYaml() {
        IDataStorage mysqlStorage = DataStorageFactory.createTagStorage();
        IDataStorage yamlStorage = new YamlDataStorage("tags.yml", true);
        
        migrateData(mysqlStorage, yamlStorage, "tag");
    }
}
