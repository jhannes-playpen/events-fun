package com.johannesbrodwall.events;

import org.json.JSONObject;

import com.johannesbrodwall.infrastructure.webserver.JSONController;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class CategoryController extends JSONController implements PostController {

    @Override
    protected JSONObject handleRequest(HttpServletRequest req) {
        List<EventCategory> categories = new ArrayList<EventCategory>();
        for (int i = 0; i < 35; i++) {
            categories.add(SampleData.sampleCategory());
        }

        return new JSONObject()
            .put("categories", toJSONArray(categories));
    }

    @Override
    protected void handlePost(JSONObject object) {

    }


}
