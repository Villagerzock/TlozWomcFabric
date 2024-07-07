package net.villagerzock.tlozwomcfabric.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.text.Text;

public class DialogueText {

    private final JsonElement text;

    public DialogueText(JsonElement text) {
        this.text = text;
    }

    public Text getText() {
        return Text.Serializer.fromJson(text);
    }
}
