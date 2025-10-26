package ink.neokoni.lightTag;

import ink.neokoni.lightTag.Commands.Commands;
import ink.neokoni.lightTag.DataStorage.Configs;
import ink.neokoni.lightTag.DataStorage.Languages;
import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.DataStorage.Tags;
import ink.neokoni.lightTag.Handler.PlayerJoinHandler;
import ink.neokoni.lightTag.Handler.PlayerQuitHandler;
import ink.neokoni.lightTag.Handler.TagsInventoryHandlers;
import ink.neokoni.lightTag.PAPIs.PAPIsCore;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightTag extends JavaPlugin {
    private static LightTag instance;
    public static String version;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this; // init instance
        version = this.getPluginMeta().getVersion();

        Configs.loadConfig(); // init configs
        Languages.loadLanguage();
        Tags.loadTags();
        PlayerDatas.loadPlayerData();

        new Commands(); // register commands

        new PAPIsCore().register(); // register PlaceholderAPI

        new PlayerJoinHandler(this); // register Event listener
        new PlayerQuitHandler(this);
        new TagsInventoryHandlers(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PlayerDatas.writeToFile();
        Tags.writeToFile();
    }

    public static LightTag getInstance() {
        return instance;
    }
}
