package com.buffalo.thevoid.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

// TODO: IMPORTANT!!! remove all n words

class SQLGeneratorTest
{
    private static Connection conn;
    private static DBUtils dbUtils;

    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        conn = DriverManager.getConnection("jdbc:derby:TheVoidDB;create=true", "", "");
        dbUtils = new DBUtils("jdbc:derby:TheVoidDB;create=true", "", "");
    }

    @Test
    void test1()
    {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("id", "INTEGER");
        map.put("nigga", "VARCHAR(255)");
        map.put("nigga1", "VARCHAR(255)");
        map.put("nigga2", "VARCHAR(255)");
        map.put("nigga3", "VARCHAR(255)");

        System.out.println(DBUtils.sqlInsertBuilder("test", map));
    }

    @Test
    void testTableCreation()
    {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("naem", "VARCHAR(255)");
        map.put("nigga", "VARCHAR(255)");
        map.put("nigga1", "VARCHAR(255)");
        map.put("nigga2", "VARCHAR(255)");
        map.put("nigga3", "VARCHAR(255)");

        System.out.println(DBUtils.tableBuilder("test2", map));

        try
        {
            Statement s = conn.createStatement();

            s.execute(DBUtils.tableBuilder("test2", map));
        }
        catch (SQLException e)
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

        HashMap<String, String> map2 = new HashMap<>();

        map2.put("naem", "asdf4");
        map2.put("nigga", "asdf");
        map2.put("nigga1", "asdf");
        map2.put("nigga2", "asdf");
        map2.put("nigga3", "asdf");

        String sql = DBUtils.sqlInsertBuilder("test2", map2);

        // Test whether the record exists.
        if(dbUtils.doesRecordExist("test2", "naem", "asdf4"))
        {
            System.out.println("Record exists.");

            LinkedHashMap<String, String> map3 = new LinkedHashMap<>();
            map3.put("naem", "asdf420");
            map3.put("nigga", "asdf2");
            map3.put("nigga1", "asdf3");
            map3.put("nigga2", "asdf4");
            map3.put("nigga3", "asdf5");

            sql = DBUtils.sqlUpdateBuilder("test2", map3, "naem = 'asdf4'");
        }
        else
        {
            System.out.println("Record does not exist.");
        }

        try
        {
            Statement s = conn.createStatement();
            System.out.println(sql);

            s.execute(sql);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void readAllTest()
    {
        System.out.println(DBUtils.sqlReadAllBuilder("test2"));
        ResultSet rs = null;

        try
        {
            Statement s = conn.createStatement();

            rs = s.executeQuery(DBUtils.sqlReadAllBuilder("test2"));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        try
        {
            while(rs.next())
            {
                System.out.println(rs.getString("naem") + " " + rs.getString("nigga"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void dropIt()
    {
        try
        {
            dbUtils.executeStatement("DROP TABLE test2");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void dateTest()
    {
        try
        {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("name", "VARCHAR(255)");
            map.put("date", "DATE");

            String sql = DBUtils.tableBuilder("dateTest", map);

            Statement s = conn.createStatement();

            s.execute(sql);
            System.out.println(sql);
            System.out.println("No errors in table creation");

            LinkedHashMap<String, Object> map2 = new LinkedHashMap<>();

            // This is how to insert a date into database
            Calendar cal = Calendar.getInstance();
            Date date = new Date(cal.getTimeInMillis());

            map2.put("name", "asdf");
            map2.put("date", date);

            sql = DBUtils.sqlInsertBuilder("dateTest", map2);
            s.execute(sql);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception
    {
        conn.close();
    }
}