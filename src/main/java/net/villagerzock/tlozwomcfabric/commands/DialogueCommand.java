package net.villagerzock.tlozwomcfabric.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.villagerzock.tlozwomcfabric.Data.DialogueDataHandler;
import net.villagerzock.tlozwomcfabric.TlozWomcFabric;

public class DialogueCommand {

    public static int execute(CommandContext<ServerCommandSource> context) {
        if (DialogueDataHandler.GlobalData.containsKey(IdentifierArgumentType.getIdentifier(context,"dialogue"))){
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeString(IdentifierArgumentType.getIdentifier(context,"dialogue").toString());
            ServerPlayNetworking.send(context.getSource().getPlayer(), TlozWomcFabric.OPEN_DIALOGUE_ON_CLIENT, buf);
            return 1;
        }else {
            context.getSource().sendError(Text.literal(IdentifierArgumentType.getIdentifier(context,"dialogue").toString() + " " + Text.translatable("command.exception.dialogue.wrong").getString()));
            return 0;
        }
    }
}
