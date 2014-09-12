package com.johannesbrodwall.events.event;

import org.json.JSONObject;

import com.johannesbrodwall.events.category.Category;
import com.johannesbrodwall.infrastructure.web.JSONConvertible;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Event implements JSONConvertible {

    @Getter @Setter
    private Integer id;

    @Getter
    private final String displayName;

    @Getter
    private Category category;

    @Getter @Setter
    private LocalDate startDate, endDate;

    private Integer categoryId;

    public Event(String displayName, Category category) {
        this.displayName = displayName;
        this.category = category;
    }

    public Event(String displayName, int categoryId) {
        this.displayName = displayName;
        this.categoryId = categoryId;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject()
            .put("displayName", displayName)
            .put("startDate", startDate)
            .put("endDate", endDate)
            .put("category", category.toJSON());
    }

    public static Event fromJSON(JSONObject object) {
        Event event = new Event(object.getString("displayName"), object.getInt("categoryId"));
        event.setStartDate(LocalDate.parse(object.getString("startDate"), DateTimeFormatter.ISO_DATE_TIME));
        event.setEndDate(LocalDate.parse(object.getString("endDate"), DateTimeFormatter.ISO_DATE_TIME));
        return event;
    }

    public Integer getCategoryId() {
        return category != null ? category.getId() : categoryId;
    }

}
