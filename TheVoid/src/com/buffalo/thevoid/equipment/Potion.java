package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.entity.IEntity;

import java.util.function.Function;

public class Potion extends AbstractItem
{
    public Potion(String name, Function<IEntity, Integer> useMethod)
    {
        super(name, useMethod);
    }
}
