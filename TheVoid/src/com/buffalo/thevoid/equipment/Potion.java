package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.entity.Enemy;
import com.buffalo.thevoid.entity.IEntity;
import com.buffalo.thevoid.entity.Player;

import java.util.function.Function;

public class Potion extends AbstractItem
{
    public Potion(String name, Function<IEntity, Integer> useMethod)
    {
        super(name, useMethod);
    }

    @Override
    public int use(IEntity entity)
    {
        if(entity instanceof Enemy)
            throw new IllegalArgumentException("You can't use potions on enemies!");

        return super.use(entity);
    }
}
