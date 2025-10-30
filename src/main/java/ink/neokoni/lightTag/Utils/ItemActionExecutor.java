package ink.neokoni.lightTag.Utils;

import ink.neokoni.lightTag.Commands.Functions.SetTag;
import ink.neokoni.lightTag.GUIs.AlmanacGUI;
import ink.neokoni.lightTag.GUIs.Base.ChestMenu;
import ink.neokoni.lightTag.GUIs.BuyTagGUI;
import ink.neokoni.lightTag.GUIs.MainGUI;
import ink.neokoni.lightTag.GUIs.SetTagGUI;
import org.bukkit.entity.Player;

import java.util.List;

public class ItemActionExecutor {
    public static void run(ChestMenu menu, List<String> actions, Player player) {
        /* Available actions:
        *  SetTag:<id> Set player's tag by tagId
        *  OpenPage:<guiName> Open GUI for player
        *  Pages:<pageNum> Open target page in GUI
        *  Close:<anyThing> Close player's inventory GUI
        *  Command:<command> Run command as player
        * */

        if (actions==null) {
            return;
        }

        for (String action : actions) {
            String type = action.split(":")[0];
            String value = action.split(":")[1];
            switch (type) {
                case "SetTag": {
                    new SetTag(player, Integer.valueOf(value));
                    return;
                }
                case "OpenPage": {
                    switch (value) {
                        case "AlmanacGUI": {
                            new AlmanacGUI(player).open();
                            return;
                        }
                        case "BuyTagGUI": {
                            new BuyTagGUI(player).open();
                            return;
                        }
                        case "MainGUI": {
                            new MainGUI(player).open();
                            return;
                        }
                        case "SetTagGUI": {
                            new SetTagGUI(player).open();
                            return;
                        }
                    }
                    return;
                }
                case "Pages": {

                }
                case "Close": {
                    player.closeInventory();
                    return;
                }
                case "Command": {
                    player.performCommand(value);
                    return;
                }
            }
        }
    }
}
