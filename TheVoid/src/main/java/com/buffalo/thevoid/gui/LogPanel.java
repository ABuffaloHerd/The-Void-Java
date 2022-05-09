package com.buffalo.thevoid.gui;

import com.buffalo.thevoid.event.IEventHandler;
import com.buffalo.thevoid.event.IEventPublisher;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class LogPanel extends JPanel implements IEventHandler<String>, IEventPublisher
{
    private final JTextArea log;
    private final JTextField input; // Used to enter a command. Saves time and code.
    private final Queue<LogEntry> queue; // Keeps track of the log entries
    private final int maxEntries; // The maximum number of entries to keep

    // Event publisher code
    private final Set<IEventHandler<Integer>> subscribers = new HashSet<>();

    public LogPanel()
    {
        super(new BorderLayout());

        // Border
        setBorder(BorderFactory.createTitledBorder("Log"));

        this.log = new JTextArea(25, 25);
        this.queue = new LinkedList<>();
        this.input = new JTextField();
        this.maxEntries = 43;

        // LogPanel settings
        log.setEditable(false);
        log.setBackground(Color.white);
        log.setVisible(true);
        log.setBorder(BorderFactory.createLineBorder(Color.black));

        // Input settings
        input.setColumns(25);
        input.setEditable(true);
        input.setBackground(Color.white);
        input.setVisible(true);

        // Action listener for the input field
        input.addActionListener(e ->
        {
            // Send the command to program using raise
            try
            {
                raise(Integer.parseInt(input.getText()));
            }
            catch(NumberFormatException ex)
            {
                raise(-1);
                write("Input an integer.");
            }

            // Clear the input field
            input.setText("");
        });

        // To make the listbox empty, stuff the whole thing with empty messages
        // This also means stuff the queue with empty messages
        for (int i = 0; i < maxEntries; i++)
        {
            queue.add(new LogEntry(Integer.toString(i)));
        }

        updateText();

        // Add the text area to the panel
        super.add(log, BorderLayout.NORTH);
        super.add(input, BorderLayout.SOUTH);
    }

    private void write(String message)
    {
        // Add the message to the queue
        queue.add(new LogEntry(message));
        queue.poll();
        updateText();
    }

    private void updateText()
    {
        log.setText("");
        for(var entry : queue)
        {
            log.append(entry.message() + "\n");
        }
    }

    public void disableInput()
    {
        input.setEnabled(false);
    }

    public void enableInput()
    {
        input.setEnabled(true);
    }

    private void raise(Integer args)
    {
        for(var subscriber : subscribers)
        {
            subscriber.handleEvent(this, args);
        }
    }

    @Override
    public void handleEvent(Object sender, String args)
    {
        // Add the new log entry to the queue
        queue.add(new LogEntry(args));
        // Pop the oldest entry off the queue if we have too many
        if(queue.size() > maxEntries)
            queue.poll();
        // Update the text
        updateText();
    }

    @Override
    public void addEventHandler(IEventHandler<?> e)
    {
        subscribers.add((IEventHandler<Integer>) e);
    }

    @Override
    public void removeEventHandler(IEventHandler<?> f)
    {
        subscribers.remove(f);
    }

    @Override
    public String toString()
    {
        return "LogPanel";
    }
}

record LogEntry(String message)
{
}