package com.johannesbrodwall.events.calendar;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.johannesbrodwall.events.SampleEventData;
import com.johannesbrodwall.events.category.EventCategory;
import com.johannesbrodwall.events.event.Event;
import com.johannesbrodwall.infrastructure.SampleData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarControllerTest {

    private CalendarController calendarController = new CalendarController();
    private LocalDate startDate = SampleData.randomDate();
    private LocalDate endDate = startDate.plusDays(20);
    private EventCategory category = SampleEventData.sampleCategory();
    private Map<LocalDate, String> holidays = new HashMap<LocalDate, String>();

    @Test
    public void itIncludesAllEvents() {
        List<Event> events = new ArrayList<Event>();
        for (int i = 0; i < 12; i++) {
            events.add(SampleEventData.sampleEvent(SampleEventData.sampleCategory(), startDate));
        }

        JSONObject json = calendarController.getJSON(events, startDate, endDate, holidays);

        assertThat(json.getJSONArray("events").getJSONObject(0).getString("displayName"))
            .isEqualTo(events.get(0).getDisplayName());
        assertThat(json.getJSONArray("events").getJSONObject(11).getString("displayName"))
            .isEqualTo(events.get(11).getDisplayName());
    }

    @Test
    public void itIncludesAllDays() {
        int duration = SampleData.randomInt(10) + 10;
        Event event = SampleEventData.sampleEvent(SampleEventData.sampleCategory());

        JSONObject json = calendarController.getJSON(event, startDate, startDate.plusDays(duration), holidays);

        assertThat(json.getJSONArray("calendar").length()).isEqualTo(duration);
    }

    @Test
    public void itDisplaysWeekends() {
        LocalDate startDate = LocalDate.of(2014, 9, 12);
        LocalDate endDate = LocalDate.of(2014, 9, 16);
        Event event = SampleEventData.sampleEvent(SampleEventData.sampleCategory());

        JSONArray calendar = calendarController.getJSONCalendar(event, startDate, endDate, holidays);

        assertThat(calendar.getJSONObject(0).optString("color")).isNullOrEmpty();
        assertThat(calendar.getJSONObject(1).getString("color")).isEqualTo("gray");
        assertThat(calendar.getJSONObject(2).getString("color")).isEqualTo("gray");
        assertThat(calendar.getJSONObject(3).optString("color")).isNullOrEmpty();
    }

    @Test
    public void itDisplayCalendarDates() {
        LocalDate startDate = LocalDate.of(2014, 9, 9);
        LocalDate endDate = LocalDate.of(2014, 9, 15);
        Event event = new Event("JavaZone", category);
        event.setStartDate(LocalDate.of(2014, 9, 10));
        event.setEndDate(LocalDate.of(2014, 9, 12));

        JSONArray calendar = calendarController.getJSONCalendar(event, startDate, endDate, holidays);

        assertThat(calendar.getJSONObject(1).getString("color")).isEqualTo(category.getColor());
        assertThat(calendar.getJSONObject(2).getString("color")).isEqualTo(category.getColor());
    }

    @Test
    public void itDisplaysHolidays() {
        LocalDate startDate = LocalDate.of(2014, 9, 9);
        LocalDate endDate = LocalDate.of(2014, 9, 15);
        Event event = new Event("JavaZone", category);
        event.setStartDate(LocalDate.of(2014, 9, 10));
        event.setEndDate(LocalDate.of(2014, 9, 13));

        holidays.put(LocalDate.of(2014, 9, 11), "BankID day");

        JSONArray calendar = calendarController.getJSONCalendar(event, startDate, endDate, holidays);

        assertThat(calendar.getJSONObject(2).getString("color")).isEqualTo("purple");
        assertThat(calendar.getJSONObject(2).getString("title")).isEqualTo("BankID day");

    }

}
