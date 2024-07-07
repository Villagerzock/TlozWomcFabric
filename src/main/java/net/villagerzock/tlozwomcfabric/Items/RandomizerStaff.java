package net.villagerzock.tlozwomcfabric.Items;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.villagerzock.tlozwomcfabric.TlozWomcFabric;
import net.villagerzock.tlozwomcfabric.client.screens.TextBoxScreen;

public class RandomizerStaff extends Item {
    public RandomizerStaff(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient){
            MinecraftClient.getInstance().setScreen(new TextBoxScreen(new Identifier(TlozWomcFabric.MODID,"dialogues/randomizer/staff_01.json")));
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
