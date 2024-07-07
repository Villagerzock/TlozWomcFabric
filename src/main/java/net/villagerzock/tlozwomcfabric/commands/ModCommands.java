package net.villagerzock.tlozwomcfabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.villagerzock.tlozwomcfabric.Data.DialogueDataHandler;
import net.villagerzock.tlozwomcfabric.client.Dialogue;

import java.util.concurrent.CompletableFuture;

public class ModCommands {

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("dialogue")
                .then(CommandManager.argument("dialogue", IdentifierArgumentType.identifier())
                        .executes(DialogueCommand::execute)
                        .suggests(new SuggestionProvider<ServerCommandSource>() {
                            @Override
                            public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
                                for (Identifier option : DialogueDataHandler.GlobalDataList){
                                    builder.suggest(option.toString());
                                }
                                return builder.buildFuture();
                            }
                        })));
    }
}
