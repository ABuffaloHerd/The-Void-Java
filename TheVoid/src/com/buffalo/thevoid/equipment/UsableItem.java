package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.entity.Enemy;
import com.buffalo.thevoid.entity.IEntity;
import com.buffalo.thevoid.entity.Player;

import java.util.function.Function;

/**
 * Used by player only on enemies.
 */
public class UsableItem extends AbstractItem
{
    private int cooldown; // How often this can be used.
    private int uses; // How many times it has been used.

    public UsableItem(String name, Function<IEntity, Integer> useMethod, int cooldown)
    {
        super(name, useMethod);
        this.cooldown = cooldown;
    }

    public int use(IEntity entity)
    {
        if(cooldown > 0)
            return 0;

        if(entity instanceof Player)
            throw new IllegalArgumentException("Player cannot use this item.");

        uses++;
        return super.use(entity);
    }

    public void cycle()
    {
        if(cooldown > 0)
            cooldown--;
    }
}
