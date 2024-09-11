package net.villagerzock.tlozwomcfabric.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.villagerzock.tlozwomcfabric.client.QuickPickerCloseReason;
import net.villagerzock.tlozwomcfabric.client.QuickPickerResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class QuickPicker extends Screen {
    private final int icon;
    private final QuickPickerResult toRun;
    private final int closeInput;
    private static final Identifier TEXTURE = new Identifier("tlozwomcfabric","textures/gui/quick_picker.png");
    private static final Identifier QUICK_ICONS = new Identifier("tlozwomcfabric","textures/gui/quick_icons.png");
    private int scroll = 0;
    private int scrollAnim = 0;
    private static final int perScroll = 2;

    public QuickPicker(int icon, QuickPickerResult toRun, int closeInput) {
        super(Text.literal("QuickPicker"));
        this.icon = icon;
        this.toRun = toRun;
        this.closeInput = closeInput;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(DrawContext context) {
        super.renderBackground(context);
        ClientPlayerEntity player = client.player;
        PlayerInventory inv = player.getInventory();
        List<ItemStack> itemStacks = generateInvList(inv);



        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        context.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0,TEXTURE);
        int x = (width - 24) / 2;
        int y = (height - 24) / 2;
        MatrixStack ms = context.getMatrices();

        for (int i = 0; i<itemStacks.size() + 1;i++){
            ms.push();
            ms.translate(x + (i * 26) * 2 + (-scrollAnim * 2) ,y,0);
            ms.scale(2.0f,2.0f,1.0f);
            context.drawTexture(TEXTURE,0,0,0,0,24,24, 48,24);
            if (i > 0){
                context.drawItemWithoutEntity(itemStacks.get(i-1),4,4);
                String countAsString = String.valueOf(itemStacks.get(i-1).getCount());
                context.drawText(client.textRenderer,Text.literal(countAsString),24 - (countAsString.length() * 6),24,0xFFFFFF,true);
            }else {
                context.drawTexture(QUICK_ICONS,4,4,0,0,16,16, 48,16);
            }
            ms.pop();
        }


        ms.push();
        ms.translate(x,y,0);
        ms.scale(2.0f,2.0f,1.0f);
        context.drawTexture(TEXTURE,0,0,24,0,24,24,48,24);
        ms.pop();


        client.player.sendMessage(Text.literal("Scroll: " + scroll + " Size: " + itemStacks.size()));
        if (scroll > itemStacks.size()){
            scroll = itemStacks.size();
        }
        if (scroll < 0){
            scroll = 0;
        }
        if (scroll * 26 < scrollAnim){
            scrollAnim -= perScroll;
        }else if (scroll * 26 > scrollAnim){
            scrollAnim += perScroll;
        }
    }
    private List<ItemStack> generateInvList(PlayerInventory inv){
        List<ItemStack> itemStacks = new ArrayList<>();
        for (int i = 0; i<inv.main.size();i++){
            ItemStack stack = inv.getStack(i).copy();
            if (!stack.isEmpty()){
                int stackID = checkforitemstackinlist(itemStacks,stack);
                if (stackID != -1){
                    ItemStack OldStack = itemStacks.get(stackID);
                    OldStack.setCount(OldStack.getCount() + stack.getCount());
                    itemStacks.set(stackID,OldStack);
                }else {
                    itemStacks.add(stack);
                }
            }
        }
        return itemStacks;
    }
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (scrollAnim == scroll * 26){
            if (amount < 0){
                scroll++;
            }else {
                scroll--;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    private int checkforitemstackinlist(List<ItemStack> itemStacks, ItemStack stack){
        for (int i = 0; i<itemStacks.size();i++){
            if (itemStacks.get(i).isOf(stack.getItem())){
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == closeInput){
            ClientPlayerEntity player = client.player;
            PlayerInventory inv = player.getInventory();
            List<ItemStack> itemStacks = generateInvList(inv);
            ItemStack stack = ItemStack.EMPTY;
            QuickPickerCloseReason reason;
            if (scroll > 0){
                stack = itemStacks.get(scroll - 1);
                reason = QuickPickerCloseReason.EQUIP;
            }else {
                reason = QuickPickerCloseReason.NOTHING_PICKED;
            }
            toRun.run(stack,reason);
            close();
            //toRun.run();
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
