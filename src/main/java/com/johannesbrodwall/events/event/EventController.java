package com.johannesbrodwall.events.event;

import org.json.JSONObject;

import com.johannesbrodwall.infrastructure.web.JSONController;

import javax.servlet.http.HttpServletRequest;

public class EventController extends JSONController {

    private EventRepository repository = new EventRepository();

    @Override
    protected JSONObject getJSON(HttpServletRequest req) {
        return new JSONObject()
            .put("events", toJSONArray(repository.findAll()));
    }

    @Override
    protected void postJSON(JSONObject object) {
        repository.insert(Event.fromJSON(object));
    }


}
