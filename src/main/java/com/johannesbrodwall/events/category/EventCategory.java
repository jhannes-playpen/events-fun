package com.johannesbrodwall.events.category;

import org.json.JSONObject;

import com.johannesbrodwall.infrastructure.web.JSONConvertible;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class EventCategory implements JSONConvertible {

    @Getter
    private Integer id;

    @Getter
    private String displayName, color;

    public EventCategory(Integer id, String displayName, String color) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject()
            .put("id", id)
            .put("displayName", displayName)
            .put("color", color);
    }

    public static EventCategory fromJSON(JSONObject object) {
        return new EventCategory(
                (Integer)object.opt("id"),
                object.getString("displayName"),
                object.getString("color"));
    }

    void setId(int id) {
        this.id = id;
    }

}
