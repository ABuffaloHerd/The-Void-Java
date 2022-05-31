package com.buffalo.thevoid.main;

import com.buffalo.thevoid.db.Database;
import com.buffalo.thevoid.entity.*;
import com.buffalo.thevoid.equipment.SpellList;
import com.buffalo.thevoid.equipment.WeaponList;
import com.buffalo.thevoid.event.GameEvent;
import com.buffalo.thevoid.event.IEventHandler;
import com.buffalo.thevoid.exception.InsufficientManaException;
import com.buffalo.thevoid.exception.ResetBattleLoopException;
import com.buffalo.thevoid.exception.TableAlreadyExistsException;
import com.buffalo.thevoid.factory.BossList;
import com.buffalo.thevoid.factory.MonsterFactory;
import com.buffalo.thevoid.gui.Mediator;
import com.buffalo.thevoid.io.ConsoleColours;
import com.buffalo.thevoid.io.FileHandler;
import com.buffalo.thevoid.io.TextHandler;

import java.util.Random;

/**
 * This bad boy contains ALL game related functions, while the actual loop is handled by the main function.
 */
public class GameManager implements IEventHandler<GameEvent>
{
    // NOTE: objects are pass by reference
    private Player player;
    private boolean playerLoaded;
    private Database database;

    // Initializer
    {
        database = new Database();
    }

    // calls methods to handle game events
    // program flow returns to main function after this.
    // Since this class handles events, it calls this function if it wants to raise an event.
    @Override
    public void handleEvent(Object sender, GameEvent args)
    {
        // If this calls a method then the switch block is not responsible for waiting.
        switch (args)
        {
            case NEWGAME ->
                    {
                        player = NewGame.newPlayer();
                        Mediator.updatePlayer(player);
                        playerLoaded = player != null;

                        if(playerLoaded)
                        {
                            // Save the player to the database
                            try
                            {
                                database.clearAll();
                                database.setup();
                            }
                            catch (TableAlreadyExistsException e)
                            {
                                System.out.println("Table already exists. Dropping and recreating table.");
                            }

                        }
                    }
            case LOADGAME ->
                    {
                        player = NewGame.loadPlayer();
                        playerLoaded = player != null;
                        if(playerLoaded)
                        {
                            System.out.printf("Player %s loaded!\n", player.name);
                            Mediator.sendToLog(String.format("Player %s loaded!", player.name));
                            Mediator.updatePlayer(player);
                        }
                        TextHandler.wait(1800);
                    }
            case SAVEGAME ->
                    {
                        FileHandler.savePlayer(player);
                        System.out.printf("Player %s saved!\n", player.name);
                        Mediator.sendToLog(String.format("Player %s saved!", player.name));
                        TextHandler.wait(2500);

                        database.writePlayer(player);
                    }
            case SHOP -> shop();
            case RESUPPLY ->
                    { // resupply method
                        player.refreshStats();
                        System.out.println("Your stats have been replenished!");
                        Mediator.sendToLog("Your stats have been replenished!");
                        Mediator.updatePlayer(player);
                        TextHandler.wait(1500);
                    }
            case BATTLE ->
                    {
                        Mediator.disableTextFields();
                        Mediator.setButtons(6);
                        battle();
                    }
            case BOSS ->
                    {
                        Mediator.setButtons(6);
                        bossBattle();
                    }
            case EXBOSS ->
                    {
                        Mediator.setButtons(6);
                        Mediator.disableTextFields();
                        Mediator.enableButtons();
                        finalBoss();
                    }
            case LEVELUP ->
                    { // This is short enough to include the wait.
                        if(WeaponList.unlockWeapon())
                        {
                            System.out.println("New weapon unlocked!");
                            Mediator.sendToLog("New weapon unlocked!");
                        }


                        if(SpellList.unlockSpell())
                        {
                            System.out.println("New spell unlocked!");
                            Mediator.sendToLog("New spell unlocked!");
                        }
                        
                        TextHandler.wait(1000);
                    }
            case PLAYERWIN ->
                    {
                        player.incrementKillCount();
                        TextHandler.wait(3000);

                        // Update level
                        if (player.updateLevel())
                        {
                            System.out.println("You leveled up!");
                            Mediator.sendToLog("You leveled up!");
                            handleEvent(this, GameEvent.LEVELUP);
                        }
                        TextHandler.wait(1000); // No auto resupply
                    }
            case PLAYERLOSE ->
                    {
                        Mediator.sendToLog("You have DIED!");
                        Mediator.sendToLog("ggez you suck");
                        Mediator.sendToLog("get rekt");
                        Mediator.sendToLog("go touch some grass");
                        Mediator.sendToLog("so many nubs in this game");
                        TextHandler.wait(8000);
                        handleEvent(this, GameEvent.RESUPPLY);
                    }
        }
    }

