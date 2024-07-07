package net.villagerzock.tlozwomcfabric.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomBakedModel implements BakedModel {
    private final BakedModel OldModel;
    private final BakedModel NewModel;
    private final boolean isBlock;
    private final boolean isGUI;
    public CustomBakedModel(BakedModel oldModel, BakedModel newModel, boolean isBlock, boolean isGUI){
        OldModel = oldModel;
        NewModel = newModel;
        this.isBlock = isBlock;
        this.isGUI = isGUI;
    }
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        List<BakedQuad> quads = new ArrayList<>();
        if (OldModel.getTransformation() != null) {
            quads.addAll(OldModel.getQuads(state, face, random));
        }
        if (NewModel.getTransformation() != null) {
            if(isBlock){
                quads.addAll(getTranslatedQuads(getScaledQuads(NewModel.getQuads(state, face, random))));
            }else {
                quads.addAll(getTranslatedQuads(NewModel.getQuads(state, face, random)));
            }
        }
        if(isGUI){
            return getScaledQuads(quads);
        }
        return quads;
    }
    public static List<BakedQuad> getTranslatedQuads(List<BakedQuad> quads){
        List<BakedQuad> toReturn = new ArrayList<>();
        for (int i = 0; i<quads.size(); i++){
            toReturn.add(translateQuad(quads.get(i),0.8f,0.8f,0));
        }
        return toReturn;
    }
    public static List<BakedQuad> getScaledQuads(List<BakedQuad> quads){
        List<BakedQuad> toReturn = new ArrayList<>();
        for (int i = 0; i<quads.size(); i++){
            toReturn.add(scaleQuad(quads.get(i),0.5f,0.5f,0.5f));
        }
        return toReturn;
    }
    public static BakedQuad translateQuad(BakedQuad quad, float x, float y, float z) {
        int[] vertexData = quad.getVertexData().clone();

        for (int i = 0; i < 4; i++) {
            int vertexOffset = i * 8;

            float vx = Float.intBitsToFloat(vertexData[vertexOffset]);
            float vy = Float.intBitsToFloat(vertexData[vertexOffset + 1]);
            float vz = Float.intBitsToFloat(vertexData[vertexOffset + 2]);

            vx += x;
            vy += y;
            vz += z;

            vertexData[vertexOffset] = Float.floatToRawIntBits(vx);
            vertexData[vertexOffset + 1] = Float.floatToRawIntBits(vy);
            vertexData[vertexOffset + 2] = Float.floatToRawIntBits(vz);
        }

        return new BakedQuad(vertexData, quad.getColorIndex(), quad.getFace(), quad.getSprite(), quad.hasShade());
    }
    public static BakedQuad scaleQuad(BakedQuad quad, float scaleX, float scaleY, float scaleZ) {
        int[] vertexData = quad.getVertexData().clone();

        for (int i = 0; i < 4; i++) {
            int vertexOffset = i * 8;

            float vx = Float.intBitsToFloat(vertexData[vertexOffset]);
            float vy = Float.intBitsToFloat(vertexData[vertexOffset + 1]);
            float vz = Float.intBitsToFloat(vertexData[vertexOffset + 2]);

            vx *= scaleX;
            vy *= scaleY;
            vz *= scaleZ;

            vertexData[vertexOffset] = Float.floatToRawIntBits(vx);
            vertexData[vertexOffset + 1] = Float.floatToRawIntBits(vy);
            vertexData[vertexOffset + 2] = Float.floatToRawIntBits(vz);
        }

        return new BakedQuad(vertexData, quad.getColorIndex(), quad.getFace(), quad.getSprite(), quad.hasShade());
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return OldModel.getParticleSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return OldModel.getTransformation();
    }

    @Override
    public ModelOverrideList getOverrides() {
        return OldModel.getOverrides();
    }
}