package com.buffalo.thevoid.db;

import com.buffalo.thevoid.data.Tuple;
import com.buffalo.thevoid.entity.Player;
import com.buffalo.thevoid.exception.TableAlreadyExistsException;
import com.buffalo.thevoid.utils.Utilities;

import java.util.LinkedHashMap;

// Main database class.
public class Database extends AbstractDBManager
{
//    public static final String URL = "jdbc:derby:TheVoidDB;create=true";
//    public static final String USER = "";
//    public static final String PASS = "";

    private DBUtils dbUtils;

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
    public void setup() throws TableAlreadyExistsException
    {
        if(DBUtils.testTableExist("PLAYER", url, user, password))
            throw new TableAlreadyExistsException();

        String table = "PLAYER";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("NAME", "VARCHAR(255)"); // Primary key
        map.put("LEVEL", "INTEGER");
        map.put("KILLCOUNT", "INTEGER");
        map.put("HP", "INTEGER");
        map.put("MP", "INTEGER");
        map.put("DEF", "INTEGER");
        map.put("RES", "INTEGER");
        map.put("WEAPONCOUNT", "INTEGER");
        map.put("SPELLCOUNT", "INTEGER");

        // Generate the SQL
        String sql = DBUtils.tableBuilder(table, map);
    }

    public void writePlayer(Player player)
    {
        // Convert player data to a map
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        Tuple<Integer, Integer> unlocked = Utilities.getWeaponCount();

        map.put("NAME", player.getName());
        map.put("LEVEL", player.getLevel());
        map.put("KILLCOUNT", player.getKillCount());
        map.put("HP", player.getMaxHP());
        map.put("MP", player.getMaxMP());
        map.put("DEF", player.getDEF());
        map.put("RES", player.getRES());
        map.put("WEAPONCOUNT", unlocked.getItem1());
        map.put("SPELLCOUNT", unlocked.getItem2());

        // Generate the SQL
        String sql = DBUtils.sqlInsertBuilder("PLAYER", map);

        // Execute the SQL
        dbUtils.executeStatement(sql);
    }
}
