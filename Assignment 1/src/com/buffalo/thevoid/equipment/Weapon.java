package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.entity.IEntity;

import java.util.function.Function;

/**
 * Exclusively for player use.
 */
public class Weapon
{

    public Function<IEntity, Integer> attackMethod;
    public String name;
    public int levelRequired;

    public Weapon(String name, Function<IEntity, Integer> attackMethod, int levelRequired)
    {
        this.name = name;
        this.levelRequired = levelRequired;
        this.attackMethod = attackMethod;
    }

}
