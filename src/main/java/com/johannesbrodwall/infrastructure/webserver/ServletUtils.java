package com.johannesbrodwall.infrastructure.webserver;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServletUtils {

    public static <T> T getSessionObject(Class<T> sessionObjectClass, HttpServletRequest req) {
        @SuppressWarnings("unchecked")
        T sessionObject = (T) req.getSession().getAttribute(sessionObjectClass.getName());
        if (sessionObject == null) {
            log.info("Creating new " + sessionObjectClass + " for " + req.getRequestedSessionId());
            try {
                sessionObject = sessionObjectClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Ensure that there's a public no-args constructor on" +
                                           sessionObjectClass);
            }
            req.getSession().setAttribute(sessionObjectClass.getName(), sessionObject);
        }
        return sessionObject;
    }

    public static String getContextUrl(HttpServletRequest req) {
        return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()
                + req.getContextPath();
    }

    public static String getRequestUrl(HttpServletRequest req) {
        String result = getContextUrl(req);
        if (req.getPathInfo() != null) result += req.getPathInfo();
        if (req.getQueryString() != null) result += "?" + req.getQueryString();
        return result;
    }

}
