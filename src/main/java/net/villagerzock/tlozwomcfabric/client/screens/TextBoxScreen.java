package net.villagerzock.tlozwomcfabric.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteDimensions;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.villagerzock.tlozwomcfabric.Data.DialogueDataHandler;
import net.villagerzock.tlozwomcfabric.TlozWomcFabric;
import net.villagerzock.tlozwomcfabric.client.Dialogue;
import net.villagerzock.tlozwomcfabric.client.DialogueButton;
import net.villagerzock.tlozwomcfabric.client.TlozWomcFabricClient;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.Map;

public class TextBoxScreen extends Screen {
    //private final NbtCompound content;
    private final Dialogue[] dialogues;
    protected int backgroundWidth = 248;
    protected int backgroundHeight = 76;
    public boolean arrowAnim = false;
    protected int arrowWidth = 11;
    protected int arrowHeight = 12;
    private static final Identifier TEXTURE = new Identifier("tlozwomcfabric","textures/gui/dialogue_box.png");
    private int tick = 0;
    private int writingTick = 0;
    private int page = 0;
    private DialogueButtonWidget[] buttons;

    public TextBoxScreen(Identifier id) {
        super(Text.literal("???"));
        //this.content = content;
        System.out.println(id.toString());
        this.dialogues = DialogueDataHandler.GlobalData.get(id);

    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private void generateButtons(boolean shouldDelete) {
        if (shouldDelete){
            for (int i = 0; i<dialogues[page-1].getButtons().length; i++){
                remove(buttons[i]);
            }
        }
        buttons = new DialogueButtonWidget[dialogues[page].getButtons().length];
        for (int i = 0; i<dialogues[page].getButtons().length;i++){
            buttons[i] = dialogues[page].getButtons()[i].getAsWidget();
            buttons[i].setPosition((width + backgroundWidth) / 2 + 4, (height - backgroundHeight) -10 + (23 * i));
            addDrawableChild(buttons[i]);
        }

    }


    @Override
    protected void init() {
        super.init();
        generateButtons(false);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(DrawContext context) {


        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0,TEXTURE);
        String text = Text.translatable(dialogues[page].getText()).getString();
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) - 10;
        context.drawTexture(TEXTURE,x,y,0,0,backgroundWidth,backgroundHeight);
        context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal(dialogues[page].getName()),x + 4, y -8, 16777215, true);
        renderText(context,y,text);
        if (writingTick >= text.length()){
            arrowAnim = true;
        }
        tick++;
        if(Math.floorMod(tick, 5) == 1 && ! arrowAnim){

            writingTick++;
        }

        renderArrow(context);
    }
    private void renderText(DrawContext context, int y, String text){

        int maxWrite = Math.min(writingTick,text.length());

        String textToDraw = text.substring(0,maxWrite);
        int i = 0;
        while (textToDraw.endsWith("§")){
            writingTick += 2;
            maxWrite = Math.min(writingTick,text.length());
            textToDraw = text.substring(0,maxWrite);

            if (i >= 500)
                break;
            i++;
        }
        String[] lines = textToDraw.split("\n");
        int lineOffset = 0;
        for (String line : lines){
            Text lineText = Text.translatable(line);
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, lineText, width / 2, y+4 + lineOffset,16777215);
            lineOffset += MinecraftClient.getInstance().textRenderer.fontHeight;
        }
    }
    private void renderArrow(DrawContext context){
        int x = (width - arrowWidth) / 2;
        int y = (height - arrowHeight) - 10;
        if (arrowAnim){
            context.drawTexture(TEXTURE,x, (int) (y + Math.round(Math.sin(tick / 20) * 2)) + 2 ,108,76,arrowWidth,arrowHeight);

        }

    }
    public void nextPage(){
        if (arrowAnim){
            if (page < dialogues.length - 1){
                page++;
                generateButtons(true);
                arrowAnim = false;
                writingTick = 0;
            }else {
                close();
            }
        }
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_SPACE){
            nextPage();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}
