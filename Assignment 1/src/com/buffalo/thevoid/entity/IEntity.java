package com.buffalo.thevoid.entity;

// The base of anything that gets hurt.
public interface IEntity
{
    int takeDamage(int amount, DamageType type);
    boolean isDead();
}
