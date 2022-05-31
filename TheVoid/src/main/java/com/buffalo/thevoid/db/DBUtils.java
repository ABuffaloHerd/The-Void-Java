package com.buffalo.thevoid.db;

import java.sql.*;
import java.util.*;

public class DBUtils extends AbstractDBManager
{
    /**
     * Testing that the embedded database is created and that the tables do indeed exist.
     *
     * @param url The URL of the database.
     * @param user The username of the database.
     * @param pass The password of the database.
     */
    public DBUtils(String url, String user, String pass)
    {
        super(url, user, pass);

        try
        {
            // Test connection
            Connection conn = DriverManager.getConnection(url, user, pass);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Failed to connect to database.");
        }
    }

    /**
     * Clone another abstract database manager's fields.
     * @param manager Clone target.
     */
    public DBUtils(AbstractDBManager manager)
    {
        this(manager.getUrl(), manager.getUser(), manager.getPassword());
    }

    /**
     * Builds an sql insert statement for the table provided. Uses a map to sync the column name with its value.
     * @param table Target table.
     * @param values A hashmap of column names and values.
     * @return An sql statement.
     */
    public static String sqlInsertBuilder(String table, HashMap<?, ?> values)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(table);
        sb.append(" (");

        for(var key : values.keySet())
        {
            sb.append(key);
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length()); // Remove last comma and space.
        sb.append(") VALUES (");

        for(var value : values.values())
        {
            sb.append("'");
            sb.append(value);
            sb.append("', ");
        }

        sb.delete(sb.length() - 2, sb.length()); // Remove last comma and space.
        sb.append(")");

        return sb.toString();
    }

    /**
     * Build an sql table creation statement. Uses a map to sync the column name with its type.
     * I love making reusable methods that are only used once.
     * @param tablename Target table.
     * @param columnType A hashmap of column names and types. Must be linked hashmap to preserve order because the first element is the primary key.
     * @return An sql statement.
     */
    public static String tableBuilder(String tablename, LinkedHashMap<String, String> columnType)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE ");
        sb.append(tablename);

        sb.append(" (");

        boolean isFirst = true;
        for(var key: columnType.keySet())
        {
            if(isFirst)
            {
                sb.append(key);
                sb.append(" ");
                sb.append(columnType.get(key));
                sb.append(" PRIMARY KEY");
                isFirst = false;
            }
            else
            {
                sb.append(", ");
                sb.append(key);
                sb.append(" ");
                sb.append(columnType.get(key));
            }
        }

        sb.append(")");

        return sb.toString();
    }

    // Builds an sql update statement.
    public static String sqlUpdateBuilder(String table, HashMap<?, ?> values, String where)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(table);
        sb.append(" SET ");

        for(var key : values.keySet())
        {
            sb.append(key);
            sb.append(" = '");
            sb.append(values.get(key));
            sb.append("', ");
        }

        sb.delete(sb.length() - 2, sb.length()); // Remove last comma and space.
        sb.append(" WHERE ");
        sb.append(where);

        return sb.toString();
    }

    /**
     * Builds a simple select all statement.
     * @param table Target table.
     * @return An sql statement.
     */
    public static String sqlReadAllBuilder(String table)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(table);
        return sb.toString();
    }

    // For use in try catch blocks.
    private void testTableExist(SQLException e)
    {
        if(e.getSQLState().equals("X0Y32"))
        {
            System.out.println("Table already exists.");
            System.out.println("Continuing...");
        }
        else
        {
            e.printStackTrace();
            System.out.println("Failed to create statement.");
        }
    }

    /**
     * Attempts to create a table with given name and dummy columns.
     * Exception is thrown if table already exists, and is used to return boolean.
     * @param name The name of the table to create.
     * @param url The URL of the database.
     * @param user The username of the database.
     * @param password The password of the database.
     * @return True if table was created, false if table already exists.
     */
    public static boolean testTableExist(String name, String url, String user, String password)
    {
        try(Connection c = DriverManager.getConnection(url, user, password))
        {
            Statement s = c.createStatement();
            s.execute("CREATE TABLE " + name.toUpperCase(Locale.ROOT) + " (id INTEGER PRIMARY KEY, name VARCHAR(255))");
        }
        catch (SQLException e)
        {
            return false;
        }

        // If we get here, the table was created. That means it doesn't exist. Drop it.
        try(Connection c = DriverManager.getConnection(url, user, password))
        {
            Statement s = c.createStatement();
            s.execute("DROP TABLE " + name.toUpperCase(Locale.ROOT));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Executes an sql statement. Target database is the one used in the constructor.
     * @param sql your sql statement. I hope you know what you're doing.
     */
    public void executeStatement(String sql) throws SQLException
    {
        Connection conn = DriverManager.getConnection(url, user, password);
        Statement s = conn.createStatement();
        s.execute(sql);

        conn.close();
    }

    public boolean doesRecordExist(String table, String column, String value)
    {
        try(Connection conn = DriverManager.getConnection(url, user, password))
        {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM " + table + " WHERE " + column + " = '" + value + "'");
            return rs.next();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    // package private
    Set<String> getAllPlayers()
    {
        // Get all records from the player table and ram into a set
        LinkedHashSet<String> set = new LinkedHashSet<>();
        StringBuilder sb = new StringBuilder();
        String sql = sqlReadAllBuilder("PLAYER");
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;

        try(Connection c = DriverManager.getConnection(url, user, password))
        {
            rs = c.createStatement().executeQuery(sql);
            rsmd = rs.getMetaData();

            if(!rs.next()) // No players exist
            {
                set.add("No players exist.");
                return set;
            }

            // Build string
            for(int i = 1; i <= rsmd.getColumnCount(); i++)
            {
                sb.append(rsmd.getColumnName(i)).append(": ").append(rs.getString(i));
                set.add(sb.append("\n").toString());
                sb.setLength(0); // reset string builder
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return set;
    }
}
