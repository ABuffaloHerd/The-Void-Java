package com.buffalo.thevoid.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame
{
    public final JPanel mainPanel;
    public final LogPanel logPanel;

    private final Mediator mediator;

    public MainFrame()
    {
        super("The Void");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);

        // Construct main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        logPanel = new LogPanel();
        logPanel.setPreferredSize(new Dimension(400, 768));

        // Add managing main panel to frame
        mainPanel.add(logPanel, BorderLayout.EAST);
        add(mainPanel);

        // Construct mediator
        mediator = new Mediator();

        // Register the program with the log panel
        // Uses static reference rather than instance reference.
        logPanel.addEventHandler(Mediator.getProgram());

        mainPanel.setVisible(true);
        logPanel.setVisible(true);

        pack();
        setVisible(true);
    }
}
