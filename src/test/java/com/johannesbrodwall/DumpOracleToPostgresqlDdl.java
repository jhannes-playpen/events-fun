package com.johannesbrodwall;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Experimental code to create DDL statements from Oracle for a similar
 * PostgreSQL structure
 */
public class DumpOracleToPostgresqlDdl {

    public void dump(Connection conn, String schema) throws SQLException {
        try (ResultSet rs = conn.getMetaData().getTables(null, schema, null, new String[] { "TABLE" })) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if (isDatabaseInternal(tableName)) continue;
                dumpColumns(conn, tableName);
            }
        }
    }

    private boolean isDatabaseInternal(String tableName) {
        return tableName.startsWith("DR$") || tableName.startsWith("MLOG$") || tableName.startsWith("RUPD$");
    }

    private void dumpColumns(Connection conn, String tableNamePattern) throws SQLException {
        try ( ResultSet columnRs = conn.getMetaData().getColumns(null, null, tableNamePattern, null) ) {
            String currentTableName = null;
            List<String> columnStatements = new ArrayList<>();
            while (columnRs.next()) {
                String tableName = columnRs.getString("TABLE_NAME");
                if (!(tableName.equals(currentTableName))) {
                    if (currentTableName != null) {
                        dumpTable(conn, currentTableName, columnStatements);
                        columnStatements.clear();
                    }
                    currentTableName = tableName;
                }
                columnStatements.add(createColumnStatement(columnRs));
            }
            dumpTable(conn, currentTableName, columnStatements);
        }
    }

    private void dumpPrimaryKeys(Connection conn, String tableName) throws SQLException {
        try ( ResultSet rs = conn.getMetaData().getPrimaryKeys(null, null, tableName) ) {
            String currentConstraint = null;
            List<String> columns = new ArrayList<String>();
            String statement = null;
            while (rs.next()) {
                String pkName = rs.getString("PK_NAME");
                if (!pkName.equals(currentConstraint) && currentConstraint != null) {
                    System.out.println(statement + "(" + String.join(",", columns) + ");");
                    columns.clear();
                }
                statement = "ALTER TABLE " + rs.getString("TABLE_NAME")
                        + " ADD CONSTRAINT " + pkName + " PRIMARY KEY ";
                currentConstraint = pkName;
                columns.add("\"" + rs.getString("COLUMN_NAME") + "\"");
            }
            if (statement == null) return;
            System.out.println(statement + "(" + String.join(", ", columns) + ");");
        }
    }

    private void dumpTable(Connection conn, String currentTableName, List<String> columnStatements)
            throws SQLException {
        System.out.println("CREATE TABLE " + currentTableName
                + " (" + String.join(", ", columnStatements) + ");");
        dumpPrimaryKeys(conn, currentTableName);
    }

    private String createColumnStatement(ResultSet rs) throws SQLException {
        String columnStatement = "\"" + rs.getString("COLUMN_NAME") + "\" ";
        columnStatement += convertColumnTypes(rs.getString("TYPE_NAME"));

        int digits = rs.getInt("DECIMAL_DIGITS");
        if (!rs.wasNull() && digits > 0) {
            columnStatement += "(" + digits + ")";
        }

        int size = rs.getInt("COLUMN_SIZE");
        if (!rs.wasNull() && size > 0) {
            columnStatement += "(" + size + ")";
        }

        columnStatement += (rs.getBoolean("NULLABLE") ? " NULL" : " NOT NULL");
        String defaultValue = rs.getString("COLUMN_DEF");
        if (defaultValue != null) {
            if ("SYSDATE".equalsIgnoreCase(defaultValue.trim())) defaultValue = "CURRENT_TIMESTAMP";
            columnStatement += " DEFAULT " + defaultValue;
        }
        return columnStatement;
    }

    private String convertColumnTypes(String columnType) {
        if (columnType.equals("VARCHAR2")) columnType = "VARCHAR";
        if (columnType.equals("NVARCHAR2")) columnType = "VARCHAR";
        if (columnType.equals("NUMBER")) columnType = "INTEGER";
        if (columnType.equals("LONG")) columnType = "BIGINT";
        if (columnType.equals("BLOB")) columnType = "BYTEA";
        if (columnType.equals("CLOB")) columnType = "TEXT";
        if (columnType.startsWith("INTERVAL ")) columnType = "INTEGER";
        return columnType;
    }

}
