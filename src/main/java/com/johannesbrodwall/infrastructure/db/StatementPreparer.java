package com.johannesbrodwall.infrastructure.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementPreparer {

    void prepare(PreparedStatement statement) throws SQLException;

}
