package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.data.Tuple;
import com.buffalo.thevoid.entity.DamageType;
import com.buffalo.thevoid.event.IEventHandler;
import com.buffalo.thevoid.event.IEventPublisher;
import com.buffalo.thevoid.io.ConsoleColours;
import com.buffalo.thevoid.io.ConsoleEventHandler;

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
    public static List<Tuple<Weapon, Boolean>> WeaponData;

    // Initializer. Make all the weapons and dump them in a public list.
    // Can't be static because it needs to access member data.
    {
        System.out.println("Initializing WeaponList...");
        // TODO: Colour string formatting

        WoodSword = new Weapon("Wooden Sword",
                IEntity -> IEntity.takeDamage(30, DamageType.PHYSICAL),
                null, 0, -1);

        Katana = new Weapon("Katana",
                IEntity ->
                {
                    int ow = 0;
                    ow += IEntity.takeDamage(80, DamageType.PHYSICAL);
                    ow += IEntity.takeDamage(80, DamageType.PHYSICAL);
                    return ow;
                }, null, 2, -1);

        HFBlade = new Weapon("High-Frequency Blade",
                IEntity ->
                {
                    int ow = 0;
                    ow += IEntity.takeDamage(170, DamageType.PHYSICAL);
                    ow += IEntity.takeDamage(170, DamageType.PHYSICAL);
                    return ow;
                }, null, 4, -1);

        HFMurasama = new Weapon("High-Frequency Murasama",
                IEntity ->
                {
                    raise(String.format("%sThe only thing I know for real.%s", ConsoleColours.TEXT_GREEN, ConsoleColours.TEXT_RESET));
                    int ow = 0;
                    ow += IEntity.takeDamage(200, DamageType.PHYSICAL);
                    ow += IEntity.takeDamage(200, DamageType.PHYSICAL);
                    return ow;
                },
                IEntity ->
                {
                    raise(String.format("%sIt's the only thing I've ever known.%s", ConsoleColours.TEXT_GREEN, ConsoleColours.TEXT_RESET));
                    int jetstreamsam = 0;
                    jetstreamsam += IEntity.takeDamage(420, DamageType.PHYSICAL);
                    return jetstreamsam;
                }, 7, 30);

        Polearm = new Weapon("Comically Large Spoon",
                IEntity ->
                {
                    int iceCream = 0;
                    iceCream += IEntity.takeDamage(350, DamageType.PHYSICAL);
                    iceCream += IEntity.takeDamage(250, DamageType.MAGIC);
                    return iceCream;
                },
                IEntity ->
                {
                    int kingBach = 0;
                    for(int x = 0; x < 3; x++)
                    {
                        kingBach += IEntity.takeDamage(410, DamageType.MAGIC);
                    }

                    return kingBach;
                }, 8, 40);

        Hammer = new Weapon("Andre's Hammer",
                IEntity ->
                {
                    int ow = 0;
                    ow += IEntity.takeDamage(400, DamageType.PHYSICAL);
                    return ow;
                },
                IEntity ->
                {
                    int bzzt = 0;
                    raise(String.format("%sStrike me down Zeus! You don't have the balls...%s", ConsoleColours.TEXT_GREEN, ConsoleColours.TEXT_RESET));

                    bzzt += IEntity.takeDamage(600, DamageType.MAGIC);
                    bzzt += IEntity.takeDamage(100, DamageType.PURE);

                    return bzzt;
                }, 8, 55);

        TBlade = new Weapon("Terra Blade",
                IEntity ->
                {
                    int burn = 0;
                    for(int x = 0; x < 4; x++)
                    {
                        burn += IEntity.takeDamage(300, DamageType.MAGIC);
                    }

                    burn += IEntity.takeDamage(450, DamageType.PHYSICAL);
                    return burn;
                },
                IEntity ->
                {
                    int burn = 0;
                    for(int x = 0; x < 3; x++)
                    {
                        burn += IEntity.takeDamage(350, DamageType.PHYSICAL);
                    }

                    burn += IEntity.takeDamage(100, DamageType.PURE);
                    return burn;
                }, 11, 45);

        Zenith = new Weapon("Zenith",
                IEntity ->
                {
                    int maxPain = 0;
                    for(int x = 0; x < 7; x++)
                    {
                        maxPain += IEntity.takeDamage(650, DamageType.PHYSICAL);
                    }

                    return maxPain;
                },
                IEntity ->
                {
                    raise("Zenith - [It has to be this way]");
                    int maxPain = 0;
                    for(int x = 0; x < 21; x++)
                    {
                        maxPain += IEntity.takeDamage(100, DamageType.MAGIC);
                    }

                    return maxPain;
                }, 13, 30);

        ChiXiao = new Weapon("Chi Xiao",
                IEntity ->
                {
                    raise("Chi Xiao - [Dual Strike]");
                    int total = 0;
                    total += IEntity.takeDamage(500, DamageType.PHYSICAL);
                    total += IEntity.takeDamage(500, DamageType.PHYSICAL);
                    return total;
                },
                IEntity ->
                {
                    int total = 0;

                    raise("Chi Xiao - [Unsheathe]");

                    total += IEntity.takeDamage(800, DamageType.MAGIC);
                    total += IEntity.takeDamage(800, DamageType.PHYSICAL);
                    total += IEntity.takeDamage(800, DamageType.PURE);

                    return total;
                }, 16, 55);

        YingXiao = new Weapon("Ying Xiao",
                IEntity ->
                {
                    raise("Ying Xiao - [Dual Strike]");
                    int total = 0;
                    total += IEntity.takeDamage(500, DamageType.MAGIC);
                    total += IEntity.takeDamage(500, DamageType.MAGIC);
                    return total;
                },
                IEntity ->
                {
                    raise("Ying Xiao - [Shadowless]");
                    int total = 0;

                    for(int x = 0; x < 10; x++)
                    {
                        total += IEntity.takeDamage(750, DamageType.PHYSICAL);
                    }
                    raise("Slash!");
                    total += IEntity.takeDamage(1000, DamageType.PURE);

                    return total;
                }, 16, 60);

        // Register event handler
        this.addEventHandler(new ConsoleEventHandler());

        // TODO: RESET ALL TO FALSE EXCEPT WOODEN SWORD BEFORE SUBMISSION
        WeaponData  = new ArrayList<>();
        WeaponData.add(new Tuple<>(WoodSword, true));
        WeaponData.add(new Tuple<>(Katana, false));
        WeaponData.add(new Tuple<>(HFBlade, false));
        WeaponData.add(new Tuple<>(HFMurasama, false));
        WeaponData.add(new Tuple<>(Polearm, false));
        WeaponData.add(new Tuple<>(Hammer, false));
        WeaponData.add(new Tuple<>(TBlade, false));
        WeaponData.add(new Tuple<>(Zenith, false));
        WeaponData.add(new Tuple<>(ChiXiao, false));
        WeaponData.add(new Tuple<>(YingXiao, false));
    }

    // This scrolls through the list and unlocks the next weapon
    public static boolean unlockWeapon()
    {
        for (var v: WeaponList.WeaponData)
        {
            if(!v.getItem2())
            {
                v.setItem2(true);
                return true;
            }
        }
        
        return false;
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
