package com.johannesbrodwall.events;

import com.johannesbrodwall.events.category.EventCategory;
import com.johannesbrodwall.events.event.Event;
import com.johannesbrodwall.infrastructure.SampleData;


public class SampleEventData extends SampleData {

    public static EventCategory sampleCategory() {
        return new EventCategory(randomWords(2), randomColor());
    }

    public static Event sampleEvent() {
        return new Event(randomWords(3));
    }

}
