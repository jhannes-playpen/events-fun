package com.johannesbrodwall.events;

import org.json.JSONObject;

public class EventCategory implements JSONConvertible {

    private String displayName;
    private String color;

    public EventCategory(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject()
            .put("displayName", displayName)
            .put("color", color);
    }

}
