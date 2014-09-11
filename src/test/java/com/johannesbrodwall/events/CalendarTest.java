package com.johannesbrodwall.events;

import static org.assertj.core.api.Assertions.assertThat;
import net.fortuna.ical4j.data.ParserException;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class CalendarTest {

    @Test
    public void shouldParseICalFeed() throws IOException, ParserException {
        HolidayCalendar holidays = new HolidayCalendar(new URL("file:src/test/resources/ics_2014_norway.ics"));

        List<CalendarEvent> events = holidays.getEvents();
        assertThat(events).hasSize(96);
        assertThat(events.get(0).getSummary()).isEqualTo("New Years Day");
        assertThat(events.get(0).getDate()).isEqualTo(LocalDate.of(2008, 1, 1));

        assertThat(holidays.get(LocalDate.of(2014, 5, 17))).isEqualTo("Constitution Day");
    }

    @Test
    public void shouldParseICalWebFeed() throws IOException {
        HolidayCalendar holidays = new HolidayCalendar(new URL("http://www.officeholidays.com/ics/ics_country.php?tbl_country=Norway"));

        assertThat(holidays.get(LocalDate.of(2014, 5, 17))).isEqualTo("Constitution Day");
        assertThat(holidays.getHolidays().get(LocalDate.of(2014, 5, 17))).isEqualTo("Constitution Day");
    }


}
