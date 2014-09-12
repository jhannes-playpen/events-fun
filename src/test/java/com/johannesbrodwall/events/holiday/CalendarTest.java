package com.johannesbrodwall.events.holiday;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class CalendarTest {

    @Test
    public void shouldParseICalFeed() throws IOException {
        HolidayCalendar holidays = new HolidayCalendar(new URL("file:src/test/resources/ics_2014_norway.ics"));

        List<CalendarEvent> events = holidays.getEvents();
        assertThat(events).hasSize(96);
        assertThat(events.get(0).getSummary()).isEqualTo("New Years Day");
        assertThat(events.get(0).getDate()).isEqualTo(LocalDate.of(2008, 1, 1));

        assertThat(holidays.get(LocalDate.of(2014, 5, 17))).isEqualTo("Constitution Day");
    }

}
