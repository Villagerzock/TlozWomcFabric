package net.villagerzock.tlozwomcfabric.Registry;

import com.google.common.collect.Maps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.StructureType;
import net.villagerzock.tlozwomcfabric.client.Dialogue;

import java.util.Map;
import java.util.function.Supplier;

public class ModRegistries extends Registries{
    /*private static final MutableRegistry<MutableRegistry<?>> ROOT;
    private static final Map<Identifier, Supplier<?>> DEFAULT_ENTRIES = Maps.newLinkedHashMap();
    public static final Registry<RecipeType<?>> DIALOGUE_TYPE;
    public static final Registry<RecipeSerializer<?>> DIALOGUE_SERIALIZER;
    public static final RegistryKey<Registry<RecipeSerializer<?>>> DIALOGUE_SERIALIZER_KEY = of("dialogue_serializer");
    public static final RegistryKey<Registry<RecipeType<?>>> DIALOGUE_TYPE_KEY = of("dialogue_type");
    private static <T> Registry<T> create(RegistryKey<? extends Registry<T>> key, Initializer<T> initializer) {
        return create(key, Lifecycle.stable(), initializer);
    }
    private static <T> Registry<T> create(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle, Initializer<T> initializer) {
        return create(key, (MutableRegistry)(new SimpleRegistry(key, lifecycle, false)), (Initializer)initializer, (Lifecycle)lifecycle);
    }
    private static <T, R extends MutableRegistry<T>> R create(RegistryKey<? extends Registry<T>> key, R registry, Initializer<T> initializer, Lifecycle lifecycle) {
        Identifier identifier = key.getValue();
        DEFAULT_ENTRIES.put(identifier, () -> {
            return initializer.run(registry);
        });
        ROOT.add((RegistryKey<MutableRegistry<?>>) key, registry, lifecycle);

        return registry;
    }
    static {
        ROOT = new SimpleRegistry(RegistryKey.ofRegistry(ROOT_KEY), Lifecycle.stable());
        DIALOGUE_TYPE = create(DIALOGUE_TYPE_KEY, (registry) -> {
            return RecipeType.CRAFTING;
        });
        DIALOGUE_SERIALIZER = create(DIALOGUE_SERIALIZER_KEY, (registry) -> {
            return RecipeSerializer.SHAPELESS;
        });
    }
    @FunctionalInterface
    private interface Initializer<T> {
        T run(Registry<T> registry);
    }
    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(new Identifier(id));
    }*/
}
