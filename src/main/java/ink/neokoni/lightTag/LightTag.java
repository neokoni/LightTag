package ink.neokoni.lightTag;

import ink.neokoni.lightTag.Commands.Commands;
import ink.neokoni.lightTag.DataStorage.*;
import ink.neokoni.lightTag.Handler.PlayerJoinHandler;
import ink.neokoni.lightTag.Handler.PlayerQuitHandler;
import ink.neokoni.lightTag.Handler.TagsInventoryHandlers;
import ink.neokoni.lightTag.PAPIs.PAPIsCore;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightTag extends JavaPlugin {
    private static LightTag instance;
    private static Economy econ = null;
    public static String version;

    public static boolean hasEco = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this; // init instance
        version = this.getPluginMeta().getVersion();

        Configs.loadConfig(); // init configs
        Languages.loadLanguage();
        Tags.loadTags();
        Templates.loadTemplates();
        PlayerDatas.loadPlayerData();

        SetupVault(); // register vault for economy

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

    private void SetupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();
        if (econ!=null)hasEco=true;
    }

    public static Economy getEcon() {
        return econ;
    }
}
