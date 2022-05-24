package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.entity.IEntity;

import java.util.function.Function;

public abstract class AbstractWeapon
{
    protected final Function<IEntity, Integer> standardAttackMethod;
    public final String name;
    public final int levelRequired;

    public AbstractWeapon(String name, Function<IEntity, Integer> standardAttackMethod, int levelRequired)
    {
        this.name = name;
        this.levelRequired = levelRequired;
        this.standardAttackMethod = standardAttackMethod;
    }

    public int use(IEntity entity)
    {
        return standardAttackMethod.apply(entity);
    }
}
