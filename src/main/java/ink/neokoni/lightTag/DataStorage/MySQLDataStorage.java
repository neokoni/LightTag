package ink.neokoni.lightTag.DataStorage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ink.neokoni.lightTag.LightTag;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * MySQL-based implementation of IDataStorage
 * Stores data in a key-value table with type information
 */
public class MySQLDataStorage implements IDataStorage {
    private final String tableName;
    private HikariDataSource dataSource;
    private final ConcurrentHashMap<String, Object> cache;

    /**
     * Constructor for MySQL storage
     * @param tableName The name of the table to use for storage
     * @param host Database host
     * @param port Database port
     * @param database Database name
     * @param username Database username
     * @param password Database password
     */
    public MySQLDataStorage(String tableName, String host, int port, String database, String username, String password) {
        this.tableName = tableName;
        this.cache = new ConcurrentHashMap<>();
        initializeDataSource(host, port, database, username, password);
    }

    private void initializeDataSource(String host, int port, String database, String username, String password) {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(String.format("jdbc:mariadb://%s:%d/%s", host, port, database));
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            
            dataSource = new HikariDataSource(config);
            createTableIfNotExists();
        } catch (Exception e) {
            LightTag.getInstance().getLogger().severe("Failed to initialize MySQL connection: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void createTableIfNotExists() {
        String createTableSQL = String.format(
            "CREATE TABLE IF NOT EXISTS %s (" +
            "path VARCHAR(255) PRIMARY KEY, " +
            "value TEXT, " +
            "type VARCHAR(50)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4", tableName
        );

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            LightTag.getInstance().getLogger().severe("Failed to create table: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load() {
        // Load all data from database into cache
        cache.clear();
        String sql = String.format("SELECT path, value, type FROM %s", tableName);
        
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String path = rs.getString("path");
                String value = rs.getString("value");
                String type = rs.getString("type");
                
                Object parsedValue = parseValue(value, type);
                if (parsedValue != null) {
                    cache.put(path, parsedValue);
                }
            }
        } catch (SQLException e) {
            LightTag.getInstance().getLogger().severe("Failed to load data from MySQL: " + e.getMessage());
        }
    }

    private Object parseValue(String value, String type) {
        if (value == null) return null;
        
        try {
            switch (type) {
                case "STRING":
                    return value;
                case "INT":
                    return Integer.parseInt(value);
                case "DOUBLE":
                    return Double.parseDouble(value);
                case "BOOLEAN":
                    return Boolean.parseBoolean(value);
                case "INT_LIST":
                    if (value.isEmpty()) return new ArrayList<Integer>();
                    return Arrays.stream(value.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                case "STRING_LIST":
                    if (value.isEmpty()) return new ArrayList<String>();
                    return new ArrayList<>(Arrays.asList(value.split(",")));
                default:
                    return value;
            }
        } catch (Exception e) {
            LightTag.getInstance().getLogger().warning("Failed to parse value: " + value + " as type: " + type);
            return null;
        }
    }

    @Override
    public String getString(String path) {
        Object value = cache.get(path);
        return value != null ? value.toString() : null;
    }

    @Override
    public int getInt(String path) {
        Object value = cache.get(path);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public double getDouble(String path) {
        Object value = cache.get(path);
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    @Override
    public boolean getBoolean(String path) {
        Object value = cache.get(path);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Integer> getIntegerList(String path) {
        Object value = cache.get(path);
        if (value instanceof List) {
            try {
                return (List<Integer>) value;
            } catch (ClassCastException e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getStringList(String path) {
        Object value = cache.get(path);
        if (value instanceof List) {
            try {
                return (List<String>) value;
            } catch (ClassCastException e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public boolean isSet(String path) {
        return cache.containsKey(path);
    }

    @Override
    public void set(String path, Object value) {
        cache.put(path, value);
    }

    @Override
    public void save() {
        // Save all cached data to database
        String insertOrUpdateSQL = String.format(
            "INSERT INTO %s (path, value, type) VALUES (?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE value = VALUES(value), type = VALUES(type)",
            tableName
        );

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertOrUpdateSQL)) {
            
            conn.setAutoCommit(false);
            
            for (ConcurrentHashMap.Entry<String, Object> entry : cache.entrySet()) {
                String path = entry.getKey();
                Object value = entry.getValue();
                
                pstmt.setString(1, path);
                pstmt.setString(2, serializeValue(value));
                pstmt.setString(3, getType(value));
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
            conn.commit();
            
        } catch (SQLException e) {
            LightTag.getInstance().getLogger().severe("Failed to save data to MySQL: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String serializeValue(Object value) {
        if (value == null) return "";
        
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            return list.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        }
        
        return value.toString();
    }

    private String getType(Object value) {
        if (value == null) return "STRING";
        
        if (value instanceof String) return "STRING";
        if (value instanceof Integer) return "INT";
        if (value instanceof Double) return "DOUBLE";
        if (value instanceof Boolean) return "BOOLEAN";
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            if (!list.isEmpty() && list.get(0) instanceof Integer) {
                return "INT_LIST";
            }
            return "STRING_LIST";
        }
        
        return "STRING";
    }

    @Override
    public java.util.Set<String> getKeys(boolean deep) {
        if (!deep) {
            // Return only root-level keys (keys without dots)
            return cache.keySet().stream()
                .filter(key -> !key.contains("."))
                .collect(java.util.stream.Collectors.toSet());
        } else {
            // Return all keys
            return new java.util.HashSet<>(cache.keySet());
        }
    }

    /**
     * Close the data source connection pool
     */
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
