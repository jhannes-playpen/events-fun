package com.johannesbrodwall.events;

import com.johannesbrodwall.events.calendar.CalendarController;
import com.johannesbrodwall.events.category.CategoryController;
import com.johannesbrodwall.events.event.EventController;
import com.johannesbrodwall.events.project.ProjectController;
import com.johannesbrodwall.infrastructure.AppConfiguration;
import com.johannesbrodwall.infrastructure.db.Database;
import com.johannesbrodwall.infrastructure.db.Transaction;
import com.johannesbrodwall.infrastructure.web.GetController;
import com.johannesbrodwall.infrastructure.web.PostController;
import com.johannesbrodwall.infrastructure.web.ServletUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiFrontServlet extends HttpServlet {

    private Database database = new Database(new AppConfiguration("events.properties"));

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ClientUserSession session = ServletUtils.getSessionObject(ClientUserSession.class, req);
        if (!session.isLoggedIn()) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        try (Transaction tx = database.transaction()) {
            ClientUserSession.setCurrent(session);
            super.service(req, resp);
            tx.setCommit();
        } finally {
            ClientUserSession.setCurrent(null);
        }

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        GetController controller = getControllers().get(req.getPathInfo());
        if (controller != null) {
            controller.doGet(req, resp);
        } else {
            super.doGet(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
    IOException {
        PostController controller = postControllers().get(req.getPathInfo());
        if (controller != null) {
            controller.doPost(req, resp);
        } else {
            super.doPost(req, resp);
        }
    }


    private Map<String,GetController> getControllers() {
        Map<String, GetController> controllers = new HashMap<>();

        controllers.put("/frontPage", new FrontPageController());
        controllers.put("/calendar", new CalendarController());
        controllers.put("/categories", new CategoryController());
        controllers.put("/projects", new ProjectController());
        controllers.put("/events", new EventController());

        return controllers;
    }


    private Map<String,PostController> postControllers() {
        Map<String, PostController> controllers = new HashMap<>();

        controllers.put("/categories", new CategoryController());
        controllers.put("/projects", new ProjectController());
        controllers.put("/events", new EventController());

        return controllers;
    }


}
