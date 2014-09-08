package com.johannesbrodwall.infrastructure.webserver;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {

    void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException;

}
