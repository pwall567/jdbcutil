/*
 * @(#) InsertStatement.java
 */

package net.pwall.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Default comment for {@code InsertStatement}.
 *
 * @author Peter Wall
 */
public class InsertStatement {

    private Connection connection;
    private String tablename;
    private List<Column> columns;

    public InsertStatement(Connection connection, String tablename) {
        this.connection = connection;
        this.tablename = tablename;
        columns = new ArrayList<>();
    }

    public void addStringColumn(String columnName, String value) {
        columns.add(new StringColumn(columnName, value));
    }

    public void addIntegerColumn(String columnName, int value) {
        columns.add(new IntegerColumn(columnName, value));
    }

    public void addParameterColumn(String columnName) {
        columns.add(new ParameterColumn(columnName));
    }

    public void execute() throws SQLException {
        PreparedStatement ps = connection.prepareStatement(toSQL());
        // now what?
    }

    public String toSQL() {
        int numCols = columns.size();
        if (numCols == 0)
            throw new IllegalStateException("No columns");
        StringBuilder sb = new StringBuilder("insert into ");
        sb.append(tablename);
        sb.append(" (");
        sb.append(columns.get(0).getColumnName());
        for (int i = 1; i < numCols; i++) {
            sb.append(", ");
            sb.append(columns.get(i).getColumnName());
        }
        sb.append(") values (");
        sb.append(columns.get(0).getColumnEntry());
        for (int i = 1; i < numCols; i++) {
            sb.append(", ");
            sb.append(columns.get(i).getColumnEntry());
        }
        sb.append(')');
        return sb.toString();
    }

    public static abstract class Column {

        private String columnName;

        public Column(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName() {
            return columnName;
        }

        public abstract String getColumnEntry();

    }

    public static abstract class ValueColumn extends Column {

        public ValueColumn(String columnName) {
            super(columnName);
        }

        public abstract boolean isNullValue();

    }

    public static class StringColumn extends ValueColumn {

        private String value;

        public StringColumn(String columnName, String value) {
            super(columnName);
            this.value = value;
        }

        @Override
        public boolean isNullValue() {
            return value == null;
        }

        @Override
        public String getColumnEntry() {
            if (value == null)
                return "null";
            StringBuilder sb = new StringBuilder();
            sb.append('\'');
            for (int i = 0, n = value.length(); i < n; i++) {
                char ch = value.charAt(i);
                if (ch == '\'')
                    sb.append('\'');
                sb.append(ch);
            }
            sb.append('\'');
            return sb.toString();
        }

    }

    public static class IntegerColumn extends ValueColumn {

        private int value;
        private boolean nullValue;

        public IntegerColumn(String columnName, int value) {
            super(columnName);
            this.value = value;
            nullValue = false;
        }

        public IntegerColumn(String columnName, Integer value) {
            super(columnName);
            nullValue = value == null;
            this.value = value == null ? 0 : value.intValue();
        }

        @Override
        public boolean isNullValue() {
            return nullValue;
        }

        @Override
        public String getColumnEntry() {
            return nullValue ? "null" : String.valueOf(value);
        }

    }

    public static class ParameterColumn extends Column {

        public ParameterColumn(String columnName) {
            super(columnName);
        }

        @Override
        public String getColumnEntry() {
            return "?";
        }

    }

}
