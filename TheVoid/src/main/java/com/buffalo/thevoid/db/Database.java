package com.buffalo.thevoid.db;

import com.buffalo.thevoid.data.Tuple;
import com.buffalo.thevoid.entity.Player;
import com.buffalo.thevoid.utils.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;

// Main database class.
public class Database extends AbstractDBManager
{
//    public static final String URL = "jdbc:derby:TheVoidDB;create=true";
//    public static final String USER = "";
//    public static final String PASS = "";

    public DBUtils dbUtils;

    public Database()
    {
        super("jdbc:derby:TheVoidDB;create=true", "", "");
        dbUtils = new DBUtils(this);
    }

    /**
     * Initializes the database for use with this game.
     *<p>
     *          REQUIREMENTS: <br>
     *          TABLE: player <br>
     *          COLUMNS: everything found in player.dat
     */
    public void setup()
    {
        dropIt("PLAYER");

        String table = "PLAYER";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        // iT'S HACKY BUT SETTING EVERYTHING TO VARCHAR MAKES LIFE EASIER
        map.put("NAME", "VARCHAR(255)"); // Primary key
        map.put("LEVEL", "VARCHAR(255)");
        map.put("KILLCOUNT", "VARCHAR(255)");
        map.put("HP", "VARCHAR(255)");
        map.put("MP", "VARCHAR(255)");
        map.put("DEF", "VARCHAR(255)");
        map.put("RES", "VARCHAR(255)");
        map.put("WEAPONCOUNT", "VARCHAR(255)");
        map.put("SPELLCOUNT", "VARCHAR(255)");

        // Generate the SQL
        String sql = DBUtils.tableBuilder(table, map);

        // Execute the SQL
        try
        {
            dbUtils.executeStatement(sql);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        // Event table
        dropIt("EVENT");

        table = "EVENT";
        map = new LinkedHashMap<>();

        map.put("ID", "INTEGER"); // Primary key
        map.put("EVENT", "VARCHAR(255)");
        map.put("TIME", "DATE");

        sql = DBUtils.tableBuilder(table, map);
        try
        {
            dbUtils.executeStatement(sql);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void dropIt(String table)
    {
        // Drop player table
        String sql = "DROP TABLE " + table.toUpperCase(Locale.ROOT);

        try(Connection conn = DriverManager.getConnection(url, user, password))
        {
            Statement s = conn.createStatement();
            s.execute(sql);
        }
        catch (SQLException e)
        {
            if(e.getSQLState().equals("42Y55"))
                System.out.println("Table does not exist");
        }
    }

    public void writePlayer(Player player)
    {
        // Convert player data to a map
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        Tuple<Integer, Integer> unlocked = Utilities.getWeaponCount();

        map.put("NAME", player.getName());
        map.put("LEVEL", String.valueOf(player.getLevel()));
        map.put("KILLCOUNT", String.valueOf(player.getKillCount()));
        map.put("HP", String.valueOf(player.getMaxHP()));
        map.put("MP", String.valueOf(player.getMaxMP()));
        map.put("DEF", String.valueOf(player.getDEF()));
        map.put("RES", String.valueOf(player.getRES()));
        map.put("WEAPONCOUNT", String.valueOf(unlocked.getItem1()));
        map.put("SPELLCOUNT", String.valueOf(unlocked.getItem2()));

        // Generate the SQL
        String sql = DBUtils.sqlInsertBuilder("PLAYER", map);

        // Check if the player already exists and switch to update if so
        if(dbUtils.doesRecordExist("PLAYER", "NAME", player.getName()))
            sql = DBUtils.sqlUpdateBuilder("PLAYER", map, "NAME = '" + player.getName() + "'");

        // Execute the SQL
        try
        {
            dbUtils.executeStatement(sql);
        }
        catch (SQLException e)
        {
            System.out.println("Error writing player to database.");
            e.printStackTrace();
        }
    }

    public Set<String> getAllPlayers()
    {
        return dbUtils.getAllPlayers();
    }

}
