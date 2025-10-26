package ink.neokoni.lightTag.Commands.Functions;

import ink.neokoni.lightTag.DataStorage.Languages;
import ink.neokoni.lightTag.LightTag;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class PrintHelp {
    public PrintHelp(CommandSender sender) {
        sender.sendMessage(MiniMessage.miniMessage()
                .deserialize("<color:#ffdbab>\uD83C\uDFF7LightTag <white>| <color:#ffdbab>\uD83C\uDF1Fv"+ LightTag.version));
        sender.sendMessage("");
        for (String s : Languages.getLanguages().getStringList("help")) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(s));
        }
    }
}
