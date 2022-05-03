package com.buffalo.thevoid.entity;

import com.buffalo.thevoid.equipment.Spell;
import com.buffalo.thevoid.equipment.Weapon;
import com.buffalo.thevoid.exception.InsufficientManaException;
import com.buffalo.thevoid.statusbars.ProgressBar;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

// You the player
// Can't put @Getter because of one private attribute.
public class Player extends Entity
{
    private @Getter @Setter int MaxMP;
    private @Getter int MP; // Manual setter
    private @Getter int level;
    private @Getter ProgressBar magicBar; // Remember to print this in battles.
    private @Getter @Setter Weapon weapon;
    private @Getter @Setter Spell spell;
    private @Getter int killCount; // Increments when you kill an enemy. Getter is only for saving.

    // NO GETTERS ALLOWED
    private int killsToLevel;

    public boolean isDefending;

    // Original constructor
    public Player(String name, int MaxHP, int MaxMP, int def, int res, Weapon weapon)
    {
        this.name = name;
        this.MaxHP = MaxHP;
        this.HP = MaxHP;
        this.MaxMP = MaxMP;
        this.MP = MaxMP;
        this.killCount = 0;
        this.level = 0;

        this.DEF = def;
        this.RES = res;
        this.isDefending = false;

        this.weapon = weapon;

        this.rand = new Random();

        this.healthBar = new ProgressBar('=', '-', this.name, this.MaxHP, 10);
        this.magicBar = new ProgressBar('=', '~', "MP", this.MaxMP, 10);
    }

    // New constructor. uses the old one.
    public Player(String name, int MaxHP, int MaxMP, int def, int res, Weapon weapon, Spell spell, int killsToLevel, int level)
    {
        this(name, MaxHP, MaxMP, def, res, weapon);
        this.killsToLevel = killsToLevel;
        this.spell = spell;

        // Overwrite level regardless.
        this.level = level;
    }

    // Constructor for when you load a player
    public Player(String name, int MaxHP, int MaxMP, int DEF, int RES, int killCount, int level, int killsToLevel, Weapon weapon, Spell spell)
    {
        this(name, MaxHP, MaxMP, DEF, RES, weapon, spell, killsToLevel, level);
        this.killCount = killCount;
    }

    // Yeah. this override was just to add one if else block.
    @Override
    public int takeDamage(int oAmount, DamageType type)
    {
        int taken = 0;
        int amount = rand.nextInt(Math.round(0.9f * oAmount), oAmount);

        switch(type)
        {
            case PHYSICAL: // really, defending is kinda useless
                if (isDefending)
                    taken = amount - (this.DEF * 2);
                else
                    taken = amount - this.DEF;
                break;

            case MAGIC: // RES reduces magic damage by a percentage. Integer division makes life easy.
                if(RES == 0) // Good thing i caught this one. I could have just multiplied by zero to cancel out the division.
                {
                    taken = amount;
                }
                else
                {
                    taken = Math.round((float)amount * ((float)this.RES) / 100.0f);
                }
                break;

            case PURE: // This just straight-up hurts.
                taken = amount;
                break;
        }

        // The player heals if this check isn't made
        if (taken < 0)
            taken = 0;

        this.HP -= taken;
        updateHealthBar();
        return taken;
    }

    // Costs 20 MP, heals 25% of max hp
    public int heal() throws InsufficientManaException
    {
        int amount;

        if ((this.MP - 20) < 0)
            throw new InsufficientManaException("Not enough mana to heal.");
        else
        {
            this.MP -= 20;
            amount = Math.round(MaxHP * 0.25f);

            this.HP += amount;
            updateHealthBar();
            updateMagicBar();

            // Make sure hp is capped
            if(HP > MaxHP)
                HP = MaxHP;
            return amount;
        }
    }

    // Level up, returns if level up was successful
    public boolean updateLevel()
    {
        // For safety, check tht killcount and killstolevel are not zero
        if(killCount == 0 || killsToLevel == 0)
            return false;

        // Check how many times you've levelled up
        int levelUpCount = killCount / killsToLevel;

        // Part that does the stuff related to levelling up
        for(int x = 0; x < levelUpCount; x++)
        {
            level++;
            this.MaxHP += 50;
            this.MaxMP += 25;
            this.DEF += 15;
            this.RES += 5;
        }

        // Update the bars
        this.healthBar.setMax(this.MaxHP);
        this.magicBar.setMax(this.MaxMP);

        // Make sure res is capped at 90
        if(this.RES > 90)
            this.RES = 90;

        return levelUpCount > 0;
    }

    @Override
    public void refreshStats()
    {
        this.MP = this.MaxMP;
        this.HP = this.MaxHP;
        updateHealthBar();
        updateMagicBar();
    }

    public void setMP(int MP)
    {
        this.MP = MP;
        updateMagicBar();
    }

    private void updateMagicBar()
    {
        magicBar.setValue(MP);
    }

    public void incrementKillCount()
    {
        killCount++;
    }
}
