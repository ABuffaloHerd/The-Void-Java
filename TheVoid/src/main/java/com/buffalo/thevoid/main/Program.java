package com.buffalo.thevoid.main;

import com.buffalo.thevoid.equipment.SpellList;
import com.buffalo.thevoid.equipment.WeaponList;
import com.buffalo.thevoid.event.GameEvent;
import com.buffalo.thevoid.event.IEventHandler;
import com.buffalo.thevoid.event.IEventPublisher;
import com.buffalo.thevoid.factory.BossList;
import com.buffalo.thevoid.gui.MainFrame;
import com.buffalo.thevoid.gui.Mediator;
import com.buffalo.thevoid.io.ConsoleColours;
import com.buffalo.thevoid.io.LogEventHandler;
import com.buffalo.thevoid.io.TextHandler;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class Program implements IEventPublisher
{
    // The event handlers for the program
    // This handles game events
    // Use a set because no duplicates. Don't want to call handleEvent twice now do we?
    private final Set<IEventHandler<GameEvent>> gameEventHandler = new HashSet<>();

    // This handles events that are written to file.
    // Now we can use the same handler to write to the gui log.
    private final Set<IEventHandler<String>> logOutputHandler = new HashSet<>(2);

    public static Program program;
    public static GameManager manager;

    public static void main(String[] args)
    {
        // Initialize the game
        boolean gameRunning = true;
        program = new Program();
        manager = new GameManager();

        // Set up mediator
        // This is done first so that when mainframe is created, program and manager are not null
        Mediator.setProgram(program);
        Mediator.setGameManager(manager);

        // Thread gui
        SwingUtilities.invokeLater(MainFrame::new);

        // Make main thread wait for gui thread to finish
        TextHandler.wait(2000);

        // Register the game event handlers
        program.addEventHandler(manager);

        // Register the log event handler
        program.logOutputHandler.add(LogEventHandler.Logger);

        // Instantiate the boss, weapons and spells
        new BossList();
        new WeaponList();
        new SpellList();

        int selection;

        // Main menu
        // GameManager handles the creation of the player and related stuff
        // This just reports which event has been selected there
        while(gameRunning)
        {
            TextHandler.clearConsole();
            Mediator.clearLog();
            Mediator.setButtons(11);

            Mediator.updateEnemy(null);

            Mediator.sendToLog(null, "Welcome to the void.\nPlease make a selection by entering the number associated with the option.");
            Mediator.sendToLog(null, "1 - " + GameEvent.NEWGAME.text + " - RESETS EVERYTHING AND STARTS OVER");
            Mediator.sendToLog(null, "2 - " + GameEvent.LOADGAME.text + " - Attempt to load a saved player");
            Mediator.sendToLog(null, "3 - " + GameEvent.SAVEGAME.text + " - Save the current player");
            Mediator.sendToLog(null, "4 - " + GameEvent.BATTLE.text + " - Fight to the death.");
            Mediator.sendToLog(null, "5 - " + GameEvent.SHOP.text + " - Change weapons.");
            Mediator.sendToLog(null, "6 - " + GameEvent.BOSS.text + " - Fight a boss.");
            Mediator.sendToLog(null, "7 - " + GameEvent.EXBOSS.text + " - Fight the final boss.");
            Mediator.sendToLog(null, "8 - " + GameEvent.RESUPPLY.text + " - Refill your health and mana.");
            Mediator.sendToLog(null, "9 - Show Stats");
            Mediator.sendToLog(null, "10 - How to play");
            Mediator.sendToLog(null, "11 - " + GameEvent.EXIT.text + " - Exit the game. Progress not saved~");

//            selection = TextHandler.validInt("", 11, 1);
            selection = TextHandler.validInt(11, 1);

            // Can't play without a player you clown.
            if(selection > 2 && !manager.isReady())
            {
                System.out.printf("%sYou must create a player first.\n", ConsoleColours.TEXT_RED);
                System.out.printf("Select new game or load game to get started.%s", ConsoleColours.TEXT_RESET);

                Mediator.sendToLog(null, "You must create a player first.");
                Mediator.sendToLog(null, "Select new game or load game to get started.");
                Mediator.sendToLog(null, "Enter 1 to continue.");

                TextHandler.validInt(1, 1);
                continue;
            }

            gameRunning = program.selectEvent(program, manager, selection);
        }
    }

    private boolean selectEvent(Program program, GameManager manager, int selection)
    {
        // raise events, transferring program flow to the game manager
        // By extracting this method, handleEvent can also access this part of the code.

        if(selection > 2 && !manager.isReady())
        {
            System.out.printf("%sYou must create a player first.\n", ConsoleColours.TEXT_RED);
            System.out.printf("Select new game or load game to get started.%s", ConsoleColours.TEXT_RESET);
//            program.raise("You must create a player first.");
//            program.raise("Select new game or load game to get started.");
            TextHandler.wait(1500);
            return true;
        }

        switch (selection)
        {
            case 1 ->
            {
                program.raise(GameEvent.NEWGAME);
                new BossList(); // Reset the boss and weapons by instantiating them again
                new WeaponList();
                new SpellList();
            }
            case 2 -> program.raise(GameEvent.LOADGAME);
            case 3 -> program.raise(GameEvent.SAVEGAME);
            case 4 -> program.raise(GameEvent.BATTLE);
            case 5 -> program.raise(GameEvent.SHOP);
            case 6 -> program.raise(GameEvent.BOSS);
            case 7 -> program.raise(GameEvent.EXBOSS);
            case 8 -> program.raise(GameEvent.RESUPPLY);
            case 9 -> manager.showStats();
            case 10 -> help();
            case 11 -> {return false;}
        }
        return true;
    }

    private static void help()
    {
        Mediator.sendToLog("Welcome to the void, a game about beating the crap out of enemies.");
        Mediator.sendToLog("Choose the battle option to instigate a fight with common monsters.");
        Mediator.sendToLog("Win this fight to increase your killcount. Level up every few kills. (specified in config)");
        Mediator.sendToLog("Unlock a new weapon every time you level up. These weapons have level requirements however.");
        Mediator.sendToLog("When you think you're ready, you may choose to fight a boss.");
        Mediator.sendToLog("They have level requirements, but those are just a suggestion.");
        Mediator.sendToLog("You may fight them at any level above the minimum.");
        Mediator.sendToLog("Defeating a boss for the first time will grant you five kills (hardcoded)");
        Mediator.sendToLog("After you've gotten bored of grinding you may choose to fight the final boss. She has no level requirement.");
        Mediator.sendToLog("This message will self destruct in 10 seconds.");
        TextHandler.wait(10000);
    }

    // Raise event
    private void raise(GameEvent event)
    {
        // Doesn't need to be a set because it's a single handler, but it shows that it can be used for multiple handlers
        for (var m: gameEventHandler)
            m.handleEvent(this, event);
    }

    // Raise event to log output
    private void raise(String text)
    {
        for(var v: logOutputHandler)
            v.handleEvent(this, text);
    }

    @Override
    @SuppressWarnings("unchecked") // shut
    public void addEventHandler(IEventHandler<?> e)
    {
        gameEventHandler.add((IEventHandler<GameEvent>) e);
    }

    @Override
    @SuppressWarnings("unchecked") // shut
    public void removeEventHandler(IEventHandler<?> f)
    {
        gameEventHandler.add((IEventHandler<GameEvent>) f);
    }
}
