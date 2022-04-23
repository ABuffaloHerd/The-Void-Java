package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.entity.IEntity;

import java.util.function.Function;

/**
 * base class for all items
 */
public abstract class AbstractItem implements IItem
{
    public String name;
    protected final Function<IEntity, Integer> useMethod;

    public AbstractItem(String name, Function<IEntity, Integer> useMethod)
    {
        this.name = name;
        this.useMethod = useMethod;
    }

    public int use(IEntity entity)
    {
        return useMethod.apply(entity);
    }
}