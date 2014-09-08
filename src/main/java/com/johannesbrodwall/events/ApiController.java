package com.johannesbrodwall.events;

import com.johannesbrodwall.infrastructure.webserver.Controller;
import com.johannesbrodwall.infrastructure.webserver.ServletUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ClientUserSession session = ServletUtils.getSessionObject(ClientUserSession.class, req);
        if (!session.isLoggedIn()) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        try {
            ClientUserSession.setCurrent(session);
            super.service(req, resp);
        } finally {
            ClientUserSession.setCurrent(null);
        }

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        Controller controller = getControllers().get(req.getPathInfo());
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
            super.doGet(req, resp);
        }
    }


    private Map<String,Controller> getControllers() {
        Map<String, Controller> controllers = new HashMap<>();

        controllers.put("/frontPage", new FrontPageController());
        controllers.put("/categories", new CategoryController());

        return controllers;
    }


    private Map<String,PostController> postControllers() {
        Map<String, PostController> controllers = new HashMap<>();

        controllers.put("/categories", new CategoryController());

        return controllers;
    }


}
