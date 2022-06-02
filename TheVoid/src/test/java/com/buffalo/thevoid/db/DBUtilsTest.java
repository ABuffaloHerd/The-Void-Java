/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.buffalo.thevoid.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author aclh2
 */
public class DBUtilsTest 
{
    DBUtils dbUtils;
    Connection conn;
    
    public DBUtilsTest() 
    {

    }
    
    @Before
    public void init()
    {
        dbUtils = new DBUtils("jdbc:derby:TEST;create=true", "", "");
        try
        {
            conn = DriverManager.getConnection("jdbc:derby:TEST;create=true", "", "");
        }
        catch(Exception stfu) { }
    }

    @Test
    public void testTableSQL() 
    {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("naem", "VARCHAR(255)");
        map.put("qwerty", "VARCHAR(255)");
        map.put("qwerty1", "VARCHAR(255)");
        map.put("qwerty2", "VARCHAR(255)");
        map.put("qwerty3", "VARCHAR(255)");

        String sql = DBUtils.tableBuilder("test2", map);
        
        System.out.println(sql);
        
        assertEquals("CREATE TABLE test2 (naem VARCHAR(255) PRIMARY KEY, qwerty VARCHAR(255), qwerty1 VARCHAR(255), qwerty2 VARCHAR(255), qwerty3 VARCHAR(255))", sql);
    }
    
    @Test
    public void testInsertSQL()
    {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("id", "INTEGER");
        map.put("qwerty", "VARCHAR(255)");
        map.put("qwerty1", "VARCHAR(255)");
        map.put("qwerty2", "VARCHAR(255)");
        map.put("qwerty3", "VARCHAR(255)");

        System.out.println(DBUtils.sqlInsertBuilder("test", map));
    }
    
    @Test
    public void testTableCreation()
    {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("naem", "VARCHAR(255)");
        map.put("qwerty", "VARCHAR(255)");
        map.put("qwerty1", "VARCHAR(255)");
        map.put("qwerty2", "VARCHAR(255)");
        map.put("qwerty3", "VARCHAR(255)");

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
        map2.put("qwerty", "asdf");
        map2.put("qwerty1", "asdf");
        map2.put("qwerty2", "asdf");
        map2.put("qwerty3", "asdf");

        String sql = DBUtils.sqlInsertBuilder("test2", map2);

        // Test whether the record exists.
        if(dbUtils.doesRecordExist("test2", "naem", "asdf4"))
        {
            System.out.println("Record exists.");

            LinkedHashMap<String, String> map3 = new LinkedHashMap<>();
            map3.put("naem", "asdf420");
            map3.put("qwerty", "asdf2");
            map3.put("qwerty1", "asdf3");
            map3.put("qwerty2", "asdf4");
            map3.put("qwerty3", "asdf5");

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
    public void readAllTest()
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
                System.out.println(rs.getString("naem") + " " + rs.getString("qwerty"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    @Test
    public void dateTest()
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
}
