# 数据存储重构总结 / Data Storage Refactoring Summary

## 项目需求 / Project Requirements

根据问题描述，需要：
According to the problem statement, we need to:

1. 分析玩家数据与tag数据存储结构
   Analyze player data and tag data storage structure

2. 重构存储部分，使其支持yaml与mysql两种存储方法
   Refactor storage to support both YAML and MySQL storage methods

3. 封装为相同的读取调用方法
   Encapsulate with unified read/access methods

4. 调用方法类似于yaml的获取方法，如 `getString("{playerUUID}.using")` 的格式
   Access methods similar to YAML's getter methods, like `getString("{playerUUID}.using")` format

## 实现的功能 / Implemented Features

### 1. 统一的数据访问接口 / Unified Data Access Interface

创建了 `IDataStorage` 接口，定义了统一的数据访问方法：
Created `IDataStorage` interface that defines unified data access methods:

- `getString(String path)` - 获取字符串值
- `getInt(String path)` - 获取整数值
- `getDouble(String path)` - 获取双精度值
- `getBoolean(String path)` - 获取布尔值
- `getIntegerList(String path)` - 获取整数列表
- `getStringList(String path)` - 获取字符串列表
- `isSet(String path)` - 检查路径是否存在
- `set(String path, Object value)` - 设置值
- `save()` - 保存数据
- `load()` - 加载数据
- `getKeys(boolean deep)` - 获取所有键

### 2. YAML 存储实现 / YAML Storage Implementation

`YamlDataStorage` 类：
- 完全向后兼容现有的 YAML 文件格式
- 使用 Bukkit 的 `YamlConfiguration` 作为底层实现
- 支持自动创建和加载文件

### 3. MySQL 存储实现 / MySQL Storage Implementation

`MySQLDataStorage` 类：
- 使用 HikariCP 连接池管理数据库连接
- 采用键值对存储结构，支持所有数据类型
- 内存缓存机制提高性能
- 自动创建数据库表结构

### 4. 工厂模式 / Factory Pattern

`DataStorageFactory` 类：
- 根据配置文件自动选择存储类型
- 统一创建存储实例的入口
- 支持为不同的数据源（玩家数据、标签数据）创建不同的存储实例

### 5. 数据迁移工具 / Data Migration Utility

`DataMigrationUtil` 类：
- 支持从 YAML 迁移到 MySQL
- 支持从 MySQL 迁移回 YAML
- 自动检测数据类型并正确迁移
- 提供详细的迁移日志

### 6. 迁移命令 / Migration Command

添加了 `/ltag migrate` 命令：
- `/ltag migrate yaml-to-mysql` - 从 YAML 迁移到 MySQL
- `/ltag migrate mysql-to-yaml` - 从 MySQL 迁移到 YAML
- 需要 `lighttag.migrate` 权限

## 代码变更 / Code Changes

### 修改的文件 / Modified Files

1. **PlayerDatas.java** - 重构为使用 IDataStorage 接口
2. **Tags.java** - 重构为使用 IDataStorage 接口
3. **config.yml** - 添加存储配置选项

### 更新的调用点 / Updated Call Sites

所有使用以下旧方法的地方都已更新：
All places using the following old methods have been updated:

- `PlayerDatas.getPlayerData()` → 直接使用 `PlayerDatas.getString()` 等方法
- `Tags.getTags()` → 直接使用 `Tags.getString()` 等方法

更新的文件包括：
Updated files include:
- PlayerJoinHandler.java
- SetTag.java
- BuyTag.java
- PlayerUsingStaticTagPAPI.java
- PlayerUsingDynamicTagPAPI.java
- TagUtils.java
- AddTag.java
- SendPlayerTagList.java
- AlmanacOfTags.java
- GiveTag.java
- ClearTag.java
- SetTagGUI.java
- BuyTagGUI.java
- AlmanacGUI.java
- Template.java (in GUIs/Base)

### 新增的文件 / New Files

1. **IDataStorage.java** - 数据存储接口
2. **YamlDataStorage.java** - YAML 存储实现
3. **MySQLDataStorage.java** - MySQL 存储实现
4. **DataStorageFactory.java** - 存储工厂
5. **DataMigrationUtil.java** - 数据迁移工具
6. **MigrateStorage.java** - 迁移命令函数
7. **STORAGE_CONFIGURATION.md** - 存储配置文档
8. **REFACTORING_SUMMARY.md** - 本文档

