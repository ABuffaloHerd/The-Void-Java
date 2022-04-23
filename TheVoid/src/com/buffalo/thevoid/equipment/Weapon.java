package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.entity.IEntity;

import java.util.function.Function;

/**
 * Exclusively for player use.
 */
public class Weapon extends AbstractItem
{
    public int levelRequired;

    public Weapon(String name, Function<IEntity, Integer> attackMethod, int levelRequired)
    {
        super(name, attackMethod);
        this.levelRequired = levelRequired;
    }

    @Override
    public int use(IEntity e)
    {
        return super.use(e);
    }
}