    // Not really a shop, but it's where you can equip weapons
    // This is responsible for checking if the player is able to use the weapon
    // as well as assigning it to the player.
    private void shop()
    {
        // Print weapons that are unlocked
        int unlocked = 0; // Keep track of this for validation
        int counter = 0; // need an index too
        Mediator.logBreak(3);

        Mediator.sendToLog("Inventory:");
        Mediator.sendToLog("Currently equipped:" + player.getWeapon().name);
        Mediator.sendToLog("The following weapons are available:");
        //System.out.printf("%s[Index - Name]%s\n\n", ConsoleColours.TEXT_GREEN, ConsoleColours.TEXT_RESET);

        for (var set : WeaponList.WeaponData)
        {
            if (set.getItem2()) // If it is unlocked
            {
                Mediator.sendToLog(String.format("%d - %s | Level required: %d\n", counter, set.getItem1().name, set.getItem1().levelRequired));
                unlocked++;
                counter++;
            }
        }

        // Uhh due to an oversight, if the weapons are not unlocked in order then the weapon equipped will not
        // correspond to the actual weapon index.
        // However, this shouldn't happen to begin with so i'm going to ignore it.
        Mediator.sendToLog(this, "Enter the index of the weapon you wish to equip.");
        Mediator.setButtons(unlocked);
        int selection = TextHandler.validInt(unlocked - 1, 0);

        // Check level
        if (WeaponList.WeaponData.get(selection).getItem1().levelRequired > player.getLevel())
        {
            System.out.println("Your level is too low!");
            Mediator.sendToLog("Your level is too low!");
        }
        else
        {
            player.setWeapon(WeaponList.WeaponData.get(selection).getItem1()); // Set weapon
            System.out.printf("Equipped %s%s%s!\n", ConsoleColours.TEXT_BLUE, player.getWeapon().name, ConsoleColours.TEXT_RESET);
            Mediator.sendToLog(String.format("Equipped %s!", player.getWeapon().name));
        }

        TextHandler.wait(1000);

        // Print spells that are unlocked
        unlocked = 0; // reset
        counter = 0; // reset

        System.out.printf("%sSpells%s\n\n", ConsoleColours.ANSI_GREEN_BACKGROUND, ConsoleColours.TEXT_RESET);
        System.out.printf("Currently equipped: %s%s%s\n", ConsoleColours.TEXT_BLUE, player.getSpell().name, ConsoleColours.TEXT_RESET);
        System.out.println("The following spells are available:");
        System.out.printf("%s[Index - Name]%s\n\n", ConsoleColours.TEXT_GREEN, ConsoleColours.TEXT_RESET);

        Mediator.sendToLog("Spells:");
        Mediator.sendToLog("Currently equipped:" + player.getSpell().name);
        Mediator.sendToLog("The following spells are available:");
        Mediator.sendToLog("Index - Name");

        for (var s : SpellList.SpellData)
        {
            if (s.getItem2()) // If it is unlocked
            {
                System.out.printf("%d - %s | Level required: %d\n", counter, s.getItem1().name, s.getItem1().levelRequired);
                Mediator.sendToLog(String.format("%d - %s | Level required: %d\n", counter, s.getItem1().name, s.getItem1().levelRequired));
                unlocked++;
                counter++;
            }
        }

        // This also has the same issue as the weapon selection
        selection = TextHandler.validInt(unlocked - 1, 0);

        // Check level
        if (SpellList.SpellData.get(selection).getItem1().levelRequired > player.getLevel())
        {
            System.out.println("Your level is too low!");
            Mediator.sendToLog("Your level is too low!");
        }
        else
        {
            player.setSpell(SpellList.SpellData.get(selection).getItem1()); // Set spell
            Mediator.sendToLog(String.format("Equipped %s!", player.getSpell().name));
        }
    }

