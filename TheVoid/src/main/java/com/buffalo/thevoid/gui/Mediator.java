package com.buffalo.thevoid.gui;

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
    // This keeps the instances private to each class that needs it.
    private static @Getter @Setter MainFrame mainFrame;
    private static @Getter @Setter Program program;
    private static @Getter @Setter GameManager gameManager;

    public Mediator(MainFrame mainFrame, Program program, GameManager gameManager)
    {
        Mediator.mainFrame = mainFrame;
        Mediator.program = program;
        Mediator.gameManager = gameManager;
    }

    public Mediator() {}
}
