package com.buffalo.thevoid.main;

import com.buffalo.thevoid.data.Config;
import com.buffalo.thevoid.entity.Player;
import com.buffalo.thevoid.equipment.Spell;
import com.buffalo.thevoid.equipment.SpellList;
import com.buffalo.thevoid.equipment.Weapon;
import com.buffalo.thevoid.equipment.WeaponList;
import com.buffalo.thevoid.gui.Mediator;
import com.buffalo.thevoid.io.InputQueue;
import com.buffalo.thevoid.io.TextHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Objects;
import java.util.Scanner;

// Package only class that handles creating a new player.
// Relies on fileIO to read the config file which is then loaded into the config class.
class NewGame
{
    // Get the first weapon regardless of whether it's been set to unlocked or not.
    private static final Weapon defaultWeapon = WeaponList.WeaponData.get(0).getItem1();
    private static final Spell defaultSpell = SpellList.SpellData.get(0).getItem1();

    public static Player newPlayer()
    {
        String name = "";
        //Scanner s = new Scanner(System.in);

        System.out.println("What is your name? ");
        Mediator.sendToLog(null, "What is your name?");

        do
        {
            name = InputQueue.dequeue();
            if(name == null)
                name = "";

            TextHandler.wait(100);
        }
        while(name.isEmpty());
        //name = s.nextLine();

        return new Player(name, Config.START_HEALTH, Config.START_MANA,
                Config.START_DEFENSE, Config.START_RESISTANCE,
                defaultWeapon, defaultSpell,
                Config.KILLS_TO_LEVEL_UP, Config.START_LEVEL);
    }

    public static Player loadPlayer()
    {
        // If any of these variables are not set, then the catch will be triggered.
        String name = "";
        int HP = 0, MP = 0, DEF = 0, RES = 0, Level = 0, Kills = 0, WeaponsUnlocked = 0, SpellsUnlocked = 0;

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
                    case "WeaponsUnlocked" -> WeaponsUnlocked = Integer.parseInt(split[1]);
                    case "SpellsUnlocked" -> SpellsUnlocked = Integer.parseInt(split[1]);
                }
            }

            // Unlock weapons
            for(int x = 0; x < WeaponsUnlocked; x++)
                WeaponList.unlockWeapon();

            // Unlock spells
            for(int x = 0; x < SpellsUnlocked; x++)
                SpellList.unlockSpell();

            return new Player(name, HP, MP, DEF, RES, Kills, Level, Config.KILLS_TO_LEVEL_UP, defaultWeapon, defaultSpell);
        }
        catch(Exception e)
        {
            System.out.println("Error loading player data. Check player.dat");
            Mediator.sendToLog(null, "Error loading player data. Check player.dat");
            return null;
        }
    }
}
