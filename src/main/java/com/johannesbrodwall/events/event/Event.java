package com.johannesbrodwall.events.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Event {

    @Getter
    private String displayName;

    public Event(String displayName) {
        this.displayName = displayName;
    }

}