    /**
     * Battle a generic enemy.
     * THIS IS WHAT NEEDS TO HAPPEN:<br>
     * 1. Generate a random enemy<br>
     * 2. Battle<br>
     * 3. If you win, increase killcount (handed over to the handleEvent method)<br>
     * 4. If you lose, DIE<br>
     * 6. After winning, evaluate if you should level up<br>
     */
    private void battle()
    {
        TextHandler.clearConsole();
        Enemy e = MonsterFactory.generateEnemy(player.getLevel());

        Mediator.sendToLog("+-------------------------------------+");
        Mediator.sendToLog(String.format("    %s has appeared!", e.name));
        Mediator.sendToLog("+-------------------------------------+");
        while (!player.isDead() && !e.isDead())
        {
            printHeader(true);

            if (battleCycle(e)) continue;

            // ENEMY ACTION SECTION
            // This is just a dummy that does nothing but attack so...
            Mediator.sendToLog(String.format("%s is attacking!", e.name));
            Mediator.sendToLog("Took " + e.attack(player) + " damage!");

            printHeader(false);

            // No need to check if player died because the loop restarts at this point.
            // Wait 1.5 seconds to continue so that player can read screen
            TextHandler.wait(1500);
        }

        // Once we break the loop, check who died
        if (player.isDead())
        {
            Mediator.sendToLog(GameEvent.PLAYERLOSE.text);
            handleEvent(this, GameEvent.PLAYERLOSE);
        }
        else // THERE CAN BE ONLY ONE
        {
            Mediator.sendToLog(GameEvent.PLAYERWIN.text);
            handleEvent(this, GameEvent.PLAYERWIN);
        }
    }

    private boolean battleCycle(Entity e)
    {
        battleMenu(e);
        Mediator.sendToLog("Select an action: (Use the buttons)");
        int choice = TextHandler.validInt(6, 1);

        // PLAYER ACTION SECTION
        try
        {
            playerAction(choice, e);
        }
        catch (InsufficientManaException ex)
        {
            Mediator.sendToLog(ex.message);
            TextHandler.wait(1000);
            return true;
        }
        catch (ResetBattleLoopException ex)
        {
            return true;
        }

        // If the enemy is dead reevaluate the loop
        return e.isDead();
    }

    /**
     * Boss battle
     * This is what needs to happen:
     * 1. Show bosslist<br>
     * 1.5 Pick a boss, make sure level is suitable.<br>
     * 2. Battle<br>
     * 3. If you win, increase killcount by 5, then check level (handled by handleEvent)<br>
     * 4. If you lose, DIE<br>
     * Might be able to cram some of the above battle method into its own section.<br>
     */
    private void bossBattle()
    {
        int toSpecial = 0; // When this hits 5, the boss unleashes special attack, probably vaporising the player
        TextHandler.clearConsole();
        Mediator.clearLog();

        // Show bosslist
        Mediator.sendToLog("+-------------------------------------+");
        Mediator.sendToLog("    Choose a boss to fight:");
        Mediator.sendToLog("+-------------------------------------+");

        int counter = 0; // Need this to keep track of which boss we are on
        for(var x : BossList.BossData)
        {
            Mediator.sendToLog(String.format("%d - %s [Defeated: %b] Level req: %d\n", counter, x.getItem1().name, x.getItem2(), x.getItem3()));
            counter++;
        }

        // Pick a boss, make sure level matches
        Mediator.sendToLog("Enter a number to select a boss: ");
        int bossSelected = TextHandler.validInt(counter, 0);
        if(BossList.BossData.get(bossSelected).getItem3() > player.getLevel())
        {
            Mediator.sendToLog("Your level is too low to fight this boss! (You'll explode if you try)");
            TextHandler.wait(2000);
            return;
        }

        // Battle
        Boss boss = BossList.BossData.get(bossSelected).getItem1(); // Select boss
        Mediator.sendToLog(String.format("You have chosen %s!\n", boss.name));
        TextHandler.wait(2000);

        // Change controls to battle mode
        Mediator.disableTextFields();
        Mediator.enableButtons();

        while(!boss.isDead() && !player.isDead())
        {
            printHeader(true);
            // Boss quote first
            Mediator.sendToLog(boss, boss.randomQuote());
            TextHandler.wait(1000);

            if(battleCycle(boss)) continue;

            // ENEMY ACTION SECTION
            // Right the boss is going to use special attack UNLESS the special attack counter is 5.
            if (toSpecial == 5)
            {
                Mediator.sendToLog(String.format("%s is unleashing a special attack!", boss.name));
                Mediator.sendToLog("This is going to hurt...");
                TextHandler.wait(1000);

                // Do the special attack
                Mediator.sendToLog("Took " + boss.specialAttack.apply(player) + " damage!");

                // Reset the counter
                toSpecial = 0;
            }
            else
            {
                // Just a regular attack :((
                Mediator.sendToLog(boss.name + " is attacking!");
                TextHandler.wait(1000);

                Mediator.sendToLog("Took " + boss.attack.apply(player) + " damage!");

                // Increment special counter
                toSpecial++;
            }

            // Let's see if the player survived.
            if (player.isDead())
                continue;

            printHeader(false);

            // Wait 1.5 seconds to continue so that player can read screen
            TextHandler.wait(1500);
        }

        // Ok so someone died.
        if (player.isDead())
        {
            System.out.println(GameEvent.PLAYERLOSE.text);
            handleEvent(this, GameEvent.PLAYERLOSE);
        }
        else
        {
            Mediator.sendToLog(GameEvent.PLAYERWIN.text);
            if (BossList.BossData.get(bossSelected).getItem2())
            {
                Mediator.sendToLog("You have already defeated this boss!");
            }
            else
            {   // One time reward of 5 kills
                Mediator.sendToLog("You have defeated this boss for the first time!");
                BossList.BossData.get(bossSelected).setItem2(true); // mark boss as defeated
                for(int x = 0; x < 5; x++)
                {
                    player.incrementKillCount();
                }
            }
            handleEvent(this, GameEvent.PLAYERWIN); // this part of the code will increment the kill count again AND check for levelup
        }
    }

