package com.buffalo.thevoid.equipment;

import com.buffalo.thevoid.data.Tuple;
import com.buffalo.thevoid.entity.DamageType;
import com.buffalo.thevoid.event.IEventHandler;
import com.buffalo.thevoid.event.IEventPublisher;
import com.buffalo.thevoid.gui.Mediator;
import com.buffalo.thevoid.io.ConsoleEventHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SpellList implements IEventPublisher
{
    private static Set<IEventHandler<String>> handlers = new HashSet<>();
    private static final Random r = new Random();

    private static Spell Fireball;
    private static Spell Lightning;
    private static Spell Tornado;
    private static Spell ZaWarudo;
    private static Spell Railgun;
    private static Spell WarpSnipe;
    private static Spell MasterSpark;

    public static ArrayList<Tuple<Spell, Boolean>> SpellData;

    // Non-static initializer
    {
        // Prevent duplicates when initializer is run.
        System.out.println("Initializing SpellList...");
        SpellData = new ArrayList<>();

        Fireball = new Spell("Fireball",
                IEntity ->
                {
                    raise("Magic!");
                    return IEntity.takeDamage(100, DamageType.MAGIC);
                }, 0, 40, 0.0f);

        Lightning = new Spell("Lightning Strike",
                IEntity ->
                {
                    raise("Electricity! Zzzzzap!");
                    return IEntity.takeDamage(280, DamageType.MAGIC);
                }, 3, 45, 0.1f);

        Tornado = new Spell("Tornado",
                IEntity ->
                {
                    raise("Tornado \"Indication to the Divine\"");
                    int total = IEntity.takeDamage(100, DamageType.MAGIC);

                    for(int i = 0; i < 3; i++)
                    {
                        total += IEntity.takeDamage(100, DamageType.MAGIC);
                    }
                    return total;
                }, 5, 40, 0.2f);

        ZaWarudo = new Spell("Za Warudo",
                IEntity ->
                {
                    raise("Za Warudo! Toki wo tomare!");
                    raise("Time has stopped!");
                    int total = 0;

                    for(int x = 0; x < 100; x++)
                    {
                        total += IEntity.takeDamage(10, DamageType.MAGIC);
                    }

                    raise("Toki wa ugoki dasu!");
                    return total;
                }, 7, 100, 0.13f);

        Railgun = new Spell("Railgun",
                IEntity ->
                {
                    raise("Flip a coin...");
                    int total = 0;

                    if(r.nextBoolean())
                    {
                        raise("Heads! You're lucky!");
                        total += IEntity.takeDamage(500, DamageType.PHYSICAL);
                    }
                    else
                    {
                        raise("Tails! Oh well.");
                    }
                    return IEntity.takeDamage(400, DamageType.MAGIC);
                }, 10, 110, 0.1f);

        WarpSnipe = new Spell("Warp Snipe",
                IEntity ->
                {
                    raise("Bolt fired!");
                    // Need random number generator
                    int hitcount = r.nextInt(0, 6);
                    int damage = 100; // Minimum damage is 100

                    for(int i = 0; i < hitcount; i++)
                    {
                        raise("Bolt reflected back at opponent. 80 damage added to total.");
                        damage += 80;
                    }

                    return IEntity.takeDamage(damage, DamageType.MAGIC);
                }, 11, 60, 0.1f);

        MasterSpark = new Spell("Master Spark",
                IEntity ->
                {
                    raise("Love Sign \"Master Spark!\"");
                    int total = 0;

                    total += IEntity.takeDamage(2000, DamageType.MAGIC);
                    total += IEntity.takeDamage(500, DamageType.PURE);
                    return total;
                }, 15, 200, 0.01f);

        // Register new event handler
        this.addEventHandler(new ConsoleEventHandler());

        // Register the log panel in the gui as an event handler
        this.addEventHandler(Mediator.getMainFrame().logPanel);

        SpellData.add(new Tuple<>(Fireball, true));
        SpellData.add(new Tuple<>(Lightning, false));
        SpellData.add(new Tuple<>(Tornado, false));
        SpellData.add(new Tuple<>(ZaWarudo, false));
        SpellData.add(new Tuple<>(Railgun, false));
        SpellData.add(new Tuple<>(WarpSnipe, false));
        SpellData.add(new Tuple<>(MasterSpark, false));
    }

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
    public void addEventHandler(IEventHandler e)
    {
        handlers.add(e);
    }

    @Override
    public void removeEventHandler(IEventHandler f)
    {
        handlers.remove(f);
    }

    private void raise(String s)
    {
        for(IEventHandler<String> handler : handlers)
        {
            handler.handleEvent(this, s);
        }
    }
}
