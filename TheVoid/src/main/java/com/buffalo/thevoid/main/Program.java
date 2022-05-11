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

public class Program implements IEventPublisher, IEventHandler<Integer>
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
    private static Mediator mediator;

    // Set up mediator
    static
    {
        mediator = new Mediator();
    }

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

        // Register gui
        final MainFrame[] mainframe = new MainFrame[1];

        mainframe[0] = new MainFrame();

        // Thread the gui.
        Thread guiThread = new Thread(() ->
        {
            mainframe[0] = new MainFrame();
        });
        guiThread.start();

        // waiting the main thread allows the gui to run and keep the reference.
        // WIthout this the reference is null and the console is filled with red text.
        TextHandler.wait(100);

        // Set up remaining references in mediator.
        Mediator.setMainFrame(mainframe[0]);

        // Register the game event handlers
        program.addEventHandler(manager);

        // Register the log event handler
        program.logOutputHandler.add(LogEventHandler.Logger);
        program.logOutputHandler.add(Mediator.getMainFrame().logPanel);

        // Register this program as the event handler for the logPanel
        // This will allow input from logPanel to be passed in here.
//        mainframe.logPanel.addEventHandler(program);
        // MOVED TO MAINFRAME CONSTRUCTOR

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
            System.out.println("Welcome to the void.\nPlease make a selection by entering the number associated with the option.");
            System.out.println("1 - " + GameEvent.NEWGAME.text + " - RESETS EVERYTHING AND STARTS OVER");
            System.out.println("2 - " + GameEvent.LOADGAME.text + " - Attempt to load a saved player");
            System.out.println("3 - " + GameEvent.SAVEGAME.text + " - Save the current player");
            System.out.println("4 - " + GameEvent.BATTLE.text + " - Fight to the death.");
            System.out.println("5 - " + GameEvent.SHOP.text + " - Change weapons.");
            System.out.println("6 - " + GameEvent.BOSS.text + " - Fight a boss.");
            System.out.println("7 - " + GameEvent.EXBOSS.text + " - Fight the final boss.");
            System.out.println("8 - " + GameEvent.RESUPPLY.text + " - Refill your health and mana.");
            System.out.println("9 - Show Stats");
            System.out.println("10 - How to play");
            System.out.println("11 - " + GameEvent.EXIT.text + " - Exit the game. Progress not saved~");

            selection = TextHandler.validInt("", 11, 1);

            // Can't play without a player you clown.
            if(selection > 2 && !manager.isReady())
            {
                System.out.printf("%sYou must create a player first.\n", ConsoleColours.TEXT_RED);
                System.out.printf("Select new game or load game to get started.%s", ConsoleColours.TEXT_RESET);
                program.raise("You must create a player first.");
                program.raise("Select new game or load game to get started.");
                TextHandler.wait(1500);
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
            program.raise("You must create a player first.");
            program.raise("Select new game or load game to get started.");
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
        System.out.println("Welcome to the void, a game about beating the crap out of enemies.");
        System.out.println("Choose the battle option to instigate a fight with common monsters.");
        System.out.println("Win this fight to increase your killcount. Level up every few kills. (specified in config)");
        System.out.println("Unlock a new weapon every time you level up. These weapons have level requirements however.");
        System.out.println("When you think you're ready, you may choose to fight a boss.");
        System.out.println("They have level requirements, but those are just a suggestion.");
        System.out.println("You may fight them at any level above the minimum.");
        System.out.println("Defeating a boss for the first time will grant you five kills (hardcoded)");
        System.out.println("After you've gotten bored of grinding you may choose to fight the final boss. She has no level requirement.");
        TextHandler.validInt("\n\nEnter any integer to continue.");
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

    @Override
    public void handleEvent(Object sender, Integer args)
    {
        System.out.println("Main program received event from " + sender + " with args: " + args);
        selectEvent(program, manager, args);
    }
}