    /**
     * Final boss battle, very long and very drawn out. Don't get me started on the difficulty.
     */
    private void finalBoss()
    {
        int toSpecial = 0; // Every 5 attacks, the boss unleashes consecutive special attack, probably annihilating the player instantly
        TextHandler.clearConsole();

        // Cinematic text sequence
        Mediator.sendToLog("+-------------------------------------+");
        Mediator.sendToLog("    Final Boss Battle");
        Mediator.sendToLog("+-------------------------------------+");

        TextHandler.wait(2000);
        Mediator.sendToLog("The ground shakes violently.");
        TextHandler.wait(2000);
        Mediator.sendToLog("A small, dark figure appears in the center of the room.");
        TextHandler.wait(2000);
        Mediator.sendToLog("The room bursts into flames.");

        // Battle
        ExBoss boss = BossList.Hirina;
        while(!boss.isDead() && !player.isDead())
        {
            printHeader(true);

            // Boss quote first
            Mediator.sendToLog(boss, boss.randomQuote());
            TextHandler.wait(1000);

            if(battleCycle(boss)) continue;

            // ENEMY ACTION SECTION
            // Right the boss is going to attack UNLESS the special attack counter is 5.
            if (toSpecial == 5)
            {
                Mediator.sendToLog(String.format("Phase 2: %s is unleashing a special attack!", boss.name));
                Mediator.sendToLog("This is going to hurt...");
                TextHandler.wait(1000);

                // Do the special attack
                Mediator.sendToLog("Took " + boss.specialAttack.apply(player) + " damage!");
            }
            else if (toSpecial == 10)
            {
                // second special attack
                Mediator.sendToLog(String.format("Phase 3: %s is kicking it up a notch!", boss.name));
                Mediator.sendToLog("Prepare for a lot of stinging!");
                TextHandler.wait(1000);

                // Do the special attack
                Mediator.sendToLog("Took " + boss.specialAttack2.apply(player) + " damage!");
            }
            else if (toSpecial == 15 || toSpecial > 20)
            {
                // third special attack
                Mediator.sendToLog(String.format("Phase 4: %s has had it with you.", boss.name));
                Mediator.sendToLog("I hope your affairs are in order...");
                TextHandler.wait(1000);

                // Do the special attack
                System.out.println("Took " + boss.specialAttack3.apply(player) + " damage!");
            }
            else
            {
                // Just a regular attack :((
                Mediator.sendToLog(String.format("%s is attacking!\n", boss.name));
                TextHandler.wait(1000);

                Mediator.sendToLog("Took " + boss.attack.apply(player) + " damage!");

            }

            // Increment special counter
            toSpecial++;

            // Let's see if the player survived.
            if (player.isDead())
                continue;

            printHeader(false);
            // Wait 1.5 seconds to continue so that player can read screen
            TextHandler.wait(1500);
        }

        // Ok so someone died.
        if (player.isDead())
        {
            Mediator.sendToLog(GameEvent.PLAYERLOSE.text);
            handleEvent(this, GameEvent.PLAYERLOSE);
        }
        else
        {
            Mediator.sendToLog(GameEvent.PLAYERWIN.text);
            handleEvent(this, GameEvent.PLAYERWIN);
        }
    }
    // Helper method to print enemy stats and menu etc...
    // Removes duplicate code
    private void battleMenu(Entity enemy)
    {
        //System.out.println(enemy.getHealthBar());
        Mediator.updateEnemy(enemy);
        // Put some space
        System.out.print("\n\n");

        // Show player stats and make sure they are not defending
        //System.out.println(player.getHealthBar());
        //System.out.println(player.getMagicBar());
        Mediator.updatePlayer(player);
        player.isDefending = false;

        // Now the menu
        Mediator.sendToLog("What will you do?");
        Mediator.sendToLog("1 - Fight!");
        Mediator.sendToLog("2 - Defend!");
        Mediator.sendToLog("3 - Heal! (20 MP)");
        Mediator.sendToLog(String.format("4 - Magic!(%d MP)", player.getSpell().cost));
        Mediator.sendToLog("5 - Nothing!"); // The shell has spoken!
        Mediator.sendToLog("6 - Surrender..");
    }

