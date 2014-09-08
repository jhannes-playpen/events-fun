package com.johannesbrodwall.events;

import com.johannesbrodwall.infrastructure.AppConfiguration;

import java.io.File;

public class EventsAppConfiguration extends AppConfiguration {

    public EventsAppConfiguration(String filename) {
        this.file = new File(filename);
    }

    private static final EventsAppConfiguration instance = new EventsAppConfiguration("events.properties");

    public static EventsAppConfiguration getInstance() {
        return instance;
    }
}
