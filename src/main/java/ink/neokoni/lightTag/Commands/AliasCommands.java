package ink.neokoni.lightTag.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import ink.neokoni.lightTag.Commands.Functions.*;
import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.DataStorage.Tags;
import ink.neokoni.lightTag.GUIs.MainGUI;
import ink.neokoni.lightTag.GUIs.SetTagGUI;
import ink.neokoni.lightTag.Utils.TextUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;

import java.util.List;

public class AliasCommands {
    public LiteralArgumentBuilder<CommandSourceStack> getBuilt(String root) {
        return Commands.literal(root)
                .executes(ctx -> {
                    if (!(ctx.getSource().getSender() instanceof Player player)) {
                        return Command.SINGLE_SUCCESS;
                    }
                    new MainGUI(player).open();
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("reload")
                        .requires(ctx -> ctx.getSender().hasPermission("lighttag.reload"))
                        .executes(ctx->{
                            new Reload(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("set")
                        .executes(ctx -> {
                            if (!(ctx.getSource().getSender() instanceof Player)) {
                                return Command.SINGLE_SUCCESS;
                            }

                            new SetTagGUI((Player) ctx.getSource().getSender()).open();

                            return Command.SINGLE_SUCCESS;
                        })

                        .then(Commands.argument("id", IntegerArgumentType.integer(1))
                            .executes(ctx -> {
                                new SetTag(ctx.getSource().getSender(), ctx.getArgument("id", Integer.class));
                                return Command.SINGLE_SUCCESS;
                        })))
                .then(Commands.literal("list")
                        .executes(ctx -> {
                            new SendPlayerTagList(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("add")
                        .then(Commands.literal("STATIC")
                                .then(Commands.argument("Content", StringArgumentType.string())
                                        .executes(ctx -> {
                                            new AddTag(ctx.getArgument("Content", String.class),
                                                    ctx.getSource().getSender());
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("ANIMATION")
                                .then(Commands.argument("Content", StringArgumentType.string())
                                        .then(Commands.argument("Banner", StringArgumentType.string())
                                                .then(Commands.argument("Delay", IntegerArgumentType.integer(0))
                                                        .executes(ctx -> {
                                                            new AddTag(ctx.getArgument("Content", String.class),
                                                                    ctx.getArgument("Banner", String.class),
                                                                    ctx.getArgument("Delay", Integer.class),
                                                                    ctx.getSource().getSender());
                                                            return Command.SINGLE_SUCCESS;
                                                        }))))))
                .then(Commands.literal("clear")
                    .executes(ctx -> {
                        new ClearTag(ctx.getSource().getSender());
                        return Command.SINGLE_SUCCESS;
                    }))
                .then(Commands.literal("give")
                        .then(Commands.argument("Player(s)", ArgumentTypes.players())
                                .then(Commands.argument("id", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("Player(s)", PlayerSelectorArgumentResolver.class);
                                            List<Player> players = targetResolver.resolve(ctx.getSource());
                                            int id = ctx.getArgument("id", Integer.class);
                                            new GiveTag(ctx.getSource().getSender(), players, id);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("almanac")
                        .executes(ctx -> {
                            new AlmanacOfTags(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("save")
                        .executes(ctx -> {
                            PlayerDatas.writeToFile();
                            Tags.writeToFile();
                            ctx.getSource().getSender().sendMessage(TextUtils.getFormatedLang("system.saved"));
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("help")
                        .executes(ctx -> {
                            new PrintHelp(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }));
    }
}
