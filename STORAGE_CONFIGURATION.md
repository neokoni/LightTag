# LightTag 存储配置指南 / Storage Configuration Guide

## 概述 / Overview

LightTag 现在支持两种数据存储方式：
LightTag now supports two data storage methods:

1. **YAML** - 基于文件的存储（默认）/ File-based storage (default)
2. **MySQL** - 基于数据库的存储 / Database-based storage

## 配置 / Configuration

在 `config.yml` 中配置存储方式：
Configure the storage method in `config.yml`:

### YAML 存储 / YAML Storage (默认 / Default)

```yaml
storage:
  type: yaml
```

使用 YAML 存储时，数据将保存在以下文件中：
When using YAML storage, data will be saved in the following files:
- `PlayerData.yml` - 玩家数据 / Player data
- `tags.yml` - 称号数据 / Tag data

### MySQL 存储 / MySQL Storage

```yaml
storage:
  type: mysql
  mysql:
    host: localhost      # 数据库主机 / Database host
    port: 3306          # 数据库端口 / Database port
    database: lighttag  # 数据库名称 / Database name
    username: root      # 数据库用户名 / Database username
    password: ""        # 数据库密码 / Database password
```

使用 MySQL 存储时，数据将保存在以下表中：
When using MySQL storage, data will be saved in the following tables:
- `player_data` - 玩家数据 / Player data
- `tag_data` - 称号数据 / Tag data

## 数据迁移 / Data Migration

### 使用迁移命令 / Using Migration Command

插件提供了内置的迁移命令来帮助在不同存储类型之间迁移数据。
The plugin provides a built-in migration command to help migrate data between different storage types.

#### 从 YAML 迁移到 MySQL / Migrating from YAML to MySQL

1. 确保现有的 `PlayerData.yml` 和 `tags.yml` 文件存在
   Ensure existing `PlayerData.yml` and `tags.yml` files exist

2. 在 `config.yml` 中配置 MySQL 数据库连接信息（保持 `storage.type: yaml`）
   Configure MySQL database connection in `config.yml` (keep `storage.type: yaml`)

3. 启动服务器 / Start the server

4. 执行迁移命令 / Execute migration command:
   ```
   /ltag migrate yaml-to-mysql
   ```
   或 / or
   ```
   /ltag migrate yaml2mysql
   ```

5. 等待迁移完成 / Wait for migration to complete

6. 停止服务器 / Stop the server

7. 在 `config.yml` 中将 `storage.type` 改为 `mysql`
   Change `storage.type` to `mysql` in `config.yml`

8. 重新启动服务器 / Restart the server

#### 从 MySQL 迁移回 YAML / Migrating from MySQL back to YAML

1. 确保 MySQL 连接配置正确
   Ensure MySQL connection is configured correctly

2. 执行迁移命令 / Execute migration command:
   ```
   /ltag migrate mysql-to-yaml
   ```
   或 / or
   ```
   /ltag migrate mysql2yaml
   ```

3. 等待迁移完成 / Wait for migration to complete

4. 停止服务器 / Stop the server

5. 在 `config.yml` 中将 `storage.type` 改为 `yaml`
   Change `storage.type` to `yaml` in `config.yml`

6. 重新启动服务器 / Restart the server

### 权限 / Permissions

迁移命令需要 `lighttag.migrate` 权限。
The migration command requires `lighttag.migrate` permission.

## 数据访问方法 / Data Access Methods

无论使用哪种存储方式，数据访问方法都是统一的：
Regardless of the storage method, the data access methods are unified:

### 玩家数据 / Player Data

```java
// 获取字符串值 / Get string value
String using = PlayerDatas.getString("{playerUUID}.using");

// 获取整数值 / Get integer value
int tagId = PlayerDatas.getInt("{playerUUID}.using");

// 获取整数列表 / Get integer list
List<Integer> ownedTags = PlayerDatas.getIntegerList("{playerUUID}.owns");

// 设置值 / Set value
PlayerDatas.set("{playerUUID}.using", tagId);
PlayerDatas.set("{playerUUID}.owns", ownedTagsList);

// 保存数据 / Save data
PlayerDatas.writeToFile();
```

