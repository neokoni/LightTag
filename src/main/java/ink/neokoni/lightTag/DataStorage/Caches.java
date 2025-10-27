package ink.neokoni.lightTag.DataStorage;

import ink.neokoni.lightTag.GUIs.AlmanacGUI;
import ink.neokoni.lightTag.GUIs.MainGUI;
import ink.neokoni.lightTag.GUIs.SetTagGUI;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Caches {
    public static final Map<Inventory, SetTagGUI> setTagGUI = new ConcurrentHashMap<>();
    public static final Map<Inventory, MainGUI> mainGUI = new ConcurrentHashMap<>();
    public static final Map<Inventory, AlmanacGUI> almanacGUI = new ConcurrentHashMap<>();
}
