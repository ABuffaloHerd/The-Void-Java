package com.buffalo.thevoid.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class DBSetupTest
{
    private static Connection conn;
    private static Statement s;

    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        conn = DriverManager.getConnection("jdbc:derby:TheVoidDB;create=true", "", "");
        s = conn.createStatement();
    }

    /**
     * Testing that the embedded database is created and that the tables do indeed exist.
     */
    @Test
    void test1()
    {
        try
        {
            Statement s = conn.createStatement();

            s.execute("CREATE TABLE test (id INTEGER PRIMARY KEY, name VARCHAR(255))");
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
    }

    @Test
    void testColumnUpdate()
    {
        try
        {
            s.execute("ALTER TABLE test ADD COLUMN test2 VARCHAR(255)");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void dropTable()
    {
        try
        {
            s.execute("DROP TABLE test");
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