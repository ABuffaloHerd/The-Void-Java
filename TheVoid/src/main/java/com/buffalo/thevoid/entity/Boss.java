package com.buffalo.thevoid.entity;

import com.buffalo.thevoid.statusbars.MultiBar;

import java.util.Random;
import java.util.function.Function;


public class Boss extends Entity
{
    public Function<IEntity, Integer> attack;
    public Function<IEntity, Integer> specialAttack;

    private final String[] quotes;

    /**
     * Bosses are enemies that just refuse to die. Also, they should hurt a lot.
     * @param name use your head
     * @param quotes the boss likes to say things
     * @param MaxHP how much hp does it have
     * @param def ignored but assigned anyway
     * @param res ignored but assigned anyway
     * @param attack custom attack method.
     * @param specialAttack custom special attack method.
     */
    public Boss(String name, String[] quotes, int MaxHP, int def, int res, Function<IEntity, Integer> attack, Function<IEntity, Integer> specialAttack)
    {
        this.name = name;
        this.quotes = quotes;
        this.MaxHP = MaxHP;
        this.HP = this.MaxHP;
        this.DEF = def;
        this.RES = res;

        this.attack = attack;
        this.specialAttack = specialAttack;

        this.healthBar = new MultiBar('#', '-', name, MaxHP, 20, 1000);
    }

    @Override
    public int takeDamage(int oAmount, DamageType ignored)
    {
        int amount = rand.nextInt(Math.round(0.9f * oAmount), oAmount);

        // Always take 70% damage.
        int taken = Math.round(amount * 0.70f);
        taken -= this.DEF; // Make it even harder to kill.

        if (taken < 0)
        {
            taken = 1;
        }

        this.HP -= taken;
        updateHealthBar();

        return taken;
    }

    public String randomQuote()
    {
        String quote = null;
        Random r = new Random();

        while (quote == null)
            quote = quotes[r.nextInt(quotes.length)];

        return quote;
    }
}
