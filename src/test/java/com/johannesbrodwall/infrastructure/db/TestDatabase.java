package com.johannesbrodwall.infrastructure.db;

import org.flywaydb.core.Flyway;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.johannesbrodwall.infrastructure.AppConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDatabase {

    private static Database database;

    public synchronized static Database getInstance() {
        if (database == null) initDatabase();
        return database;
    }

    private static void initDatabase() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        AppConfiguration properties = new AppConfiguration("events-test");

        database = new Database();
        database.setUrl(properties.getProperty("database.url", "jdbc:postgresql://localhost:5432/events_test"));
        database.setUsername(properties.getProperty("datasource.username", "events_test"));
        database.setPassword(properties.getProperty("datasource.password", "events_test"));
        database.setDriverClassName(properties.getProperty("database.driverClassName", "org.postgresql.Driver"));

        Flyway flyway = new Flyway();
        flyway.setDataSource(database.getDataSource());
        int number = flyway.migrate();
        log.info("Completed " + number + " migrations");
    }

}
