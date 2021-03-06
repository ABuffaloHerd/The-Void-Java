package com.buffalo.thevoid.gui;

import com.buffalo.thevoid.event.IEventHandler;
import com.buffalo.thevoid.event.IEventPublisher;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class LogPanel extends JPanel implements IEventHandler<String>, IEventPublisher
{
    private final JTextArea log;
    private final Queue<LogEntry> queue; // Keeps track of the log entries
    private final int maxEntries; // The maximum number of entries to keep

    // Event publisher code
    private final Set<IEventHandler<Integer>> subscribers = new HashSet<>();

    public LogPanel()
    {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Border
        setBorder(BorderFactory.createTitledBorder("Log"));

        this.log = new JTextArea();
        this.queue = new LinkedList<>();
        this.maxEntries = 70;

        // LogPanel settings
        log.setEditable(false);
        log.setBackground(Color.gray);
        log.setForeground(Color.black);
        log.setLineWrap(true);
        log.setColumns(50);
        log.setVisible(true);
        log.setBorder(BorderFactory.createLineBorder(Color.black));

        // Using whatever means necessary to stop the log area from resizing itself.
        JScrollPane scroll = new JScrollPane(log);
        scroll.setPreferredSize(new Dimension(400, 700));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createLineBorder(Color.black));

        // autoscroll
        DefaultCaret caret = (DefaultCaret)log.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        clear(); // Make sure log is empty.

        // Add the text area to the panel
        super.add(scroll);
    }

    public void clear()
    {
        // To make the listbox empty, stuff the whole thing with empty messages
        // This also means stuff the queue with empty messages
        queue.clear();
        for (int i = 0; i < maxEntries; i++)
        {
            queue.add(new LogEntry(""));
        }
        updateText();
    }

    protected void write(String message)
    {
        // Add the new log entry to the queue
        queue.add(new LogEntry(message));
        // Pop the oldest entry off the queue if we have too many
        if(queue.size() > maxEntries)
            queue.poll();
        // Update the text
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

    protected void disableInput()
    {
        //input.setEnabled(false);
    }

    protected void enableInput()
    {
        //input.setEnabled(true);
    }

    // TODO: Check for safe removal
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
        if(sender == null)
        {
            write(args);
        }
        else
        {
            write(sender.getClass().getSimpleName() + ": " + args);
        }
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