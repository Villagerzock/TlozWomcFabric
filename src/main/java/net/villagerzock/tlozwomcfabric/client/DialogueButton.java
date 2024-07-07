package net.villagerzock.tlozwomcfabric.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.villagerzock.tlozwomcfabric.client.screens.DialogueButtonWidget;
import net.villagerzock.tlozwomcfabric.client.screens.TextBoxScreen;

public class DialogueButton {
    private final String ButtonText;
    private final String CommandToRun;
    private final boolean shouldClose;
    public DialogueButton(String buttonText, String commandToRun, boolean shouldClose){
        ButtonText = buttonText;
        CommandToRun = commandToRun;
        this.shouldClose = shouldClose;
    }
    public DialogueButtonWidget getAsWidget(){
        return new DialogueButtonWidget(0,0,108,21, Text.literal(ButtonText), new ButtonWidget.PressAction() {
            @Override
            public void onPress(ButtonWidget button) {
                if (MinecraftClient.getInstance().currentScreen instanceof TextBoxScreen){
                    TextBoxScreen screen = (TextBoxScreen) MinecraftClient.getInstance().currentScreen;
                    if (screen.arrowAnim){
                        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                        buf.writeString(CommandToRun);
                        ClientPlayNetworking.send(TlozWomcFabricClient.RUN_COMMAND_ON_SERVER,buf);
                        screen.nextPage();
                    }
                }
            }

        });
    }
}