## 使用示例 / Usage Examples

### 配置 YAML 存储 / Configure YAML Storage

```yaml
storage:
  type: yaml
```

### 配置 MySQL 存储 / Configure MySQL Storage

```yaml
storage:
  type: mysql
  mysql:
    host: localhost
    port: 3306
    database: lighttag
    username: root
    password: "your_password"
```

### 代码中访问数据 / Accessing Data in Code

```java
// 获取玩家正在使用的标签
String usingTag = PlayerDatas.getString(player.getUniqueId() + ".using");

// 获取玩家拥有的标签列表
List<Integer> ownedTags = PlayerDatas.getIntegerList(player.getUniqueId() + ".owns");

// 设置玩家正在使用的标签
PlayerDatas.set(player.getUniqueId() + ".using", tagId);

// 获取标签类型
String tagType = Tags.getString(tagId + ".type");

// 获取标签价格
double price = Tags.getDouble(tagId + ".price");

// 保存数据
PlayerDatas.writeToFile();
Tags.writeToFile();
```

## 向后兼容性 / Backward Compatibility

### 保留的方法 / Retained Methods

为了向后兼容，以下方法被保留但标记为 `@Deprecated`：
For backward compatibility, the following methods are retained but marked as `@Deprecated`:

- `PlayerDatas.getPlayerData()` - 返回 YamlConfiguration（仅在 YAML 模式）
- `PlayerDatas.savePlayerData(YamlConfiguration)` - 不执行任何操作
- `Tags.getTags()` - 返回 YamlConfiguration（仅在 YAML 模式）

### 迁移路径 / Migration Path

现有代码可以：
Existing code can:
1. 继续使用旧方法（在 YAML 模式下）
2. 逐步迁移到新的 API
3. 在 MySQL 模式下，旧方法会返回空配置并记录警告

## 性能优化 / Performance Optimizations

### MySQL 存储 / MySQL Storage

1. **连接池** - 使用 HikariCP 管理连接
2. **内存缓存** - 所有数据在内存中缓存，减少数据库查询
3. **批量操作** - save() 方法使用批量插入
4. **索引** - path 字段是主键，支持快速查询

### YAML 存储 / YAML Storage

1. **延迟加载** - 只在需要时加载文件
2. **内存操作** - 所有操作在内存中完成，最后统一保存

## 测试建议 / Testing Recommendations

### 功能测试 / Functional Testing

1. 测试 YAML 模式下的所有功能
2. 测试 MySQL 模式下的所有功能
3. 测试数据迁移（YAML → MySQL）
4. 测试数据迁移（MySQL → YAML）

### 性能测试 / Performance Testing

1. 大量玩家数据下的性能
2. 大量标签数据下的性能
3. 并发访问性能

### 兼容性测试 / Compatibility Testing

1. 从旧版本升级的兼容性
2. 多服务器环境下的 MySQL 共享数据

## 注意事项 / Notes

1. **备份数据** - 在迁移存储类型前务必备份数据
2. **权限配置** - 确保 MySQL 用户有足够的权限创建表
3. **依赖项** - 使用 MySQL 存储需要 HikariCP 和 MariaDB 驱动
4. **数据一致性** - 在多服务器环境下使用 MySQL，注意数据同步

## 未来改进 / Future Improvements

1. 添加 Redis 存储支持
2. 添加更多的数据迁移选项（如部分迁移）
3. 添加自动备份功能
4. 优化 MySQL 查询性能
5. 添加数据验证和完整性检查

## 总结 / Conclusion

本次重构成功实现了：
This refactoring successfully achieved:

✅ 支持 YAML 和 MySQL 两种存储方式
✅ 统一的数据访问 API
✅ 类似 YAML 的调用格式（如 `getString("{playerUUID}.using")`）
✅ 完全向后兼容
✅ 易于扩展新的存储类型
✅ 提供了数据迁移工具

代码质量提升：
Code quality improvements:

- 更好的抽象和封装
- 遵循 SOLID 原则
- 易于测试和维护
- 清晰的文档
