package com.johannesbrodwall.infrastructure.db;

import org.flywaydb.core.Flyway;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.johannesbrodwall.infrastructure.AppConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDatabase {

    private TestDatabase() {
    }

    private static Database database;

    public synchronized static Database getInstance() {
        if (database == null) initDatabase();
        return database;
    }

    private static void initDatabase() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        AppConfiguration configuration = new AppConfiguration("events-test");
        configuration.setDefault("events.db.url", "jdbc:postgresql://localhost:5432/events_test");
        configuration.setDefault("events.db.username", "events_test");
        configuration.setDefault("events.db.password", "events_test");
        configuration.setDefault("events.db.driverClassName", "org.postgresql.Driver");

        database = new Database(configuration);

        Flyway flyway = new Flyway();
        flyway.setDataSource(database.getDataSource());
        flyway.clean();
        int number = flyway.migrate();
        log.info("Completed " + number + " migrations");
    }

}
