package net.villagerzock.tlozwomcfabric.client.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.villagerzock.tlozwomcfabric.Mixins.PlayerEntityMixin;

public class LinksInventory extends AbstractInventoryScreen<LinksInventoryHandler> {
    private static final Identifier INVENTORY = new Identifier("tlozwomcfabric","textures/gui/inventory.png");
    protected int backgroundWidth = 195;
    protected int backgroundHeight = 136;

    public LinksInventory(PlayerEntity user) {
        super(new LinksInventoryHandler(user.getInventory(),!user.getEntityWorld().isClient,user), user.getInventory(), Text.literal(""));
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) /2;
        context.drawTexture(INVENTORY, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }
}
