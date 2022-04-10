package com.buffalo.thevoid.factory;

import com.buffalo.thevoid.data.Tuple3;
import com.buffalo.thevoid.entity.Boss;
import com.buffalo.thevoid.entity.DamageType;
import com.buffalo.thevoid.entity.ExBoss;
import com.buffalo.thevoid.event.IEventHandler;
import com.buffalo.thevoid.event.IEventPublisher;
import com.buffalo.thevoid.io.ConsoleColours;
import com.buffalo.thevoid.io.FileHandler;
import com.buffalo.thevoid.io.MasterEventHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * This MUST be initialized with new BossList() before any references to it are made. Otherwise all objects in it are null.
 */
public class BossList implements IEventPublisher
{
    // An arraylist of bosses
    // The tuple contains:
    // item1 : boss object
    // item2 : isDefeated
    // item3 : levelRequired
    // In order that they should be fought in

    private static Boss Skeletron, Mariah, RoboGlasse, CrackerBrothers, JerryChen;
    private static final Set<IEventHandler<String>> eventHandlers = new HashSet<>();
    public static List<Tuple3<Boss, Boolean, Integer>> BossData = new ArrayList<>();
    public static ExBoss Hirina;

    // Initializer
    {
        System.out.println("Initializing BossList...");
        try
        {
            // Declare bosses
            Skeletron = new Boss("Skeletron 1000", FileHandler.loadBossQuotes("skeletron"), 1000, 50, 10,
                    IEntity -> IEntity.takeDamage(60, DamageType.PHYSICAL),
                    IEntity ->
                    {
                        int pain = 0;
                        raise(ConsoleColours.TEXT_YELLOW + "Skeletron 1K: ACTIVATING ROASTERINATOR 20000" + ConsoleColours.TEXT_RESET);

                        pain += IEntity.takeDamage(50, DamageType.MAGIC);
                        pain += IEntity.takeDamage(50, DamageType.MAGIC);

                        return pain;
                    });

            Mariah = new Boss("Mariah", FileHandler.loadBossQuotes("mariah"), 5000, 100, 20,
                    IEntity -> IEntity.takeDamage(100, DamageType.PHYSICAL),
                    IEntity ->
                    {
                        int yeowch = 0;
                        raise(ConsoleColours.TEXT_YELLOW + "Mariah: \"Consider this your eviction notice.\"\n" +
                                "Mariah: Nandaka - [Ancient History]" + ConsoleColours.TEXT_RESET);

                        yeowch += IEntity.takeDamage(240, DamageType.MAGIC);
                        yeowch += IEntity.takeDamage(240, DamageType.PHYSICAL);

                        return yeowch;
                    });

            RoboGlasse = new Boss("Robo-Glasse", FileHandler.loadBossQuotes("robog"), 8650, 140, 25,
                    IEntity -> IEntity.takeDamage(200, DamageType.MAGIC),
                    IEntity ->
                    {
                        int oof = 0;
                        raise(ConsoleColours.TEXT_YELLOW + "Robo-Glasse: \"Right, this isn't my problem anymore. Get out.\"" + ConsoleColours.TEXT_RESET);

                        oof += IEntity.takeDamage(300, DamageType.PURE);
                        for(int x = 0; x < 5; x++)
                        {
                            oof += IEntity.takeDamage(50, DamageType.PHYSICAL);
                        }

                        return oof;
                    });

            CrackerBrothers = new Boss("The Cracker Brothers", FileHandler.loadBossQuotes("crackers"), 10500, 200, 30,
                    IEntity ->
                    {
                        int yeesh = 0;
                        raise(ConsoleColours.TEXT_PURPLE + "Cracker Brothers: \"Cracker Brothers' twin attack!\"" + ConsoleColours.TEXT_RESET);
                        yeesh += IEntity.takeDamage(420, DamageType.PHYSICAL);
                        yeesh += IEntity.takeDamage(420, DamageType.MAGIC);

                        return yeesh;
                    },
                    IEntity ->
                    {
                        int bigPain = 0;

                        System.out.printf("Cracker Brothers' Signature Move: %s L E A N   B E A M %s\n", ConsoleColours.TEXT_PURPLE, ConsoleColours.TEXT_RESET);

                        bigPain += IEntity.takeDamage(650, DamageType.PHYSICAL);
                        bigPain += IEntity.takeDamage(300, DamageType.MAGIC);
                        bigPain += IEntity.takeDamage(200, DamageType.PURE);

                        return bigPain;
                    });

            JerryChen = new Boss("Real Estate Agent Jerry Chen", FileHandler.loadBossQuotes("jerry"), 15000, 370, 40,
                    IEntity ->
                    {
                        int owMyGut = 0;
                        raise(String.format("%s Jerry Chen: \"PROPERTY DAMAGE\" %s\n", ConsoleColours.ANSI_BLUE_BACKGROUND, ConsoleColours.TEXT_RESET));
                        owMyGut += IEntity.takeDamage(690, DamageType.PHYSICAL);
                        owMyGut += IEntity.takeDamage(400, DamageType.MAGIC);

                        return owMyGut;
                    },
                    IEntity ->
                    {
                        int prolapse = 0;
                        raise(String.format("%s Jerry Chen: Fiery Fist O Pain - [Intestine Exploding Punch] %s\n", ConsoleColours.TEXT_CYAN, ConsoleColours.TEXT_RESET));

                        prolapse += IEntity.takeDamage(999, DamageType.PURE);
                        prolapse += IEntity.takeDamage(999, DamageType.PHYSICAL);

                        return prolapse;
                    });

            Hirina = new ExBoss("Hirina", FileHandler.loadBossQuotes("firequeen"), 50000, 600, 80,
                    IEntity -> IEntity.takeDamage(800, DamageType.PHYSICAL),
                    IEntity ->
                    {
                        raise(String.format("%s Hirina: ... %s\n", ConsoleColours.TEXT_RED, ConsoleColours.TEXT_RESET));
                        raise(String.format("%s Hirina: Excalibur - [Groundbreaker] %s\n", ConsoleColours.TEXT_RED, ConsoleColours.TEXT_RESET));

                        return IEntity.takeDamage(1000, DamageType.MAGIC);
                    },
                    IEntity ->
                    {
                        int extremePain = 0;
                        raise(String.format("%s Hirina: Chun Jun - [One Thousand Slashes] %s\n", ConsoleColours.TEXT_RED, ConsoleColours.TEXT_RESET));

                        for(int x = 0; x < 1000; x++)
                        {
                            extremePain += IEntity.takeDamage(1, DamageType.PURE);
                        }

                        return extremePain;
                    },
                    IEntity ->
                    {
                        int manyPain = 0;
                        raise(String.format("%s Hirina: Behold, the light of a thousand angry sun gods! %s\n", ConsoleColours.TEXT_RED, ConsoleColours.TEXT_RESET));
                        raise(String.format("%s Hirina: Kusanagi - [Unsheathe] %s\n", ConsoleColours.TEXT_RED, ConsoleColours.TEXT_RESET));

                        for(int x = 0; x < 3; x++)
                        {
                            manyPain += IEntity.takeDamage(600, DamageType.MAGIC);
                        }

                        manyPain += IEntity.takeDamage(2000, DamageType.PURE);

                        return manyPain;
                    });
        }
        catch (IOException ignored)
        {
            System.out.println("Cannot find a boss file. Please check files, and run again. Self Destruct activating.");

            // Bad coding
            System.exit(-1);
        }

        // Add them to the public list
        BossData.add(new Tuple3<>(Skeletron, false, 2));
        BossData.add(new Tuple3<>(Mariah, false, 5));
        BossData.add(new Tuple3<>(RoboGlasse, false, 7));
        BossData.add(new Tuple3<>(CrackerBrothers, false, 10));
        BossData.add(new Tuple3<>(JerryChen, false, 12));

        // Register the master event handler
        addEventHandler(MasterEventHandler.masterEventHandler);
    }

    @Override
    public void addEventHandler(IEventHandler<?> e)
    {
        eventHandlers.add((IEventHandler<String>) e);
    }

    @Override
    public void removeEventHandler(IEventHandler<?> f)
    {
        eventHandlers.remove(f);
    }

    private void raise(String arg)
    {
        for (IEventHandler<String> e: eventHandlers)
        {
            e.handleEvent(this, arg);
        }
    }
}
