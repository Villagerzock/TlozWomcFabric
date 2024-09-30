package net.villagerzock.tlozwomcfabric.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.villagerzock.tlozwomcfabric.client.QuickPickerCloseReason;
import net.villagerzock.tlozwomcfabric.client.QuickPickerResult;
import org.joml.Quaternionf;

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
    private static final int perScroll = 8;

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
    public void renderBackground(DrawContext drawContext) {
        CustomDrawContext context = new CustomDrawContext(client,drawContext.getVertexConsumers());
        super.renderBackground(context);
        MatrixStack ms = context.getMatrices();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        ms.push();
        ms.translate(0.0F,0.0F,201.0F);
        context.fillVertGradient(0, 0, 50, this.height,0, 0x00000000, 0xFFFFFFFF);
        context.fillVertGradient(this.width-50, 0, this.width, this.height,0, 0xFFFFFFFF, 0x00000000);
        ms.pop();
        RenderSystem.disableBlend();


        ClientPlayerEntity player = client.player;
        PlayerInventory inv = player.getInventory();
        List<ItemStack> itemStacks = generateInvList(inv);



        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        context.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0,TEXTURE);
        int x = (width - 24) / 2;
        int y = (height - 24) / 2;
        for (int i = 0; i<itemStacks.size() + 1;i++){
            ms.push();
            ms.translate(x + (i * 26) * 2 + (-scrollAnim * 2) ,y,0);
            ms.scale(2.0f,2.0f,1.0f);
            context.drawTexture(TEXTURE,0,0,0,0,24,24, 48,24);
            if (i > 0){
                context.drawItemWithoutEntity(itemStacks.get(i-1),4,4);
                ms.translate(0.0F,0.0F,200.0F);
                ms.scale(0.5f,0.5f,0.5f);
                context.drawText(client.textRenderer,Text.literal("x" + itemStacks.get(i-1).getCount()),6,32,0xFFFFFF,true);
            }else {
                ms.translate(0.0F,0.0F,200.0F);
                context.drawTexture(QUICK_ICONS,4,4,16 * icon,0,16,16, 48,16);
            }
            ms.pop();
        }
        ms.push();
        ms.translate(x,y,201.0F);
        ms.scale(2.0f,2.0f,1.0f);
        context.drawTexture(TEXTURE,0,0,24,0,24,24,48,24);
        ms.pop();


        //client.player.sendMessage(Text.literal("Scroll: " + scroll + " Size: " + itemStacks.size()));
        int tabAmount = itemStacks.size() + 1;
        scroll += tabAmount;
        scroll = Math.floorMod(scroll,tabAmount);
        //player.sendMessage(Text.literal(String.valueOf(scroll)));
        if (scroll * 26 < scrollAnim){
            scrollAnim -= perScroll;
            if (scroll * 26 > scrollAnim){
                scrollAnim = scroll * 26;
            }
        }else if (scroll * 26 > scrollAnim){
            scrollAnim += perScroll;
            if (scroll * 26 < scrollAnim){
                scrollAnim = scroll * 26;
            }
        }
    }
    public static List<ItemStack> generateInvList(PlayerInventory inv){
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
    public ItemStack findItemInInventory(PlayerInventory inv,Item item){
        ItemStack itemStacks = ItemStack.EMPTY;
        int lastHighest = 0;
        for (int i = 0; i<inv.main.size();i++){
            ItemStack stack = inv.getStack(i).copy();
            if (!stack.isEmpty() && stack.isOf(item)){
                if (lastHighest < stack.getCount()){
                    lastHighest = stack.getCount();
                    itemStacks = stack;
                }
            }
        }
        return itemStacks;
    }
    private int findItemSlotInInventory(PlayerInventory inv,Item item){
        int itemStacks = 0;
        int lastHighest = 0;
        for (int i = 0; i<inv.main.size();i++){
            ItemStack stack = inv.getStack(i).copy();
            if (!stack.isEmpty() && stack.isOf(item)){
                if (lastHighest < stack.getCount()){
                    lastHighest = stack.getCount();
                    itemStacks = i;
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

    public static int checkforitemstackinlist(List<ItemStack> itemStacks, ItemStack stack){
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
            int Slot = 0;
            QuickPickerCloseReason reason;
            if (scroll > 0){
                Item item = itemStacks.get(scroll - 1).getItem();
                stack = findItemInInventory(inv,item);
                Slot = findItemSlotInInventory(inv,item);
                reason = QuickPickerCloseReason.EQUIP;
            }else {
                reason = QuickPickerCloseReason.NOTHING_PICKED;
            }
            toRun.run(stack, Slot,reason);
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
