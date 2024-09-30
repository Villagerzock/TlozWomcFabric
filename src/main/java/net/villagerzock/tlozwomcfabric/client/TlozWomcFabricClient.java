package net.villagerzock.tlozwomcfabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.villagerzock.tlozwomcfabric.GameModes.Link.client.HUD.LinkHUD;
import net.villagerzock.tlozwomcfabric.TlozWomcFabric;
import net.villagerzock.tlozwomcfabric.client.screens.QuickPicker;
import net.villagerzock.tlozwomcfabric.client.screens.TextBoxScreen;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20C;

import java.io.IOException;

public class TlozWomcFabricClient implements ClientModInitializer {
    public static KeyBinding useAbilityKeyBind;
    public static KeyBinding materialsKeyBind;
    public static KeyBinding shieldKeyBind;
    public static KeyBinding swordKeyBind;
    public static KeyBinding whistleKeyBind;
    public static DrawContext context = null;
    public static final Identifier USE_ABILITY_PACKET_ID = new Identifier(TlozWomcFabric.MODID, "use_ability");
    public static final Identifier ACTIVATE_ABILITY = new Identifier(TlozWomcFabric.MODID, "activate_ability");
    public static final Identifier RUN_COMMAND_ON_SERVER = new Identifier(TlozWomcFabric.MODID, "run_command_on_server");
    public static final Identifier DROP_ITEM_STACK_FROM_INVENTORY = new Identifier(TlozWomcFabric.MODID, "drop_item_stack_from_inventory");

    @Override
    public void onInitializeClient() {

        useAbilityKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tlozwomcfabric.use_ability",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.tlozwomcfabric.keybinds"
        ));
        materialsKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tlozwomcfabric.materials",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UP,
                "category.tlozwomcfabric.keybinds"
        ));
        shieldKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tlozwomcfabric.shields",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT,
                "category.tlozwomcfabric.keybinds"
        ));
        swordKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tlozwomcfabric.weapons",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT,
                "category.tlozwomcfabric.keybinds"
        ));
        whistleKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tlozwomcfabric.whistle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_DOWN,
                "category.tlozwomcfabric.keybinds"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (useAbilityKeyBind.wasPressed()){
                ClientPlayNetworking.send(USE_ABILITY_PACKET_ID, PacketByteBufs.empty());
                System.out.println("UseAbility");
            }
            while (materialsKeyBind.wasPressed()){
                client.setScreen(new QuickPicker(0, new QuickPickerResult() {
                    @Override
                    public void run(ItemStack item, int slot,QuickPickerCloseReason reason) {
                        if (reason == QuickPickerCloseReason.EQUIP){
                            ClientPlayNetworking.send(DROP_ITEM_STACK_FROM_INVENTORY, PacketByteBufs.create().writeItemStack(item).writeVarInt(slot));
                        }
                    }
                },InputUtil.fromTranslationKey(materialsKeyBind.getBoundKeyTranslationKey()).getCode()));
            }
            while (swordKeyBind.wasPressed()){
                client.setScreen(new QuickPicker(1, new QuickPickerResult() {
                    @Override
                    public void run(ItemStack item, int slot, QuickPickerCloseReason reason) {
                        if (reason == QuickPickerCloseReason.EQUIP){
                            ClientPlayNetworking.send(TlozWomcFabric.EQUIP_ITEM_IN_SLOT, PacketByteBufs.create().writeItemStack(item).writeVarInt(slot));
                        }
                    }
                },InputUtil.fromTranslationKey(swordKeyBind.getBoundKeyTranslationKey()).getCode()));
            }
            while (shieldKeyBind.wasPressed()){
                client.setScreen(new QuickPicker(2, new QuickPickerResult() {
                    @Override
                    public void run(ItemStack item, int slot, QuickPickerCloseReason reason) {
                        if (reason == QuickPickerCloseReason.EQUIP){
                            ClientPlayNetworking.send(TlozWomcFabric.EQUIP_ITEM_IN_SLOT_TO_OFF, PacketByteBufs.create().writeItemStack(item).writeVarInt(slot));
                        }
                    }
                },InputUtil.fromTranslationKey(shieldKeyBind.getBoundKeyTranslationKey()).getCode()));
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(TlozWomcFabric.OPEN_DIALOGUE_ON_CLIENT, (client, handler, buf, responseSender) -> {
            client.setScreen(new TextBoxScreen(new Identifier(buf.readString())));

        });
        HudRenderCallback.EVENT.register(new LinkHUD());
    }
}
