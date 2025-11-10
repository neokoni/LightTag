package ink.neokoni.lightTag.DataStorage;

import ink.neokoni.lightTag.LightTag;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * YAML-based implementation of IDataStorage
 * Provides backward compatibility with existing YAML files
 */
public class YamlDataStorage implements IDataStorage {
    private final File file;
    private YamlConfiguration data;
    private final boolean isResource;

    /**
     * Constructor for YAML storage
     * @param fileName The name of the YAML file
     * @param isResource Whether the file is a resource that should be extracted from the jar
     */
    public YamlDataStorage(String fileName, boolean isResource) {
        File pluginFolder = LightTag.getInstance().getDataFolder();
        this.file = new File(pluginFolder, fileName);
        this.isResource = isResource;
        this.data = new YamlConfiguration();
    }

    @Override
    public void load() {
        // Create file if it doesn't exist
        if (!file.exists()) {
            if (isResource) {
                // Extract from resources
                LightTag.getInstance().saveResource(file.getName(), false);
            } else {
                // Create empty file
                try {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                } catch (IOException e) {
                    LightTag.getInstance().getLogger().severe("Failed to create " + file.getName() + ": " + e.getMessage());
                }
            }
        }

        // Load the YAML file
        data = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public String getString(String path) {
        return data.getString(path);
    }

    @Override
    public int getInt(String path) {
        return data.getInt(path);
    }

    @Override
    public double getDouble(String path) {
        return data.getDouble(path);
    }

    @Override
    public boolean getBoolean(String path) {
        return data.getBoolean(path);
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        return data.getIntegerList(path);
    }

    @Override
    public List<String> getStringList(String path) {
        return data.getStringList(path);
    }

    @Override
    public boolean isSet(String path) {
        return data.isSet(path);
    }

    @Override
    public void set(String path, Object value) {
        data.set(path, value);
    }

    @Override
    public void save() {
        try {
            data.save(file);
        } catch (IOException e) {
            LightTag.getInstance().getLogger().severe("Failed to save " + file.getName() + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public java.util.Set<String> getKeys(boolean deep) {
        return data.getKeys(deep);
    }

    /**
     * Get the underlying YamlConfiguration for compatibility
     * @return The YamlConfiguration object
     * @deprecated Use IDataStorage methods instead
     */
    @Deprecated
    public YamlConfiguration getYamlConfiguration() {
        return data;
    }
}
