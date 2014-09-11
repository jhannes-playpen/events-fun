package com.johannesbrodwall.events.event;

import com.johannesbrodwall.events.category.EventCategory;

import java.time.LocalDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Event {

    @Getter @Setter
    private Integer id;

    @Getter
    private final String displayName;

    @Getter
    private final EventCategory category;

    @Getter @Setter
    private LocalDate startDate, endDate;

    public Event(String displayName, EventCategory category) {
        this.displayName = displayName;
        this.category = category;
    }

}
