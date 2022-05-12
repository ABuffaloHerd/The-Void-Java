package com.buffalo.thevoid.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame
{
    public final JPanel mainPanel;
    public final LogPanel logPanel;
    public final EntityPanel entityPanel;
    private final JPanel controlPanel;

    private final Mediator mediator;

    public MainFrame()
    {
        super("The Void");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);

        // Construct logging panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        logPanel = new LogPanel();
        logPanel.setPreferredSize(new Dimension(400, 768));

        // Add managing main panel to frame
        mainPanel.add(logPanel, BorderLayout.EAST);
        add(mainPanel);

        // Construct mediator
        mediator = new Mediator();
        Mediator.setMainFrame(this); // set static reference to this.

        // Register the program with the log panel
        // Uses static reference rather than instance reference.
        // Can remove. Just check first.
        // TODO: check for safe removal
        logPanel.addEventHandler(Mediator.getProgram());

        // Create a display panel
        // This panel will be used to display three other panels
        // 1. Enemy panel
        // 2. Player panel
        // 3. Quick access buttons. These map to integers 1 - 6 and are used to control battle.
        // They perform the same function as typing the number in the log panel.
        controlPanel = new JPanel(new GridLayout(3, 1));
        entityPanel = new EntityPanel();
        controlPanel.add(entityPanel);

        // Add control panel to main panel
        mainPanel.add(controlPanel, BorderLayout.CENTER);

        mainPanel.setVisible(true);
        logPanel.setVisible(true);
        controlPanel.setVisible(true);

        pack();
        setVisible(true);
    }
}
