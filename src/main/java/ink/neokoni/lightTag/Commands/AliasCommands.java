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
                        new PrintHelp(ctx.getSource().getSender());
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
                        .requires(ctx->ctx.getSender().hasPermission("lighttag.set"))
                        .executes(ctx -> {
                            if (!(ctx.getSource().getSender() instanceof Player)) {
                                ctx.getSource().getSender().sendMessage(TextUtils.getFormatedLang("system.player-only"));
                                return Command.SINGLE_SUCCESS;
                            }

                            new SetTagGUI((Player) ctx.getSource().getSender(), 1).open();

                            return Command.SINGLE_SUCCESS;
                        })

                        .then(Commands.argument("id", IntegerArgumentType.integer(0))
                            .executes(ctx -> {
                                new SetTag(ctx.getSource().getSender(), ctx.getArgument("id", Integer.class));
                                return Command.SINGLE_SUCCESS;
                        })))
                .then(Commands.literal("list")
                        .requires(ctx->ctx.getSender().hasPermission("lighttag.list"))
                        .executes(ctx -> {
                            new SendPlayerTagList(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("add")
                        .requires(ctx->ctx.getSender().hasPermission("lighttag.add"))
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
                    .requires(ctx->ctx.getSender().hasPermission("lighttag.list"))
                    .executes(ctx -> {
                        new ClearTag(ctx.getSource().getSender());
                        return Command.SINGLE_SUCCESS;
                    }))
                .then(Commands.literal("give")
                        .requires(ctx->ctx.getSender().hasPermission("lighttag.give"))
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
                        .requires(ctx->ctx.getSender().hasPermission("lighttag.almanac"))
                        .executes(ctx -> {
                            new AlmanacOfTags(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("save")
                        .requires(ctx->ctx.getSender().hasPermission("lighttag.save"))
                        .executes(ctx -> {
                            PlayerDatas.writeToFile();
                            Tags.writeToFile();
                            ctx.getSource().getSender().sendMessage(TextUtils.getFormatedLang("system.saved"));
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("help")
                        .requires(ctx->ctx.getSender().hasPermission("lighttag.help"))
                        .executes(ctx -> {
                            new PrintHelp(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("debug")
                        .requires(ctx->ctx.getSender().hasPermission("lighttag.debug"))
                        .then(Commands.literal("save_deserialized_item_to_file")
                                .executes(ctx -> {
                                    new DeserializeItemToFile(ctx.getSource().getSender());
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("buy")
                        .requires(ctx -> ctx.getSender().hasPermission("lighttag.buy"))
                        .then(Commands.argument("id", IntegerArgumentType.integer(0))
                                .executes(ctx -> {
                                    if (!(ctx.getSource().getSender() instanceof Player player)) {
                                        ctx.getSource().getSender().sendMessage(TextUtils.getFormatedLang("system.player-only"));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    new BuyTag(player, ctx.getArgument("id", Integer.class));
                                    return Command.SINGLE_SUCCESS;
                                })));
    }
}
