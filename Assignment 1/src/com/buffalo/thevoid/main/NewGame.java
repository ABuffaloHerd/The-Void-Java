package com.buffalo.thevoid.main;

import com.buffalo.thevoid.data.Config;
import com.buffalo.thevoid.entity.Player;
import com.buffalo.thevoid.equipment.Weapon;
import com.buffalo.thevoid.equipment.WeaponList;
import com.buffalo.thevoid.io.ConsoleColours;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.util.Scanner;

// Package only class that handles creating a new player.
// Relies on fileIO to read the config file which is then loaded into the config class.
class NewGame
{
    // Get the first item regardless of whether it's been set to unlocked or not.
    private static final Weapon defaultWeapon = WeaponList.WeaponData.get(0).getItem1();

    public static Player newPlayer()
    {
        String name;
        Scanner s = new Scanner(System.in);

        System.out.printf("%sWhat is your name?%s ", ConsoleColours.ANSI_BLACK_BACKGROUND, ConsoleColours.TEXT_RESET);
        name = s.nextLine();

//        This was funny the first 10 times and now it's just annoying.
//        System.out.printf("%sWhat is your quest?%s ", ConsoleColours.ANSI_BLACK_BACKGROUND, ConsoleColours.TEXT_RESET);
//        s.next();
//
//        System.out.printf("%sWhat is your favourite colour?%s ", ConsoleColours.ANSI_BLACK_BACKGROUND, ConsoleColours.TEXT_RESET);
//        s.next();
//
//        System.out.printf("%sWhat is the airspeed velocity of an unladen swallow?%s ", ConsoleColours.ANSI_BLACK_BACKGROUND, ConsoleColours.TEXT_RESET);
//        s.next();

        return new Player(name, Config.START_HEALTH, Config.START_MANA,
                Config.START_DEFENSE, Config.START_RESISTANCE,
                defaultWeapon,
                Config.KILLS_TO_LEVEL_UP, Config.START_LEVEL);
    }

    public static Player loadPlayer()
    {
        // If any of these variables are not set, then the catch will be triggered.
        String name = "";
        int HP = 0, MP = 0, DEF = 0, RES = 0, Level = 0, Kills = 0;

        try(BufferedReader br = new BufferedReader(new FileReader(Config.cwd + "/resources/save/player.dat")))
        {
            while(br.ready())
            {
                String line = br.readLine();
                String[] split = line.split("=");
                switch (split[0])
                {
                    case "Name" -> name = split[1];
                    case "HP" -> HP = Integer.parseInt(split[1]);
                    case "MP" -> MP = Integer.parseInt(split[1]);
                    case "DEF" -> DEF = Integer.parseInt(split[1]);
                    case "RES" -> RES = Integer.parseInt(split[1]);
                    case "Level" -> Level = Integer.parseInt(split[1]);
                    case "KillCount" -> Kills = Integer.parseInt(split[1]);
                }
            }

            return new Player(name, HP, MP, DEF, RES, defaultWeapon, Kills, Level);
        }
        catch(Exception e)
        {
            System.out.println("Error loading player data.");
            return null;
        }
    }
}
