package net.villagerzock.tlozwomcfabric.client.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LinksInventory extends AbstractInventoryScreen<PlayerScreenHandler> {
    private static final Identifier ITEM_TAB = new Identifier("minecraft","textures/gui/container/creative_inventory/tab_items.png");
    private static final Identifier TABS = new Identifier("minecraft","textures/gui/container/creative_inventory/tabs.png");
    protected int backgroundWidth = 195;
    protected int backgroundHeight = 136;
    public LinksInventory(PlayerEntity player) {
        super(player.playerScreenHandler, player.getInventory(), Text.translatable("container.crafting"));
    }
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) /2;
        context.drawTexture(ITEM_TAB,x,y,0,0,backgroundWidth,backgroundHeight);
    }
}
