package net.villagerzock.tlozwomcfabric.Mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.villagerzock.tlozwomcfabric.Accessors.PlayerGetAccessor;
import net.villagerzock.tlozwomcfabric.client.screens.LinksInventoryHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerGetAccessor {

    public LinksInventoryHandler currentLinksInventoryHandler;
    protected int ShrineAmount = 0;
    protected int RootAmount = 0;
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void PlayerEntityInit(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci){
        PlayerEntity __ = ((PlayerEntity) (Object) this);
        this.currentLinksInventoryHandler = new LinksInventoryHandler(__.getInventory(),!world.isClient,__);
    }
    public int getShrineAmount() {
        return ShrineAmount;
    }

    @Override
    public void increaseShrineAmount(int amount) {
        ShrineAmount += amount;
    }

    @Override
    public int getRootAmount() {
        return RootAmount;
    }

    @Override
    public void increaseRootAmount(int amount) {
        RootAmount += amount;
    }

    @Inject(method = "readCustomDataFromNbt",at = @At(value = "RETURN"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci){
        ShrineAmount = nbt.getInt("shrines");
    }
    @Inject(method = "writeCustomDataToNbt",at = @At(value = "RETURN"))
    public void writeCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci){
        nbt.putInt("shrines",ShrineAmount);
    }
}
