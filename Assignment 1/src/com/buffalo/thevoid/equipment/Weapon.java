package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.entity.IEntity;
import lombok.NonNull;

import java.util.function.Function;

/**
 * Exclusively for player use.
 */
public class Weapon extends AbstractWeapon
{
    protected final Function<IEntity, Integer> specialAttackMethod; // can be null
    public final int spCost;
    public final boolean hasSpecialAttack; // extra insurance against game breaking bullshit

    public Weapon(String name, @NonNull Function<IEntity, Integer> standardAttackMethod, Function<IEntity, Integer> specialAttackMethod, int levelRequired, Integer spCost)
    {
        super(name, standardAttackMethod, levelRequired);
        this.specialAttackMethod = specialAttackMethod;
        this.spCost = spCost;

        hasSpecialAttack = specialAttackMethod != null;
    }

    public int specialAttack(IEntity entity)
    {
        if (specialAttackMethod != null) // It is the game's responsibility to check if the player has enough SP
        {
            return specialAttackMethod.apply(entity);
        }
        else
        {
            throw new IllegalStateException("This weapon does not have a special attack method");
        }
    }
}
