package com.johannesbrodwall.events.category;

import org.json.JSONObject;

import com.johannesbrodwall.infrastructure.web.JSONController;

import javax.servlet.http.HttpServletRequest;

public class CategoryController extends JSONController {

    private CategoryRepository repository = new CategoryRepository();

    @Override
    public JSONObject getJSON(HttpServletRequest req) {
        return new JSONObject()
            .put("categories", toJSONArray(repository.findAll()));
    }

    @Override
    public void postJSON(JSONObject object) {
        repository.insert(EventCategory.fromJSON(object));
    }


}
