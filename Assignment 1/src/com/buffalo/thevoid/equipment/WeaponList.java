package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.data.Tuple;
import com.buffalo.thevoid.entity.DamageType;
import com.buffalo.thevoid.entity.IEntity;
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
    public static List<Tuple<Weapon, Boolean>> WeaponData;

    // Initializer. Make all the weapons and dump them in a public list.
    // Can't be static because it needs to access member data.
    {
        System.out.println("Initializing WeaponList...");
        // TODO: Colour string formatting

        WoodSword = new Weapon("Wooden Sword",
                IEntity ->
                {
                    return IEntity.takeDamage(30, DamageType.PHYSICAL);
                }, null, 0, null);

        // Register event handler
        this.addEventHandler(MasterEventHandler.masterEventHandler);

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
