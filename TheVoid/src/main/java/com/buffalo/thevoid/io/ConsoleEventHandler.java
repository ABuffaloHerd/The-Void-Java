package com.buffalo.thevoid.io;

import com.buffalo.thevoid.equipment.SpellList;
import com.buffalo.thevoid.equipment.WeaponList;
import com.buffalo.thevoid.event.IEventHandler;
import com.buffalo.thevoid.factory.BossList;

// One class to print all boss and weapon outputs.
public class ConsoleEventHandler implements IEventHandler<String>
{
    public ConsoleEventHandler()
    {
        // empty
    }

    private static final String BOSS_OUTPUT_PREFIX = "BOSS: ";
    private static final String WEAPON_OUTPUT_PREFIX = "WEAPON: ";
    private static final String SPELL_OUTPUT_PREFIX = "SPELL: ";

    @Override
    public void handleEvent(Object sender, String args)
    {
        if(sender instanceof BossList)
            System.out.println(BOSS_OUTPUT_PREFIX + args);
        else if(sender instanceof WeaponList)
            System.out.println(WEAPON_OUTPUT_PREFIX + args);
        else if(sender instanceof SpellList)
            System.out.println(SPELL_OUTPUT_PREFIX + args);
        else
            System.out.println(args);
    }
}
