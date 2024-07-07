package net.villagerzock.tlozwomcfabric.client.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.villagerzock.tlozwomcfabric.TlozWomcFabric;

public class DialogueButtonWidget extends ButtonWidget {
    private static final Identifier TEXTURE = new Identifier("tlozwomcfabric","textures/gui/dialogue_box.png");
    public DialogueButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.getTextureManager().bindTexture(TEXTURE);
        drawTexture(context, TEXTURE, this.getX(),this.getY(),0,76,21,this.width,this.height,256,256);
        int textColor = 0xFFFFFFFF;
        context.drawText(client.textRenderer,getMessage(),this.getX() + 4,this.getY() + (this.height - 8) / 2,textColor,true);
    }
}
