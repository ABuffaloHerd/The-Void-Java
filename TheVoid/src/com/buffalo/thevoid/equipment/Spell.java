package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.entity.IEntity;
import lombok.Getter;

import java.util.function.Function;

public class Spell extends Weapon
{
    private final @Getter int cost;

    public Spell(String name, int cost, int levelRequired, Function<IEntity, Integer> use)
    {
        super(name, use, levelRequired);
        this.cost = cost;
    }
}
