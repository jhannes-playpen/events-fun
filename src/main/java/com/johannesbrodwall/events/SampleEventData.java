package com.johannesbrodwall.events;

import com.johannesbrodwall.events.category.EventCategory;
import com.johannesbrodwall.events.event.Event;
import com.johannesbrodwall.events.project.Project;
import com.johannesbrodwall.infrastructure.SampleData;


public class SampleEventData extends SampleData {

    public static EventCategory sampleCategory() {
        return new EventCategory(null, randomWords(2), randomColor());
    }

    public static Event sampleEvent(EventCategory category) {
        Event event = new Event(randomWords(3), category);
        event.setStartDate(randomDate());
        event.setEndDate(event.getStartDate().plusDays(randomInt(30)));
        return event;
    }

    public static Project sampleProject() {
        return new Project(randomWords(3));
    }

    public static Project sampleProject(Project parent) {
        Project project = sampleProject();
        project.setParent(parent);
        return project;
    }

}
