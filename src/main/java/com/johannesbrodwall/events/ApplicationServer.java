package com.johannesbrodwall.events;

import org.flywaydb.core.Flyway;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.johannesbrodwall.infrastructure.AppConfiguration;
import com.johannesbrodwall.infrastructure.db.Database;
import com.johannesbrodwall.infrastructure.webserver.WebServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationServer extends WebServer {

    public ApplicationServer(int port) {
        super(port);
    }

    @Override
    public void start() throws Exception {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        Database database = new Database(new AppConfiguration("events.properties"));
        Flyway flyway = new Flyway();
        flyway.setDataSource(database.getDataSource());
        flyway.migrate();

        addHandler(shutdownHandler());
        addHandler(createWebAppContext("/events"));
        addHandler(createRedirectContextHandler("/", "/events"));
        super.start();
    }

    public static void main(String[] args) throws Exception {
        int port = 10080;
        if (System.getenv("PORT") != null) {
            port = Integer.parseInt(System.getenv("PORT"));
        }

        ApplicationServer server = new ApplicationServer(port);
        server.start();

        log.info("Started " + server.getURI());
    }

}
