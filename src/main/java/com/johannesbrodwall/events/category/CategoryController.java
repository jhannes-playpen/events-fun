package com.johannesbrodwall.events.category;

import org.json.JSONObject;

import com.johannesbrodwall.events.SampleData;
import com.johannesbrodwall.infrastructure.web.JSONController;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class CategoryController extends JSONController {

    private CategoryRepository repository = new CategoryRepository();

    @Override
    public JSONObject getJSON(HttpServletRequest req) {
        List<EventCategory> categories = new ArrayList<EventCategory>();
        for (int i = 0; i < 10; i++) {
            categories.add(SampleData.sampleCategory());
        }
        return new JSONObject()
            .put("categories", toJSONArray(categories));
    }

    @Override
    public void postJSON(JSONObject object) {
        repository.insert(EventCategory.fromJSON(object));
    }


}
