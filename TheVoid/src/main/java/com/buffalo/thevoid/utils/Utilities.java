package com.buffalo.thevoid.utils;

import com.buffalo.thevoid.data.Tuple;
import com.buffalo.thevoid.equipment.SpellList;
import com.buffalo.thevoid.equipment.WeaponList;

/**
 * Functions that do not fit in any other category.
 */
public class Utilities
{
    public static Tuple<Integer, Integer> getWeaponCount()
    {
        // Count weapons unlocked
        int weaponsUnlocked = 0;
        for(var v: WeaponList.WeaponData)
            if (v.getItem2())
                weaponsUnlocked++;

        // Count spells unlocked
        int spellsUnlocked = 0;
        for(var v: SpellList.SpellData)
            if(v.getItem2())
                spellsUnlocked++;

        // Compensating for wooden sword being unlocked at the start.
        weaponsUnlocked -= 1;

        // Compensating for fireball being unlocked at the start.
        spellsUnlocked -= 1;

        return new Tuple<>(weaponsUnlocked, spellsUnlocked);
    }
}
