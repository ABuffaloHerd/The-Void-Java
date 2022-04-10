package com.buffalo.thevoid.main;

import com.buffalo.thevoid.entity.*;
import com.buffalo.thevoid.equipment.WeaponList;
import com.buffalo.thevoid.event.GameEvent;
import com.buffalo.thevoid.event.IEventHandler;
import com.buffalo.thevoid.factory.BossList;
import com.buffalo.thevoid.factory.MonsterFactory;
import com.buffalo.thevoid.io.ConsoleColours;
import com.buffalo.thevoid.io.FileHandler;
import com.buffalo.thevoid.io.TextHandler;

/**
 * This bad boy contains all game functions, while the actual loop is handled by the main function.
 */
public class GameManager implements IEventHandler<GameEvent>
{
    // NOTE: objects are pass by reference
    private Player player;
    private boolean playerLoaded;

    // calls methods to handle game events
    // program flow returns to main function after this.
    @Override
    public void handleEvent(Object sender, GameEvent args)
    {
        // If this calls a method then the switch block is not responsible for waiting.
        switch (args)
        {
            case NEWGAME ->
                    {
                        player = NewGame.newPlayer();
                        playerLoaded = true;
                    }
            case LOADGAME ->
                    {
                        player = NewGame.loadPlayer();
                        playerLoaded = true;
                        TextHandler.wait(1800);
                    }
            case SAVEGAME -> FileHandler.savePlayer(player);
            case SHOP -> shop();
            case RESUPPLY ->
                    { // resupply method
                        player.refreshStats();
                        System.out.println("Your stats have been replenished!");
                        TextHandler.wait(1500);
                    }
            case BATTLE -> battle();
            case BOSS -> bossBattle();
            case EXBOSS -> finalBoss();
            case LEVELUP ->
                    { // This is short enough to include the wait.
                        System.out.println("New weapon unlocked!");
                        WeaponList.unlockWeapon();
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
                            handleEvent(this, GameEvent.LEVELUP);
                        }
                        TextHandler.wait(1000); // No auto resupply
                    }
            case PLAYERLOSE ->
                    {
                        System.out.println("You have DIED!");
                        System.out.println("ggez you suck");
                        System.out.println("get rekt");
                        System.out.println("go touch some grass");
                        System.out.println("so many nubs in this game");
                        TextHandler.wait(3000);
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

        System.out.printf("%sInventory%s\n\n", ConsoleColours.ANSI_GREEN_BACKGROUND, ConsoleColours.TEXT_RESET);
        System.out.printf("Currently equipped: %s%s%s\n", ConsoleColours.TEXT_BLUE, player.getWeapon().name, ConsoleColours.TEXT_RESET);
        System.out.println("The following weapons are available:");
        System.out.printf("%sIndex - Name%s\n", ConsoleColours.TEXT_GREEN, ConsoleColours.TEXT_RESET);

        for (var set : WeaponList.WeaponData)
        {
            if (set.getItem2()) // If it is unlocked
            {
                System.out.printf("%d - %s | Level required: %d\n", counter, set.getItem1().name, set.getItem1().levelRequired);
                unlocked++;
                counter++;
            }
        }

        // Uhh due to an oversight, if the weapons are not unlocked in order then the weapon equipped will not
        // correspond to the actual weapon index.
        // However, this shouldn't happen to begin with so i'm going to ignore it.
        int selection = TextHandler.validInt("Choose a weapon via index.", unlocked - 1, 0);

        // Check level
        if (WeaponList.WeaponData.get(selection).getItem1().levelRequired > player.getLevel())
            System.out.println("Your level is too low!");
        else
        {
            player.setWeapon(WeaponList.WeaponData.get(selection).getItem1()); // Set weapon
            System.out.printf("Equipped %s%s%s!\n", ConsoleColours.TEXT_BLUE, player.getWeapon().name, ConsoleColours.TEXT_RESET);
        }

        TextHandler.wait(1000);
    }

    /**
     * Battle a generic enemy.
     * THIS IS WHAT NEEDS TO HAPPEN:
     * 1. Generate a random enemy
     * 2. Battle
     * 3. If you win, increase killcount (handed over to the handleEvent method)
     * 4. If you lose, DIE
     * 6. After winning, evaluate if you should level up
     */
    private void battle()
    {
        TextHandler.clearConsole();
        Enemy e = MonsterFactory.generateEnemy(player.getLevel());

        System.out.println("+-----------------------------------------------------+");
        System.out.printf("%s%s has appeared!%s\n", ConsoleColours.TEXT_RED, e.name, ConsoleColours.TEXT_RESET);
        System.out.println("+-----------------------------------------------------+");
        while (!player.isDead() && !e.isDead())
        {
            printHeader(true);

            battleMenu(e);
            int choice = TextHandler.validInt("Enter number to choose action.", 4, 1);

            // PLAYER ACTION SECTION
            if(playerAction(choice, e))
                continue;

            if (e.isDead()) // If the enemy is dead reevaluate the loop
                continue;

            // ENEMY ACTION SECTION
            // This is just a dummy that does nothing but attack so...
            System.out.printf("%s is attacking!\n", e.name);
            System.out.println("Took " + e.attack(player) + " damage!");

            printHeader(false);

            // No need to check if player died because the loop restarts at this point.
            // Wait 1.5 seconds to continue so that player can read screen
            TextHandler.wait(1500);
        }

        // Once we break the loop, check who died
        if (player.isDead())
        {
            System.out.println(GameEvent.PLAYERLOSE.text);
            handleEvent(this, GameEvent.PLAYERLOSE);
        }
        else // THERE CAN BE ONLY ONE
        {
            System.out.println(GameEvent.PLAYERWIN.text);
            handleEvent(this, GameEvent.PLAYERWIN);
        }
    }

    /**
     * Boss battle
     * This is what needs to happen:
     * 1. Show bosslist
     * 1.5 Pick a boss, make sure level is suitable.
     * 2. Battle
     * 3. If you win, increase killcount by 5, then check level (handled by handleEvent)
     * 4. If you lose, DIE
     * Might be able to cram some of the above battle method into its own section.
     */
    private void bossBattle()
    {
        int toSpecial = 0; // When this hits 5, the boss unleashes special attack, probably vaporising the player
        TextHandler.clearConsole();

        // Show bosslist
        System.out.println("+-----------------------------------------------------+");
        System.out.printf("|%s%s%s|\n", ConsoleColours.TEXT_BLUE, "Bosses", ConsoleColours.TEXT_RESET);
        System.out.println("+-----------------------------------------------------+");

        int counter = 0; // Need this to keep track of which boss we are on
        for(var x : BossList.BossData)
        {
            System.out.printf("%d - %s [Defeated: %b] Level req: %d\n", counter, x.getItem1().name, x.getItem2(), x.getItem3());
            counter++;
        }

        // Pick a boss, make sure level matches
        int bossSelected = TextHandler.validInt("Enter number to choose boss.", counter, 0);
        if(BossList.BossData.get(bossSelected).getItem3() > player.getLevel())
        {
            System.out.println("Your level is too low to fight this boss! (You'll explode if you try)");
            TextHandler.wait(2000);
            return;
        }

        // Battle
        Boss boss = BossList.BossData.get(bossSelected).getItem1(); // Select boss
        System.out.printf("You have chosen %s!\n", boss.name);
        TextHandler.wait(2000);

        while(!boss.isDead() && !player.isDead())
        {
            printHeader(true);
            // Boss quote first
            System.out.printf("%s%s%s\n", ConsoleColours.TEXT_BLUE, boss.randomQuote(), ConsoleColours.TEXT_RESET);
            TextHandler.wait(1000);

            battleMenu(boss);
            int choice = TextHandler.validInt("Enter number to choose action.", 4, 1); // reuse the choice variable

            // PLAYER ACTION SECTION
            if(playerAction(choice, boss))
                continue;

            if (boss.isDead()) // If the enemy is dead after the player's action reevaluate the loop
                continue;

            // ENEMY ACTION SECTION
            // Right the boss is going to attack UNLESS the special attack counter is 5.
            if (toSpecial == 5)
            {
                System.out.printf("%s is unleashing a special attack!\nThis is going to hurt...\n", boss.name);
                TextHandler.wait(1000);

                // Do the special attack
                System.out.println("Took " + boss.specialAttack.apply(player) + " damage!");

                // Reset the counter
                toSpecial = 0;
            }
            else
            {
                // Just a regular attack :((
                System.out.printf("%s is attacking!\n", boss.name);
                TextHandler.wait(1000);

                System.out.println("Took " + boss.attack.apply(player) + " damage!");

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
            System.out.println(GameEvent.PLAYERWIN.text);
            if (BossList.BossData.get(bossSelected).getItem2())
            {
                System.out.println("You have already defeated this boss!");
            }
            else
            {   // One time reward of 5 kills
                System.out.println("You have defeated this boss for the first time!");
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
        System.out.println("+-----------------------------------------------------+");
        System.out.printf("|%s%s%s|\n", ConsoleColours.TEXT_BLUE, "Final Boss", ConsoleColours.TEXT_RESET);
        System.out.println("+-----------------------------------------------------+");

        TextHandler.wait(2000);
        System.out.println("The ground shakes violently.");
        TextHandler.wait(2000);
        System.out.println("A small, dark figure appears in the center of the room.");
        TextHandler.wait(2000);
        System.out.println("The room bursts into flames.");

        // Battle
        ExBoss boss = BossList.Hirina;
        while(!boss.isDead() && !player.isDead())
        {
            printHeader(true);

            // Boss quote first
            System.out.printf("%s%s%s\n", ConsoleColours.TEXT_BLUE, boss.randomQuote(), ConsoleColours.TEXT_RESET);
            TextHandler.wait(1000);

            battleMenu(boss);
            int choice = TextHandler.validInt("Enter number to choose action.", 4, 1); // reuse the choice variable

            // PLAYER ACTION SECTION
            if(playerAction(choice, boss))
                continue;

            if (boss.isDead()) // If the enemy is dead reevaluate the loop
                continue;

            // ENEMY ACTION SECTION
            // Right the boss is going to attack UNLESS the special attack counter is 5.
            if (toSpecial == 5)
            {
                System.out.printf("%sPhase 2: %s is unleashing a special attack!%s\nThis is going to hurt...\n", ConsoleColours.TEXT_YELLOW, boss.name, ConsoleColours.TEXT_RESET);
                TextHandler.wait(1000);

                // Do the special attack
                System.out.println("Took " + boss.specialAttack.apply(player) + " damage!");

                // Reset the counter
            }
            else if (toSpecial == 10)
            {
                // second special attack
                System.out.printf("%sPhase 3: %s is unleashing a special attack!%s\nThis is going to hurt...\n", ConsoleColours.TEXT_YELLOW, boss.name, ConsoleColours.TEXT_RESET);
                TextHandler.wait(1000);

                // Do the special attack
                System.out.println("Took " + boss.specialAttack2.apply(player) + " damage!");
            }
            else if (toSpecial == 15 || toSpecial > 20)
            {
                // third special attack
                System.out.printf("%sPhase 4: %s is unleashing a special attack!%s\nThis is going to hurt...\n", ConsoleColours.TEXT_YELLOW, boss.name, ConsoleColours.TEXT_RESET);
                TextHandler.wait(1000);

                // Do the special attack
                System.out.println("Took " + boss.specialAttack3.apply(player) + " damage!");
            }
            else
            {
                // Just a regular attack :((
                System.out.printf("%s is attacking!\n", boss.name);
                TextHandler.wait(1000);

                System.out.println("Took " + boss.attack.apply(player) + " damage!");

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
            System.out.println(GameEvent.PLAYERLOSE.text);
            handleEvent(this, GameEvent.PLAYERLOSE);
        }
        else
        {
            System.out.println(GameEvent.PLAYERWIN.text);
            handleEvent(this, GameEvent.PLAYERWIN);
        }
    }
    // Helper method to print enemy stats and menu etc...
    // Removes duplicate code
    private void battleMenu(Entity enemy)
    {
        System.out.println(enemy.getHealthBar());
        // Put some space
        System.out.print("\n\n");

        // Show player stats and make sure they are not defending
        System.out.println(player.getHealthBar());
        System.out.println(player.getMagicBar());
        player.isDefending = false;

        // Now the menu
        System.out.println("What will you do?");
        System.out.println("1 - Fight!");
        System.out.println("2 - Defend!");
        System.out.println("3 - Heal!");
        System.out.println("4 - Nothing!"); // The shell has spoken!
    }

    // Helper method to remove duplicated code
    // Returns true if the player doesn't do anything that changes the gamestate
    // i.e heal with insufficient mana
    private boolean playerAction(int choice, Entity e)
    {
        // PLAYER ACTION SECTION
        switch (choice)
        {
            case 1:
                System.out.println("You attack!");
                System.out.println("Dealt " + player.getWeapon().attackMethod.apply(e) + " damage!");
                break;
            case 2:
                player.isDefending = true;
                break;
            case 3:
                int amount = player.heal();
                if (amount == 0)
                {
                    System.out.println("You don't have enough mana!");
                    return true;
                }
                else
                {
                    System.out.printf("You healed %d health!\n", amount);
                }
            case 4: // Why did i include this option it's useless
                break;
        }

        return false;
    }

    // Do i need a new event just to run this?
    public void showStats()
    {
        System.out.println(player.getHealthBar());
        System.out.println(player.getMagicBar());
        System.out.println("DEF: " + player.getDEF());
        System.out.println("RES: " + player.getRES());
        System.out.println("Level: " + player.getLevel());

        TextHandler.wait(3000);
    }

    // Prints start or end of turn headers
    private void printHeader(boolean flag)
    {
        if(flag)
        {
            System.out.println("===========================");
            System.out.println("==    START OF TURN     ==");
            System.out.println("===========================");
        }
        else
        {
            System.out.println("===========================");
            System.out.println("==    END OF TURN       ==");
            System.out.println("===========================");
        }

        System.out.println("\n");
    }

    // Tells main function whether a player is loaded or not
    public boolean isReady()
    {
        return playerLoaded;
    }
}
