package ink.neokoni.lightTag.Commands.Functions;

import ink.neokoni.lightTag.DataStorage.DataMigrationUtil;
import ink.neokoni.lightTag.Utils.TextUtils;
import org.bukkit.command.CommandSender;

/**
 * Command function to migrate data between storage types
 */
public class MigrateStorage {
    
    /**
     * Execute storage migration
     * @param sender Command sender
     * @param direction Migration direction: "yaml-to-mysql" or "mysql-to-yaml"
     */
    public MigrateStorage(CommandSender sender, String direction) {
        if (!sender.hasPermission("lighttag.migrate")) {
            sender.sendMessage(TextUtils.getFormatedLang("system.no-perms"));
            return;
        }
        
        sender.sendMessage(TextUtils.getFormatedPrefix() + "§eStarting data migration...");
        
        try {
            switch (direction.toLowerCase()) {
                case "yaml-to-mysql":
                case "yaml2mysql":
                    migrateYamlToMysql(sender);
                    break;
                case "mysql-to-yaml":
                case "mysql2yaml":
                    migrateMysqlToYaml(sender);
                    break;
                default:
                    sender.sendMessage(TextUtils.getFormatedPrefix() + "§cInvalid migration direction!");
                    sender.sendMessage(TextUtils.getFormatedPrefix() + "§cUsage: /ltag migrate <yaml-to-mysql|mysql-to-yaml>");
                    return;
            }
            
            sender.sendMessage(TextUtils.getFormatedPrefix() + "§aData migration completed successfully!");
            sender.sendMessage(TextUtils.getFormatedPrefix() + "§eNote: Remember to update storage.type in config.yml and restart the server.");
        } catch (Exception e) {
            sender.sendMessage(TextUtils.getFormatedPrefix() + "§cMigration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void migrateYamlToMysql(CommandSender sender) {
        sender.sendMessage(TextUtils.getFormatedPrefix() + "§eMigrating player data from YAML to MySQL...");
        DataMigrationUtil.migratePlayerDataYamlToMysql();
        
        sender.sendMessage(TextUtils.getFormatedPrefix() + "§eMigrating tag data from YAML to MySQL...");
        DataMigrationUtil.migrateTagDataYamlToMysql();
    }
    
    private void migrateMysqlToYaml(CommandSender sender) {
        sender.sendMessage(TextUtils.getFormatedPrefix() + "§eMigrating player data from MySQL to YAML...");
        DataMigrationUtil.migratePlayerDataMysqlToYaml();
        
        sender.sendMessage(TextUtils.getFormatedPrefix() + "§eMigrating tag data from MySQL to YAML...");
        DataMigrationUtil.migrateTagDataMysqlToYaml();
    }
}
