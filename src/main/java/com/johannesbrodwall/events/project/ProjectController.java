package com.johannesbrodwall.events.project;

import org.json.JSONObject;

import com.johannesbrodwall.infrastructure.web.JSONController;

import javax.servlet.http.HttpServletRequest;

public class ProjectController extends JSONController {

    private ProjectRepository repository = new ProjectRepository();

    @Override
    protected JSONObject getJSON(HttpServletRequest req) {
        return new JSONObject()
            .put("projects", toJSONArray(repository.findAll()));
    }

    @Override
    protected void postJSON(JSONObject object) {
        repository.insert(Project.fromJSON(object));

    }


}
