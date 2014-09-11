package com.johannesbrodwall.events.calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.johannesbrodwall.events.HolidayCalendar;
import com.johannesbrodwall.events.SampleEventData;
import com.johannesbrodwall.events.event.Event;
import com.johannesbrodwall.infrastructure.web.GetController;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CalendarController implements GetController {

    @Override
    public final void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject response = getJSON(req);

        resp.setContentType("application/json");
        try (Writer writer = resp.getWriter()) {
            writer.write(response.toString());
        }
    }

    protected JSONObject getJSON(HttpServletRequest req) throws IOException {
        LocalDate startDate = LocalDate.of(2014, 4, 28);
        LocalDate endDate = startDate.plusDays(20);
        List<Event> events = new ArrayList<Event>();
        for (int i = 0; i < 12; i++) {
            events.add(SampleEventData.sampleEvent(SampleEventData.sampleCategory(), startDate));
        }
        HolidayCalendar holidays = new HolidayCalendar(new URL("http://www.officeholidays.com/ics/ics_country.php?tbl_country=Norway"));
        return getJSON(startDate, endDate, events, holidays.getHolidays());
    }

    public JSONObject getJSON(LocalDate startDate, LocalDate endDate, List<Event> events, Map<LocalDate, String> holidays) {
        JSONArray jsonEvents = new JSONArray();
        for (Event event : events) {
            jsonEvents.put(getJSON(event, startDate, endDate, holidays));
        }
        return new JSONObject()
            .put("events", jsonEvents)
            .put("startDate", startDate)
            .put("endDate", endDate);
    }

    private JSONObject getJSON(Event event, LocalDate startDate, LocalDate endDate, Map<LocalDate, String> holidays) {
        JSONArray calendar = new JSONArray();
        LocalDate date = startDate;
        while ( date.isBefore(endDate) ) {
            if (holidays.containsKey(date)) {
                calendar.put(new JSONObject()
                    .put("color", "purple")
                    .put("title", holidays.get(date)));
            } else if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                calendar.put(new JSONObject().put("color", "gray"));
            } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                calendar.put(new JSONObject().put("color", "gray"));
            } else if (isWithinEvent(event, date)) {
                calendar.put(new JSONObject().put("color", event.getCategory().getColor()));
            } else {
                calendar.put(new JSONObject());
            }
            date = date.plusDays(1);
        }
        return new JSONObject()
            .put("displayName", event.getDisplayName())
            .put("calendar", calendar);
    }

    private boolean isWithinEvent(Event event, LocalDate date) {
        if (event.getStartDate().isAfter(date)) return false;
        if (event.getEndDate().isBefore(date)) return false;
        return true;
    }

}
