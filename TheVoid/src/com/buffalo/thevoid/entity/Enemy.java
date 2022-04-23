package com.buffalo.thevoid.entity;

import com.buffalo.thevoid.statusbars.ProgressBar;
import lombok.Getter;

/**
 * Generic enemy. Does physical damage.
 */
public class Enemy extends Entity
{
    private final @Getter int ATK;

    public Enemy(String name, int atk, int def, int res, int maxhp)
    {
        this.name = name;
        this.ATK = atk;
        this.DEF = def;
        this.RES = res;
        this.MaxHP = maxhp;
        this.HP = maxhp;

        this.healthBar = new ProgressBar('#', '-', this.name, this.MaxHP, 10);
    }

    public int attack(IEntity e)
    {
        return e.takeDamage(ATK, DamageType.PHYSICAL);
    }
}
