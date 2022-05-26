package com.buffalo.thevoid.gui;

import com.buffalo.thevoid.entity.Entity;
import com.buffalo.thevoid.entity.Player;
import com.buffalo.thevoid.main.GameManager;
import com.buffalo.thevoid.main.Program;
import lombok.Getter;
import lombok.Setter;

/**
 * The Mediator class is used to communicate between the GUI and the game
 */
public class Mediator
{
    // Only one of each of these is allowed. Shared between all instances of this class.
    // Run constructor only once. Use blank constructor to make a new instance that shares the data.
    // This keeps the instances private and only exposes the methods used to communicate between the classes.
    private static @Getter @Setter MainFrame mainFrame;
    private static @Getter @Setter Program program;
    private static @Getter @Setter GameManager gameManager;

    public Mediator(MainFrame mainFrame, Program program, GameManager gameManager)
    {
        Mediator.mainFrame = mainFrame;
        Mediator.program = program;
        Mediator.gameManager = gameManager;
    }

    public Mediator()
    {

    }

    public static void clearLog()
    {
        mainFrame.logPanel.clear();
    }

    public static void sendToLog(Object sender, String message)
    {
        mainFrame.logPanel.handleEvent(sender, message);
    }

    public static void sendToLog(String message)
    {
        mainFrame.logPanel.handleEvent(null, message);
    }

    public static void logBreak(int count)
    {
        for(int i = 0; i < count; i++)
        {
            mainFrame.logPanel.handleEvent(null, "");
        }
    }

    public static void logBreak()
    {
        mainFrame.logPanel.handleEvent(null, "");
    }

    //******************************************************************************************************************
    // Control methods
    //******************************************************************************************************************
    public static void disableButtons()
    {
        mainFrame.buttonPanel.disableAllButtons();
    }

    public static void enableButtons()
    {
        mainFrame.buttonPanel.enableAllButtons();
    }

    public static void disableTextFields()
    {
        mainFrame.logPanel.disableInput();
    }

    public static void enableTextFields()
    {
        mainFrame.logPanel.enableInput();
    }

    public static void setButtons(int count)
    {
        mainFrame.buttonPanel.rebuild(count);
        mainFrame.invalidate();
        mainFrame.repaint();
    }
    //******************************************************************************************************************
    // Display methods
    //******************************************************************************************************************

    public static void updatePlayer(Player p)
    {
        mainFrame.playerPanel.updateDisplay(p);
    }

    public static void updateEnemy(Entity e)
    {
        mainFrame.entityPanel.updateDisplay(e);
    }

}
