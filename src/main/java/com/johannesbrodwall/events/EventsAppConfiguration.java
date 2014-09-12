package com.johannesbrodwall.events;

import com.johannesbrodwall.infrastructure.AppConfiguration;

public class EventsAppConfiguration extends AppConfiguration {

    public EventsAppConfiguration(String filename) {
        super(filename);
    }

    private static final EventsAppConfiguration instance = new EventsAppConfiguration("events.properties");

    public static EventsAppConfiguration getInstance() {
        return instance;
    }
}
