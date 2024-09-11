package net.villagerzock.tlozwomcfabric;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.villagerzock.tlozwomcfabric.Data.DialogueDataHandler;
import net.villagerzock.tlozwomcfabric.Data.Elements;
import net.villagerzock.tlozwomcfabric.Data.ItemProperties;
import net.villagerzock.tlozwomcfabric.Items.ModItems;
import net.villagerzock.tlozwomcfabric.client.Dialogue;
import net.villagerzock.tlozwomcfabric.client.DialogueButton;
import net.villagerzock.tlozwomcfabric.client.DialogueText;
import net.villagerzock.tlozwomcfabric.commands.ModCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class TlozWomcFabric implements ModInitializer {
    public static String MODID = "tlozwomcfabric";
    public static final Identifier USE_ABILITY_PACKET_ID = new Identifier(TlozWomcFabric.MODID, "use_ability");
    public static final Identifier RUN_COMMAND_ON_SERVER = new Identifier(TlozWomcFabric.MODID, "run_command_on_server");
    public static final Identifier OPEN_DIALOGUE_ON_CLIENT = new Identifier(TlozWomcFabric.MODID, "open_dialogue_on_client");
    public static final Identifier DROP_ITEM_STACK_FROM_INVENTORY = new Identifier(TlozWomcFabric.MODID, "drop_item_stack_from_inventory");
    public static final Logger logger = LoggerFactory.getLogger(MODID);
    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        CommandRegistrationCallback.EVENT.register(ModCommands::registerCommands);
        ServerTickEvents.END_WORLD_TICK.register(server -> {
            int phase = server.getMoonPhase();
            if (phase == 8){
            }
        });
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier(MODID, "my_resources");
            }
            @Override
            public void reload(ResourceManager manager) {
                for(Identifier id : manager.findResources("item_properties", path -> path.getPath().endsWith(".json")).keySet()) {
                    try (InputStream stream = manager.getResource(id).get().getInputStream()){
                        try {
                            Reader reader = new InputStreamReader(stream, "UTF-8");
                            JsonObject object = new Gson().fromJson(reader, JsonObject.class);
                            JsonObject fuse = object.get("fuse").getAsJsonObject();
                            Item item = Registries.ITEM.get(new Identifier(object.get("item").getAsString()));
                            double fuse_damage = fuse.get("damage").getAsDouble();
                            Elements element = Elements.valueOf(fuse.get("element").getAsString().toUpperCase());
                            int fuse_elemet_strenght = fuse.get("strenght").getAsInt();
                            int fuse_durability = fuse.get("durability").getAsInt();
                            ItemProperties.GlobalProperties.put(item, new ItemProperties(fuse_damage, element, fuse_elemet_strenght,fuse_durability));
                        }catch (NullPointerException e){
                            throw new RuntimeException(e);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                for(Identifier id : manager.findResources("dialogues", path -> path.getPath().endsWith(".json")).keySet()) {
                    try (InputStream stream = manager.getResource(id).get().getInputStream()){
                        try {
                            Reader reader = new InputStreamReader(stream, "UTF-8");
                            JsonArray array = new Gson().fromJson(reader, JsonArray.class);
                            Dialogue[] dialogues = new Dialogue[array.size()];
                            for (int i = 0; i<array.size(); i++){
                                JsonObject dialogue = array.get(i).getAsJsonObject();
                                String text = dialogue.get("text").getAsString();
                                String name = dialogue.get("name").getAsString();
                                JsonArray buttons = dialogue.get("buttons").getAsJsonArray();
                                DialogueButton[] dialogueButtons = new DialogueButton[buttons.size()];
                                for (int button = 0; button<buttons.size();button++){
                                    JsonObject buttonObject = buttons.get(button).getAsJsonObject();
                                    String buttonText = buttonObject.get("text").getAsString();
                                    String commandToRun = buttonObject.get("command").getAsString();
                                    boolean shouldClose = buttonObject.get("should_close").getAsBoolean();
                                    dialogueButtons[button] = new DialogueButton(buttonText, commandToRun, shouldClose);
                                }

                                dialogues[i] = new Dialogue(text,dialogueButtons,name);
                            }
                            System.out.println(id.toString());
                            DialogueDataHandler.GlobalData.put(id,dialogues);
                            DialogueDataHandler.GlobalDataList.add(id);
                        }catch (NullPointerException e){
                            throw new RuntimeException(e);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(RUN_COMMAND_ON_SERVER, ((server, player, handler, buf, responseSender) -> {
            try {
                executeServerCommand(server,buf.readString(),player);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }));
        ServerPlayNetworking.registerGlobalReceiver(DROP_ITEM_STACK_FROM_INVENTORY, ((server, player, handler, buf, responseSender) -> {
            ItemStack stack = buf.readItemStack();
            ItemStack item;
            if (stack.getCount() > 64){
                item = new ItemStack(stack.getItem(), 64);
            }else {
                item = stack;
            }
            World world = player.getWorld();
            player.dropItem(item, true, true);
            int slot = player.getInventory().getSlotWithStack(item);
            if (slot >= 0){
                player.getInventory().setStack(slot, ItemStack.EMPTY);
            }

        }));
        ServerPlayNetworking.registerGlobalReceiver(USE_ABILITY_PACKET_ID, ((server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                System.out.println("UseAbility");
                ItemStack stack = player.getInventory().getMainHandStack();
                if (stack.hasNbt()){
                    if (!stack.getNbt().contains("fuseditem")){
                        fuse(player,stack);
                    }
                }else {
                    fuse(player,stack);
                }
                stack.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier("fuse_damage", stack.getNbt().getCompound("fuseditem").getDouble("attack_damage"), EntityAttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
            });
        }));
    }

    private  void fuse(ServerPlayerEntity player, ItemStack stack){
        ItemEntity entity =findNearestItemEntity(player, 5d);
        ItemStack FusedItem = entity.getStack();
        double AttackDamage = 1.0;
        int durability = 0;
        if(FusedItem.getItem() instanceof SwordItem){
            AttackDamage = ((SwordItem) FusedItem.getItem()).getAttackDamage();
            durability = ((SwordItem) FusedItem.getItem()).getMaxDamage();
        }
        if (stack.getItem() instanceof SwordItem){
            AttackDamage += ((SwordItem) stack.getItem()).getAttackDamage();
        }
        NbtCompound newNbt = new NbtCompound();
        String NameSpace = Registries.ITEM.getId(FusedItem.getItem()).getNamespace();
        String ItemName = FusedItem.getItem().toString();
        ItemProperties properties = ItemProperties.GlobalProperties.get(Registries.ITEM.get(new Identifier(NameSpace,ItemName)));
        if(properties != null){
            AttackDamage += properties.getFuseDamage();
            durability += properties.getDurability();
        }
        newNbt.putString("item",NameSpace + ":" + ItemName);
        newNbt.putDouble("attack_damage", AttackDamage);
        System.out.println(NameSpace);
        stack.getNbt().put("fuseditem", newNbt);

        stack.setDamage(-durability);
        player.getInventory().setStack(player.getInventory().selectedSlot,stack);
        entity.kill();
    }
    private ItemEntity findNearestItemEntity(ServerPlayerEntity player, double radius) {
        Vec3d playerPos = player.getPos();
        double nearestDistanceSq = radius * radius;
        ItemEntity nearestEntity = null;

        // Erhalte alle Entities im gegebenen Umkreis
        List<ItemEntity> entities = player.getWorld().getEntitiesByType(EntityType.ITEM, player.getBoundingBox().expand(radius), entity -> true);

        for (ItemEntity entity : entities) {
            double distanceSq = playerPos.squaredDistanceTo(entity.getPos());
            if (distanceSq < nearestDistanceSq) {
                nearestDistanceSq = distanceSq;
                nearestEntity = entity;
            }
        }

        return nearestEntity;
    }
    private void applyDamage(ItemStack stack, ServerPlayerEntity player){

    }
    private void applyGlowingEffect(ServerPlayerEntity player, double radius) {
        Vec3d playerPos = player.getPos();
        Box boundingBox = new Box(playerPos.add(-radius, -radius, -radius), playerPos.add(radius, radius, radius));
        List<ItemEntity> entities = player.getWorld().getEntitiesByType(EntityType.ITEM, boundingBox, entity -> true);

        for (ItemEntity entity : entities) {
            double distanceSq = player.squaredDistanceTo(entity);
            if (distanceSq <= radius * radius) {
                // Sende ein Packet an den Client, um den Glowing-Effekt anzuwenden
                player.networkHandler.sendPacket(new EntityStatusS2CPacket(entity, (byte) 0x1F)); // 0x1F ist der Status für Glowing
                player.sendMessage(Text.literal("Glowing!!!"));
            }
        }
    }    public boolean NbtListContainsCompoundContains(NbtList list){
        for(int i = 0; i<list.size(); i++){
            if(list.getCompound(i).contains("Name")){
                if(list.getCompound(i).getString("Name") == "fused_damage"){
                    return false;
                }
            }
        }
        return true;
    }
    private void executeServerCommand(MinecraftServer server, String command, ServerPlayerEntity player) throws CommandSyntaxException {
        // Eine ServerCommandSource erstellen mit höchster Berechtigungsstufe
        ServerCommandSource commandSource = new ServerCommandSource(
                player, // Der Spieler, der den Befehl sendet
                player.getPos(), // Position des Spielers
                player.getRotationClient(), // Rotation des Spielers
                player.getWorld() instanceof ServerWorld ? (ServerWorld) player.getWorld() : null, // Welt des Spielers
                4, // Berechtigungsstufe (4 ist OP-Berechtigung)
                player.getName().getString(), // Name des Spielers
                player.getDisplayName(), // Anzeige des Namens
                server, // Minecraft-Server
                player // Entity, die den Befehl sendet (der Spieler selbst)
        ).withMaxLevel(4); // Setzt die maximale Berechtigungsstufe auf 4

        // Befehl ausführen
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
        ParseResults<ServerCommandSource> parseResults = dispatcher.parse(command,commandSource);
        dispatcher.execute(parseResults);
    }
    private void serverTick(){
        ServerTickEvents.END_WORLD_TICK.register(serverWorld -> {
            for (ServerPlayerEntity player : serverWorld.getPlayers()){
                for(int i = 0; i<player.getInventory().size(); i++){
                    ItemStack stack = player.getInventory().getStack(i);
                    if (stack.hasNbt()){
                        if (stack.getNbt().contains("fuseditem")){
                            NbtCompound fuseditem = stack.getNbt().getCompound("fuseditem");
                            Double attackDamage = fuseditem.getDouble("attack_damage");
                            if(stack.getItem() instanceof SwordItem){
                                SwordItem item = (SwordItem) stack.getItem();
                                attackDamage += item.getAttackDamage();
                            }
                            EntityAttributeModifier modifier = new EntityAttributeModifier("fused_damage", attackDamage, EntityAttributeModifier.Operation.ADDITION);
                            //AttributeModifiers.Name
                            boolean isThere = NbtListContainsCompoundContains(stack.getNbt().getList("AttributeModifiers", NbtElement.COMPOUND_TYPE));
                            //boolean isThere = !stack.getAttributeModifiers(EquipmentSlot.MAINHAND).containsValue(modifier);
                            if (isThere){
                                stack.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, modifier , EquipmentSlot.MAINHAND);
                                System.out.println("HAllO");
                            }
                            System.out.println(!isThere);
                        }
                    }
                }
            }
        });
    }
}