    // Helper method to remove duplicated code
    // Returns true if the player doesn't do anything that changes the gamestate
    // i.e. heal with insufficient mana
    private void playerAction(int choice, Entity e) throws InsufficientManaException, ResetBattleLoopException
    {
        // PLAYER ACTION SECTION
        // Make sure no defense is applied
        player.isDefending = false;

        switch (choice)
        {
            case 1:
                Mediator.sendToLog("You attack!");
                Mediator.sendToLog("Dealt " + player.getWeapon().use(e) + " damage!");
                Mediator.updateEnemy(e);
                break;
            case 2:
                player.isDefending = true;
                break;
            case 3:
                int amount = player.heal(); // Throws exception if not enough mana
                Mediator.sendToLog(String.format("You healed %d health!\n", amount));
                Mediator.updatePlayer(player);
                break;
            case 4:
                // Check if player has enough mana
                if (player.getSpell().cost > player.getMP())
                {
                    throw new InsufficientManaException("You don't have enough mana to cast this!");
                }
                else
                {
                    // Cast spell
                    Mediator.sendToLog("You cast " + player.getSpell().name + "!");
                    // Check for failure
                    float hit = new Random().nextFloat(0, 1);
                    if (hit < player.getSpell().failrate)
                    {
                        Mediator.sendToLog("Ah whoops.. You should've paid more attention in wizard school!");
                        TextHandler.wait(1000);
                        Mediator.sendToLog("Took " + player.getSpell().use(player) + " damage");
                    }
                    else
                    {
                        Mediator.sendToLog("Casting spell!");
                        Mediator.sendToLog("Dealt " + player.getSpell().use(e) + " damage");
                        player.setMP(player.getMP() - player.getSpell().cost); // Subtract mana
                    }
                    Mediator.updateEnemy(e);
                    Mediator.updatePlayer(player);
                }
            case 5: // Why did i include this option it's useless
                break;
            case 6:
                player.takeDamage(100000, DamageType.PURE);
                Mediator.sendToLog("You surrender!");
                Mediator.updatePlayer(player);
                throw new ResetBattleLoopException();
        }
    }

    // Do i need a new event just to run this?
    public void showStats()
    {
        System.out.println(player.getHealthBar());
        System.out.println(player.getMagicBar());
        System.out.println("DEF: " + player.getDEF());
        System.out.println("RES: " + player.getRES());
        System.out.println("Level: " + player.getLevel());

        TextHandler.wait(6000);
    }

    // Prints start or end of turn headers
    private void printHeader(boolean flag)
    {
        if(flag)
        {
            Mediator.sendToLog("===========================");
            Mediator.sendToLog("==     START OF TURN     ==");
            Mediator.sendToLog("===========================");
        }
        else
        {
            Mediator.sendToLog("===========================");
            Mediator.sendToLog("==      END OF TURN      ==");
            Mediator.sendToLog("===========================");
        }

        Mediator.logBreak();
    }

    // Tells main function whether a player is loaded or not
    public boolean isReady()
    {
        return playerLoaded;
    }
}
