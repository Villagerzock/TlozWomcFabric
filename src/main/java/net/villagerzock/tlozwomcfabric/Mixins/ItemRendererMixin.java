package net.villagerzock.tlozwomcfabric.Mixins;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.villagerzock.tlozwomcfabric.client.CustomBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow public abstract void renderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model);

    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useFusedModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay){
        if(stack.getNbt() != null){
            if(stack.getNbt().contains("fuseditem")){
                NbtCompound nbt = (NbtCompound) stack.getNbt().get("fuseditem");
                String modid = nbt.getString("item").substring(0, nbt.getString("item").indexOf(":"));
                String item = nbt.getString("item").substring(nbt.getString("item").indexOf(":")).replaceAll(":", "");
                //System.out.println(modid + ":" + item + " in " + renderMode.asString());
                BakedModel NewModel = ((ItemRendererAccessor) this).mccourse$getModels().getModelManager().getModel(new ModelIdentifier(modid, item, "inventory"));
                return new CustomBakedModel(value, NewModel, stack.getItem() instanceof BlockItem, renderMode == ModelTransformationMode.GUI);
            }
        }
        return value;
    }
}
