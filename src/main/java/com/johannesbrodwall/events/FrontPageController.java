package com.johannesbrodwall.events;

import org.json.JSONObject;

import com.johannesbrodwall.infrastructure.oauth.ClientUserSession;
import com.johannesbrodwall.infrastructure.web.JSONController;

import javax.servlet.http.HttpServletRequest;

public class FrontPageController extends JSONController {

    @Override
    protected JSONObject getJSON(HttpServletRequest req) {
        return new JSONObject()
            .put("username", ClientUserSession.getCurrent().getDisplayName());
    }

    protected void postJSON(JSONObject object) {
    }

}
