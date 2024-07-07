package net.villagerzock.tlozwomcfabric.Data;

import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemProperties {
    public static Map<Item, ItemProperties> GlobalProperties = new HashMap<>();
    private final double FUSE_DAMAGE;
    private final Elements FUSE_ELEMENT;
    private final int FUSE_ELEMENT_STRENGHT;

    public ItemProperties(double fuseDamage, Elements fuseElement, int fuseElementStrenght) {
        FUSE_DAMAGE = fuseDamage;
        FUSE_ELEMENT = fuseElement;
        FUSE_ELEMENT_STRENGHT = fuseElementStrenght;
    }

    public double getFuseDamage() {
        return FUSE_DAMAGE;
    }
}
