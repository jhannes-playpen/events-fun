package com.johannesbrodwall.events;

import com.johannesbrodwall.infrastructure.webserver.WebServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationServer extends WebServer {

    public ApplicationServer(int port) {
        super(port);
    }

    public static void main(String[] args) throws Exception {
        ApplicationServer server = new ApplicationServer(10080);
        server.addHandler(server.shutdownHandler());
        server.addHandler(server.createWebAppContext("/events"));
        server.addHandler(server.createRedirectContextHandler("/", "/events"));
        server.start();

        log.info("Started " + server.getURI());
    }


}
