package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.data.Tuple;
import com.buffalo.thevoid.entity.DamageType;
import com.buffalo.thevoid.event.IEventHandler;
import com.buffalo.thevoid.event.IEventPublisher;
import com.buffalo.thevoid.io.ConsoleColours;
import com.buffalo.thevoid.io.MasterEventHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * This MUST be initialized with new WeaponList() before any references to it are made. Otherwise all objects in it are null.
 */
public class WeaponList implements IEventPublisher
{
    // Manual? Semi-automatic? Oh, these are the weapons!
    // These can be dumped into the initializer but i like to see what's in the list.
    private static Weapon WoodSword;
    private static Weapon Katana;
    private static Weapon HFBlade;
    private static Weapon HFMurasama;
    private static Weapon Polearm;
    private static Weapon ChiXiao;
    private static Weapon YingXiao;
    private static Weapon TBlade;
    private static Weapon Hammer;
    private static Weapon Zenith;
    private static final Set<IEventHandler<String>> eventHandlerSet = new HashSet<>();
    public static List<Tuple<Weapon, Boolean>> WeaponData = new ArrayList<>();

    // Initializer. Make all the weapons and dump them in a public list.
    // Can't be static because it needs to access member data.
    {
        System.out.println("Initializing WeaponList...");
        // TODO: Colour string formatting

        // Register event handler
        this.addEventHandler(MasterEventHandler.masterEventHandler);

        // Declare weapons
        WoodSword = new Weapon("Wooden Sword", IEntity -> IEntity.takeDamage(30, DamageType.PHYSICAL), 0);
        Katana = new Weapon("Katana",
                IEntity ->
                {
                    int ow = 0;
                    ow += IEntity.takeDamage(80, DamageType.PHYSICAL);
                    ow += IEntity.takeDamage(80, DamageType.PHYSICAL);

                    return ow;
                }, 2);

        HFBlade = new Weapon("High Frequency Blade",
                IEntity ->
                {
                    int ow = 0;
                    ow += IEntity.takeDamage(170, DamageType.PHYSICAL);
                    ow += IEntity.takeDamage(170, DamageType.PHYSICAL);
                    ow += IEntity.takeDamage(170, DamageType.PHYSICAL);

                    return ow;
                }, 4);

        HFMurasama = new Weapon("High Frequency Murasama",
                IEntity ->
                {
                    raise(String.format("%sThe only thing I know for real.%s", ConsoleColours.TEXT_GREEN, ConsoleColours.TEXT_RESET));
                    return IEntity.takeDamage(420, DamageType.PHYSICAL);
                }, 7);

        Polearm = new Weapon("Comically Large Spoon",
                IEntity ->
                {
                    int ow = 0;
                    raise(String.format("%sOnly a spoonful.%s", ConsoleColours.TEXT_GREEN, ConsoleColours.TEXT_RESET));
                    for (int i = 0; i < 3; i++)
                    {
                        ow += IEntity.takeDamage(410, DamageType.MAGIC);
                    }

                    return ow;
                }, 8);

        Hammer = new Weapon("Andre's Hammer",
                IEntity ->
                {
                    int bzzt = 0;
                    raise(String.format("%sStrike me down Zeus! You don't have the balls...%s", ConsoleColours.TEXT_GREEN, ConsoleColours.TEXT_RESET));

                    bzzt += IEntity.takeDamage(600, DamageType.MAGIC);
                    bzzt += IEntity.takeDamage(100, DamageType.PURE);

                    return bzzt;
                }, 8);

        TBlade = new Weapon("Terra Blade",
                IEntity ->
                {
                    int burn = 0;
                    for(int x = 0; x < 4; x++)
                    {
                        burn += IEntity.takeDamage(300, DamageType.MAGIC);
                    }

                    burn += IEntity.takeDamage(500, DamageType.PHYSICAL);
                    return burn;
                }, 10);

        Zenith = new Weapon("Zenith",
                IEntity ->
                {
                    int maxPain = 0;
                    for(int x = 0; x < 7; x++)
                    {
                        maxPain += IEntity.takeDamage(550, DamageType.MAGIC);
                    }

                    return maxPain;
                }, 10);

        ChiXiao = new Weapon("Chi Xiao",
                IEntity ->
                {
                    int total = 0;

                    raise("Chi Xiao - [Unsheathe]"); // It's cool when you announce what's about to happen

                    total += IEntity.takeDamage(800, DamageType.MAGIC);
                    total += IEntity.takeDamage(800, DamageType.PHYSICAL);
                    total += IEntity.takeDamage(800, DamageType.PURE);

                    return total;
                }, 12);

        YingXiao = new Weapon("Ying Xiao",
                IEntity ->
                {
                    int total = 0;

                    raise("Ying Xiao - [Shadowless]");

                    for(int x = 0; x < 10; x++)
                    {
                        total += IEntity.takeDamage(750, DamageType.PHYSICAL);
                    }

                    total += IEntity.takeDamage(1000, DamageType.PURE);
                    return total;
                }, 12);

        // TODO: RESET ALL TO FALSE EXCEPT WOODEN SWORD BEFORE SUBMISSION
        WeaponData.add(new Tuple<>(WoodSword, true));
        WeaponData.add(new Tuple<>(Katana, true));
        WeaponData.add(new Tuple<>(HFBlade, true));
        WeaponData.add(new Tuple<>(HFMurasama, true));
        WeaponData.add(new Tuple<>(Polearm, true));
        WeaponData.add(new Tuple<>(Hammer, true));
        WeaponData.add(new Tuple<>(TBlade, true));
        WeaponData.add(new Tuple<>(Zenith, true));
        WeaponData.add(new Tuple<>(ChiXiao, true));
        WeaponData.add(new Tuple<>(YingXiao, true));
    }

    // This scrolls through the list and unlocks the next weapon
    public static void unlockWeapon()
    {
        for (var v: WeaponList.WeaponData)
        {
            if(!v.getItem2())
            {
                v.setItem2(true);
                return;
            }
        }
    }

    @Override
    public void addEventHandler(IEventHandler e)
    {
        eventHandlerSet.add(e);
    }

    @Override
    public void removeEventHandler(IEventHandler f)
    {
        eventHandlerSet.remove(f);
    }

    private void raise(String arg)
    {
        for (IEventHandler<String> e: eventHandlerSet)
        {
            e.handleEvent(this, arg);
        }
    }
}
