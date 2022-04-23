package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.data.Tuple;
import com.buffalo.thevoid.entity.DamageType;
import com.buffalo.thevoid.event.IEventHandler;
import com.buffalo.thevoid.event.IEventPublisher;
import com.buffalo.thevoid.io.MasterEventHandler;
import com.buffalo.thevoid.io.TextHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SpellList implements IEventPublisher
{
    private Set<IEventHandler<String>> handlers;

    private Spell Lightning;
    private Spell Fireball; // wizard clash of clans
    private Spell ZaWarudo; // you know what this is
    private Spell BlackHole;
    private Spell WarpSnipe; // kingdom hearts reference
    private Spell MasterSpark; // touhou reference
    private Spell Railgun; // railgun reference
    // copilot do you have any good spell names?
    private Spell Copilot; // ok buddy.

    public static final ArrayList<Tuple<Spell, Boolean>> SpellData = new ArrayList<>();

    // Non static initializer
    {
        // Register event handler
        handlers = new HashSet<>(1);
        addEventHandler(MasterEventHandler.masterEventHandler);

        Lightning = new Spell("Lightning", 30, 2, e ->
        {
            raise("Zap!");
            return e.takeDamage(100, DamageType.MAGIC);
        });

        Fireball = new Spell("Fireball", 50, 3, e ->
        {
            raise("Fireball!");
            return e.takeDamage(150, DamageType.MAGIC);
        });

        ZaWarudo = new Spell("Za Warudo", 150, 11, e ->
        {
            raise("ZA WARUDO! TOKI WO TOMARE!");
            TextHandler.wait(1000);
            int damage = 0;

            for(int i = 0; i < 100; i++)
            {
                raise("Silverware thrown at opponent. 20 damage added to total.");
                damage += 20;
            }

            raise("Toki wa ugoki dasu!");
            return e.takeDamage(damage, DamageType.MAGIC);
        });

        BlackHole = new Spell("Black Hole", 750, 20, e ->
        {
            raise("69420 / 0");
            return e.takeDamage(10000, DamageType.PURE);
        });

        WarpSnipe = new Spell("Warp Snipe", 80, 7, e ->
        {
            raise("Bolt fired!");
            // Need random number generator
            int hitcount = new Random().nextInt(0, 6); // hitcount between 0 and 5 times
            int damage = 60; // Minimum damage is 60

            for(int i = 0; i < hitcount; i++)
            {
                raise("Bolt reflected back at opponent. 15 damage added to total.");
                damage += 15;
            }

            return e.takeDamage(damage, DamageType.MAGIC);
        });

        MasterSpark = new Spell("Master Spark", 120, 9, e ->
        {
            raise("Love Sign \"Master Spark\"");
            return e.takeDamage(300, DamageType.MAGIC);
        });

        Railgun = new Spell("Railgun", 240, 13, e ->
        {
            raise("Coinflip~");
            int damage = 0;
            damage +=  e.takeDamage(500, DamageType.MAGIC);
            damage += e.takeDamage(400, DamageType.PHYSICAL);

            return damage;
        });

        Copilot = new Spell("Copilot", 30, 2, e ->
        {
            raise("Copilot! Take the wheel!");
            // you can do better than that
            return e.takeDamage(100, DamageType.MAGIC);
        });

        SpellData.add(new Tuple<>(Lightning, false));
        SpellData.add(new Tuple<>(Fireball, false));
        SpellData.add(new Tuple<>(ZaWarudo, false));
        SpellData.add(new Tuple<>(BlackHole, false));
        SpellData.add(new Tuple<>(WarpSnipe, false));
        SpellData.add(new Tuple<>(MasterSpark, false));
        SpellData.add(new Tuple<>(Railgun, false));
        SpellData.add(new Tuple<>(Copilot, false));
    }

    // This scrolls through the list and unlocks the next spell
    public static boolean unlockSpell()
    {
        for (var v: SpellList.SpellData)
        {
            if(!v.getItem2())
            {
                v.setItem2(true);
                return true;
            }
        }

        return false;
    }

    @Override
    @SuppressWarnings("unchecked") // shut up
    public void addEventHandler(IEventHandler e)
    {
        handlers.add(e);
    }

    @Override
    public void removeEventHandler(IEventHandler<?> f)
    {
        handlers.remove(f);
    }

    private void raise(String args)
    {
        for (var handler : handlers)
        {
            handler.handleEvent(this, args);
        }
    }
}
