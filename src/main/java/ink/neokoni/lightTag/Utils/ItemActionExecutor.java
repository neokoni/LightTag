package ink.neokoni.lightTag.Utils;

import ink.neokoni.lightTag.Commands.Functions.BuyTag;
import ink.neokoni.lightTag.Commands.Functions.ClearTag;
import ink.neokoni.lightTag.Commands.Functions.SetTag;
import ink.neokoni.lightTag.DataStorage.Caches;
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
        *  ClearTag:<anyThing> Clear player's using tag, like /ltag clear
        *  BuyTag:<id> Buy a tag as player
        * */

        if (actions==null) {
            return;
        }

        for (String action : actions) {
            String[] res = action.split(":");
            String type,value;
            type=res[0];
            if (res.length<2) {
                value="";
            } else {
                value=res[1];
            }
            switch (type) {
                case "SetTag": {
                    new SetTag(player, Integer.valueOf(value));
                    continue;
                }
                case "OpenPage": {
                    switch (value) {
                        case "AlmanacGUI": {
                            new AlmanacGUI(player, 1).open();
                            continue;
                        }
                        case "BuyTagGUI": {
                            new BuyTagGUI(player, 1).open();
                            continue;
                        }
                        case "MainGUI": {
                            new MainGUI(player).open();
                            continue;
                        }
                        case "SetTagGUI": {
                            new SetTagGUI(player, 1).open();
                            continue;
                        }
                    }
                    continue;
                }
                case "Pages": {
                    AlmanacGUI almanacGUI = Caches.almanacGUI.get(menu.getInv());
                    BuyTagGUI buyTagGUI = Caches.buyTagGUI.get(menu.getInv());
                    SetTagGUI setTagGUI = Caches.setTagGUI.get(menu.getInv());

                    if (almanacGUI!=null) {
                        if (value.equals("Next")) {
                            almanacGUI.next();
                            continue;
                        } else if (value.equals("Previous")) {
                            almanacGUI.previous();
                            continue;
                        }
                    } else if(buyTagGUI!=null) {
                        if (value.equals("Next")) {
                            buyTagGUI.next();
                            continue;
                        } else if (value.equals("Previous")) {
                            buyTagGUI.previous();
                            continue;
                        }
                    } else if(setTagGUI!=null) {
                        if (value.equals("Next")) {
                            setTagGUI.next();
                            continue;
                        } else if (value.equals("Previous")) {
                            setTagGUI.previous();
                            continue;
                        }
                    }
                    continue;
                }
                case "Close": {
                    player.closeInventory();
                    continue;
                }
                case "Command": {
                    player.performCommand(value);
                    continue;
                }
                case "ClearTag": {
                    new ClearTag(player);
                    continue;
                }
                case "BuyTag": {
                    int i = Integer.valueOf(value);
                    new BuyTag(player, i);
                    continue;
                }
            }
        }
    }
}
