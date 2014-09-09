package com.johannesbrodwall.infrastructure.web;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class JSONController implements GetController, PostController {

    @Override
    public final void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject response = getJSON(req);

        resp.setContentType("application/json");
        try (Writer writer = resp.getWriter()) {
            writer.write(response.toString());
        }
    }

    protected abstract JSONObject getJSON(HttpServletRequest req);

    protected JSONArray toJSONArray(List<? extends JSONConvertible> objects) {
        return new JSONArray(objects.stream().map(JSONConvertible::toJSON).toArray());
    }

    protected abstract void postJSON(JSONObject object);

    @Override
    public final void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Reader reader = req.getReader()) {
            JSONObject obj = new JSONObject(new JSONTokener(reader));
            postJSON(obj);
            resp.sendError(HttpServletResponse.SC_ACCEPTED);
        }
    }

}
