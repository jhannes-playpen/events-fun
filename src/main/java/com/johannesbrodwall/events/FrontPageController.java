package com.johannesbrodwall.events;

import org.json.JSONObject;

import com.johannesbrodwall.infrastructure.webserver.JSONController;

import javax.servlet.http.HttpServletRequest;

public class FrontPageController extends JSONController {

    @Override
    protected JSONObject handleRequest(HttpServletRequest req) {
        return new JSONObject()
            .put("username", ClientUserSession.getCurrent().getDisplayName());
    }

    protected void handlePost(JSONObject object) {
    }

}
