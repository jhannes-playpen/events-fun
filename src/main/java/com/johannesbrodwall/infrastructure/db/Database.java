package com.johannesbrodwall.infrastructure.db;

import com.johannesbrodwall.infrastructure.AppConfiguration;
import com.mchange.v2.c3p0.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class Database {

    public Database(AppConfiguration configuration) {
        setUrl(configuration.getRequiredProperty("events.db.url"));
        setUsername(configuration.getRequiredProperty("events.db.username"));
        setPassword(configuration.getRequiredProperty("events.db.password"));
        setDriverClassName(configuration.getRequiredProperty("events.db.driverClassName"));
    }

    public Database() {
    }

    private static class DbTransaction implements Transaction {
        private boolean rollback = true;

        @Override
        public void close() {
            Connection connection = currentConnection.get();
            try {
                if (rollback) {
                    connection.rollback();
                } else {
                    connection.commit();
                }
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            currentConnection.set(null);
        }

        @Override
        public void setCommit() {
            rollback = false;
        }
    }

    public Transaction transaction() {
        try {
            currentConnection.set(dataSource.getConnection());
            getCurrentConnection().setAutoCommit(false);
            return new DbTransaction();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private DriverManagerDataSource dataSource = new DriverManagerDataSource();
    private static ThreadLocal<Connection> currentConnection = new ThreadLocal<Connection>();

    private static Connection getCurrentConnection() {
        return currentConnection.get();
    }

    public void setUrl(String url) {
        dataSource.setJdbcUrl(url);
    }

    public void setUsername(String username) {
        dataSource.setUser(username);
    }

    public void setPassword(String password) {
        dataSource.setPassword(password);
    }

    public void setDriverClassName(String driverClassName) {
        dataSource.setDriverClass(driverClassName);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public static <T> List<T> executeQueryForAll(String query, ResultSetTransformer<T> transformer) {
        try (Statement statement = getCurrentConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<T> result = new ArrayList<T>();
            while (resultSet.next()) {
                result.add(transformer.apply(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeInsert(String query, StatementPreparer preparer) {
        try (PreparedStatement statement = getCurrentConnection().prepareStatement(query)) {
            preparer.prepare(statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T executePreparedQuery(String query, StatementPreparer preparer, ResultSetTransformer<T> transformer) {
        try (PreparedStatement statement = getCurrentConnection().prepareStatement(query)) {
            preparer.prepare(statement);

            try(ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return transformer.apply(rs);
                }
                throw new NotFoundException();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static long queryForLong(String query) {
        try (Statement statement = getCurrentConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            throw new NotFoundException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
