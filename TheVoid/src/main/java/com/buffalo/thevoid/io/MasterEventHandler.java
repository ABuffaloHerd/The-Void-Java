package com.buffalo.thevoid.io;

import com.buffalo.thevoid.equipment.WeaponList;
import com.buffalo.thevoid.event.IEventHandler;
import com.buffalo.thevoid.factory.BossList;

// One class to print all boss and weapon outputs.
// masterEventHandler probably isn't the best name for it.
public class MasterEventHandler implements IEventHandler<String>
{
    public static MasterEventHandler masterEventHandler = new MasterEventHandler();

    private static final String BOSS_OUTPUT_PREFIX = "BOSS: ";
    private static final String WEAPON_OUTPUT_PREFIX = "WEAPON: ";

    @Override
    public void handleEvent(Object sender, String args)
    {
        if(sender instanceof BossList)
            System.out.println(BOSS_OUTPUT_PREFIX + args);
        else if(sender instanceof WeaponList)
            System.out.println(WEAPON_OUTPUT_PREFIX + args);
    }
}
