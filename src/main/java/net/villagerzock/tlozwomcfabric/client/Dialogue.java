package net.villagerzock.tlozwomcfabric.client;

import com.google.gson.JsonObject;
import net.minecraft.registry.Registries;

import java.util.List;

public class Dialogue {
    private final String text;
    private final DialogueButton[] buttons;
    private final String name;
    public Dialogue(String text, DialogueButton[] buttons, String name){
        this.text = text;
        this.buttons = buttons;
        this.name = name;
    }

    public DialogueButton[] getButtons() {
        return buttons;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
    public static Dialogue serializeFromJsonObjetc(JsonObject object){
        return new Dialogue("hallo",new DialogueButton[]{},"???");
    }
}
