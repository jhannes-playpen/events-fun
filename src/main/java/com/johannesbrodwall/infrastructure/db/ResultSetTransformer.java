package com.johannesbrodwall.infrastructure.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetTransformer<T> {

    T apply(ResultSet resultSet) throws SQLException;

}
