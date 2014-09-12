package com.johannesbrodwall.infrastructure.oauth;


import com.johannesbrodwall.infrastructure.web.ServletUtils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Oauth2CallbackServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ClientUserSession userSession = ServletUtils.getSessionObject(ClientUserSession.class, req);

        String redirectUri = ServletUtils.getContextUrl(req) + "/oauth2callback";
        userSession.fetchAuthToken(req.getParameter("code"), redirectUri);
        userSession.fetchProfile();
        resp.sendRedirect(req.getContextPath());
    }
}
