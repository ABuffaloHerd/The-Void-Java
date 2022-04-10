package com.buffalo.thevoid.data;

import com.buffalo.thevoid.io.FileHandler;

/**
 * Configuration class for the application.
 * Provides the values loaded by FileHandler.readConfig() as public static final variables.
 * I don't think the class needs to be instantiated like the other lists.
 */
public class Config
{
    public static final int DIFFICULTY;
    public static final int START_HEALTH;
    public static final int START_MANA;
    public static final int START_LEVEL;
    public static final int KILLS_TO_LEVEL_UP;
    public static final int START_DEFENSE;
    public static final int START_RESISTANCE;
    public static final String cwd = System.getProperty("user.dir"); // Current working directory

    static
    {
        DIFFICULTY = FileHandler.readConfig("Difficulty");
        START_HEALTH = FileHandler.readConfig("StartHealth");
        START_MANA = FileHandler.readConfig("StartMana");
        START_LEVEL = FileHandler.readConfig("StartLevel");
        KILLS_TO_LEVEL_UP = FileHandler.readConfig("KillsToLevelUp");
        START_DEFENSE = FileHandler.readConfig("StartDefense");
        START_RESISTANCE = FileHandler.readConfig("StartResistance");
    }
}
