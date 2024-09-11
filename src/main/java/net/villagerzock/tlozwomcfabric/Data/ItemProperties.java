package net.villagerzock.tlozwomcfabric.Data;

import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemProperties {
    public static Map<Item, ItemProperties> GlobalProperties = new HashMap<>();
    private final double FUSE_DAMAGE;
    private final Elements FUSE_ELEMENT;
    private final int FUSE_ELEMENT_STRENGHT;
    private final int DURABILITY;

    public ItemProperties(double fuseDamage, Elements fuseElement, int fuseElementStrenght,int Durability) {
        FUSE_DAMAGE = fuseDamage;
        FUSE_ELEMENT = fuseElement;
        FUSE_ELEMENT_STRENGHT = fuseElementStrenght;
        DURABILITY = Durability;
    }

    public int getDurability() {
        return DURABILITY;
    }

    public double getFuseDamage() {
        return FUSE_DAMAGE;
    }
}