### 称号数据 / Tag Data

```java
// 获取字符串值 / Get string value
String tagType = Tags.getString("{tagId}.type");

// 获取整数值 / Get integer value
int delay = Tags.getInt("{tagId}.delay");

// 获取双精度值 / Get double value
double price = Tags.getDouble("{tagId}.price");

// 获取字符串列表 / Get string list
List<String> content = Tags.getStringList("{tagId}.content");

// 检查路径是否存在 / Check if path exists
boolean hasPrice = Tags.isSet("{tagId}.price");

// 设置值 / Set value
Tags.set("{tagId}.type", "STATIC");

// 保存数据 / Save data
Tags.writeToFile();
```

## 性能考虑 / Performance Considerations

### YAML 存储 / YAML Storage
- 优点 / Advantages:
  - 易于配置和维护 / Easy to configure and maintain
  - 无需额外的数据库服务器 / No need for additional database server
  - 数据文件易于查看和编辑 / Data files are easy to view and edit

- 缺点 / Disadvantages:
  - 大量数据时性能较差 / Poor performance with large amounts of data
  - 不适合多服务器共享数据 / Not suitable for multi-server data sharing

### MySQL 存储 / MySQL Storage
- 优点 / Advantages:
  - 更好的性能，特别是数据量大时 / Better performance, especially with large data volumes
  - 支持多服务器共享数据 / Supports multi-server data sharing
  - 更好的数据完整性和安全性 / Better data integrity and security

- 缺点 / Disadvantages:
  - 需要额外的 MySQL 数据库服务器 / Requires additional MySQL database server
  - 配置相对复杂 / More complex configuration

## 依赖项 / Dependencies

使用 MySQL 存储需要以下依赖项：
Using MySQL storage requires the following dependencies:

- HikariCP (连接池 / Connection pool)
- MariaDB Java Client (MySQL 驱动 / MySQL driver)

这些依赖项已经在 `build.gradle` 中配置为 `compileOnly`，需要在运行时提供。
These dependencies are configured as `compileOnly` in `build.gradle` and need to be provided at runtime.

## 故障排除 / Troubleshooting

### MySQL 连接失败 / MySQL Connection Failed

如果遇到 MySQL 连接失败，请检查：
If you encounter MySQL connection failures, check:

1. MySQL 服务器是否正在运行 / MySQL server is running
2. 数据库连接信息是否正确 / Database connection information is correct
3. 数据库用户是否有足够的权限 / Database user has sufficient permissions
4. 防火墙是否允许连接 / Firewall allows the connection

### 数据丢失 / Data Loss

为避免数据丢失：
To avoid data loss:

1. 定期备份数据 / Regularly backup data
2. 在更改存储类型前先备份 / Backup before changing storage type
3. 测试配置后再在生产环境使用 / Test configuration before using in production

## 技术细节 / Technical Details

### 数据存储接口 / Data Storage Interface

所有存储实现都遵循 `IDataStorage` 接口：
All storage implementations follow the `IDataStorage` interface:

```java
public interface IDataStorage {
    String getString(String path);
    int getInt(String path);
    double getDouble(String path);
    boolean getBoolean(String path);
    List<Integer> getIntegerList(String path);
    List<String> getStringList(String path);
    boolean isSet(String path);
    void set(String path, Object value);
    void save();
    void load();
    Set<String> getKeys(boolean deep);
}
```

### MySQL 表结构 / MySQL Table Structure

MySQL 存储使用以下表结构：
MySQL storage uses the following table structure:

```sql
CREATE TABLE IF NOT EXISTS {table_name} (
    path VARCHAR(255) PRIMARY KEY,
    value TEXT,
    type VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

数据以键值对的形式存储，`path` 字段是点分隔的路径（如 `{playerUUID}.using`）。
Data is stored as key-value pairs, with the `path` field being a dot-separated path (e.g., `{playerUUID}.using`).
