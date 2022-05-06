package com.buffalo.thevoid.entity;

import com.buffalo.thevoid.statusbars.ProgressBar;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

// basically any living creature.
public abstract @Getter class Entity implements IEntity
{
    public String name;
    protected int MaxHP;
    protected int DEF;
    protected int RES;
    protected @Setter int HP;
    protected ProgressBar healthBar;

    protected Random rand = new Random();

    /**
     * Makes this entity get hurt.Passes amount of pain to lastDamageTaken
     * @param oAmount How much pain?
     * @param type What kind of pain?
     * @return damage taken after defense and resistances.
     */
    @Override
    public int takeDamage(int oAmount, DamageType type)
    {
        int taken = 0;

        // Add a bit of rng to the damage taken.
        int amount = rand.nextInt(Math.round(0.9f * oAmount), oAmount);

        switch(type)
        {
            case PHYSICAL: // really, defending is kinda useless
                taken = amount - this.DEF;
                break;

            case MAGIC: // RES reduces magic damage by a percentage. Integer division makes life easy.
                if(RES == 0)
                {
                    taken = amount; // avoids divide by zero
                }
                else
                {
                    taken = Math.round((float)amount / (float)this.RES);
                }
                break;

            case PURE: // This just straight-up hurts.
                taken = amount;
                break;
        }

        // The entity heals if this is negative.
        if (taken < 0)
            taken = 0;

        HP -= taken;
        updateHealthBar();
        return taken;
    }

    @Override
    public boolean isDead()
    {
        return this.HP <= 0;
    }

    public void refreshStats()
    {
        this.HP = this.MaxHP;
    }

    protected void updateHealthBar()
    {
        healthBar.setValue(this.HP);
    }
}