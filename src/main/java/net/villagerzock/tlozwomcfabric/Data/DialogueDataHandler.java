package net.villagerzock.tlozwomcfabric.Data;

import net.minecraft.util.Identifier;
import net.villagerzock.tlozwomcfabric.client.Dialogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogueDataHandler {
    public static Map<Identifier, Dialogue[]> GlobalData = new HashMap<>();
    public static List<Identifier> GlobalDataList = new ArrayList<>();
}
