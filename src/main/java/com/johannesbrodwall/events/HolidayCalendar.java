package com.johannesbrodwall.events;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HolidayCalendar {

    private List<CalendarEvent> events = new ArrayList<CalendarEvent>();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public HolidayCalendar(URL url) throws IOException {
        try (InputStream in = url.openStream(); Reader reader = new InputStreamReader(in)) {
            readCalendar(reader);
        }
    }

    private void readCalendar(Reader in) throws IOException {
        try(BufferedReader reader = new BufferedReader(in)) {
            String line;
            CalendarEvent currentEvent = null;
            while ((line = reader.readLine()) != null) {
                if (line.equals("BEGIN:VEVENT")) {
                    currentEvent = new CalendarEvent();
                } else if (line.startsWith("DTSTART:")) {
                    currentEvent.setDate(LocalDate.parse(line.substring("DTSTART:".length()), dateFormatter));
                } else if (line.startsWith("SUMMARY;")) {
                    currentEvent.setSummary(line.substring("SUMMARY;LANGUAGE=en-us:Norway: ".length()).trim());
                } else if (line.equals("END:VEVENT")) {
                    events.add(currentEvent);
                    currentEvent = null;
                }
            }
        }
    }

    public String get(LocalDate date) {
        return events.stream().filter((e) -> e.getDate().equals(date))
                .findFirst().orElse(new CalendarEvent()).getSummary();
    }

    public List<CalendarEvent> getEvents() {
        return events;
    }

}
