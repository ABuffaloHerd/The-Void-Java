package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.entity.IEntity;

import java.util.function.Function;

public class Spell extends AbstractWeapon
{
    public final int cost; // This is all the game needs to know about a spell. the game will handle spell checking and mana consumption.
    public final float failrate; // You know what's funny? Spells blowing up in your face.

    public Spell(final String name, Function<IEntity, Integer> useMethod, final int levelRequired, final int cost, final float failrate)
    {
        super(name, useMethod, levelRequired);
        this.cost = cost;
        this.failrate = failrate;
    }
}
