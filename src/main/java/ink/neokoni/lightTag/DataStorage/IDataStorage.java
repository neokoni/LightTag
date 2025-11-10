package ink.neokoni.lightTag.DataStorage;

import java.util.List;

/**
 * Interface for data storage operations
 * Provides unified access methods similar to YAML configuration
 */
public interface IDataStorage {
    /**
     * Get a string value from the storage
     * @param path The path to the value, e.g., "{playerUUID}.using"
     * @return The string value or null if not found
     */
    String getString(String path);

    /**
     * Get an integer value from the storage
     * @param path The path to the value
     * @return The integer value or 0 if not found
     */
    int getInt(String path);

    /**
     * Get a double value from the storage
     * @param path The path to the value
     * @return The double value or 0.0 if not found
     */
    double getDouble(String path);

    /**
     * Get a boolean value from the storage
     * @param path The path to the value
     * @return The boolean value or false if not found
     */
    boolean getBoolean(String path);

    /**
     * Get a list of integers from the storage
     * @param path The path to the value
     * @return The list of integers or empty list if not found
     */
    List<Integer> getIntegerList(String path);

    /**
     * Get a list of strings from the storage
     * @param path The path to the value
     * @return The list of strings or empty list if not found
     */
    List<String> getStringList(String path);

    /**
     * Check if a path exists in the storage
     * @param path The path to check
     * @return true if the path exists, false otherwise
     */
    boolean isSet(String path);

    /**
     * Set a value in the storage
     * @param path The path to set
     * @param value The value to set
     */
    void set(String path, Object value);

    /**
     * Save the storage to persistent storage (file, database, etc.)
     */
    void save();

    /**
     * Load/reload the storage from persistent storage
     */
    void load();

    /**
     * Get all keys at the root level or under a specific path
     * @param deep Whether to get keys recursively
     * @return Set of keys
     */
    java.util.Set<String> getKeys(boolean deep);
}
