package net.villagerzock.tlozwomcfabric.client;

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
}
