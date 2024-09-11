package net.villagerzock.tlozwomcfabric.Items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.villagerzock.tlozwomcfabric.TlozWomcFabric;

public class ModItems {
    public static final Item RANDOMIZER_STAFF = registerItem("randomizer_staff", new RandomizerStaff(new Item.Settings()));
    public static final Item ACORN = registerItem("acorn", new Item(new Item.Settings()));
    public static final Item AEROCUDA_EYEBALL = registerItem("aerocuda_eyeball", new Item(new Item.Settings()));
    public static final Item AEROCUDA_WING = registerItem("aerocuda_wing", new Item(new Item.Settings()));
    public static final Item AMBER = registerItem("amber", new Item(new Item.Settings()));
    public static final Item ANCIENT_AROWANA = registerItem("ancient_arowana", new FilledMapItem(new Item.Settings()));
    public static final Item ANCIENT_BLADE = registerItem("ancient_blade", new Item(new Item.Settings()));
    public static final Item AMORANTH = registerItem("amoranth", new Item(new Item.Settings()));
    public static final Item ARMORED_CARP = registerItem("armored_carp", new Item(new Item.Settings()));
    public static final Item ARMORED_PORGY = registerItem("armored_porgy", new Item(new Item.Settings()));
    public static final Item BIG_HEARTY_RADISH = registerItem("big_hearty_radish", new Item(new Item.Settings()));
    public static final Item BIG_HEARTY_TRUFFLE = registerItem("big_hearty_truffle", new Item(new Item.Settings()));
    public static final Item BIRD_EGG = registerItem("bird_egg", new Item(new Item.Settings()));
    public static final Item BLACK_BOKOBLIN_HORN = registerItem("black_bokoblin_horn", new Item(new Item.Settings()));
    public static final Item BLACK_BOSS_BOKOBLIN_HORN = registerItem("black_boss_bokoblin_horn", new Item(new Item.Settings()));
    public static final Item BLACK_HINOX_HORN = registerItem("black_hinox_horn", new Item(new Item.Settings()));
    public static final Item BLACK_HORRIBLIN_HORN = registerItem("black_horriblin_horn", new Item(new Item.Settings()));
    public static final Item BLACK_LIZALFOS_HORN = registerItem("black_lizalfos_horn", new Item(new Item.Settings()));
    public static final Item BLACK_LIZALFOS_TAIL = registerItem("black_lizalfos_tail", new Item(new Item.Settings()));
    public static final Item BLACK_MOBLIN_HORN = registerItem("black_moblin_horn", new Item(new Item.Settings()));
    public static final Item BLADED_RHINO_BEETLE = registerItem("bladed_rhino_beetle", new Item(new Item.Settings()));
    public static final Item BLUE_BOKOBLIN_HORN = registerItem("blue_bokoblin_horn", new Item(new Item.Settings()));
    public static final Item BLUE_BOSS_BOKOBLIN_HORN = registerItem("blue_boss_bokoblin_horn", new Item(new Item.Settings()));

    private static void addItemsToIngredientTab(FabricItemGroupEntries entries){
        entries.add(ACORN);
    }
    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM,new Identifier(TlozWomcFabric.MODID,name), item);
    }
    public static void registerModItems(){
        TlozWomcFabric.logger.info("Registering Items");

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::addItemsToIngredientTab);
    }
}
