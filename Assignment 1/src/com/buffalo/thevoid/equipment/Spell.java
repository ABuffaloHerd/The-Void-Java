package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.entity.IEntity;

import java.util.function.Function;

public class Spell extends AbstractWeapon
{
    public final int cost;

    public Spell(final String name, Function<IEntity, Integer> useMethod, final int levelRequired, final int cost)
    {
        super(name, useMethod, levelRequired);
        this.cost = cost;
    }

    public int cast(IEntity entity)
    {
        return standardAttackMethod.apply(entity);
    }
}
