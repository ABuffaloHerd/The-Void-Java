package com.buffalo.thevoid.gui;

import com.buffalo.thevoid.io.InputQueue;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ButtonPanel extends JPanel
{
    public final ArrayList<JButton> buttons = new ArrayList<>();

    public ButtonPanel()
    {
        this(6);
    }

    public ButtonPanel(int BUTTONCOUNT)
    {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Controls"));
        constructButtons(BUTTONCOUNT);
    }

    private void constructButtons(int BUTTONCOUNT)
    {
        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 0;

        // Add buttons using loops.
        buttons.clear();

        // Button values start at 1, not 0
        for(int x = 1; x <= BUTTONCOUNT; x++)
        {
            buttons.add(new CustomButton(Integer.toString(x), x));
        }

        if(buttons.size() % 2 == 0) //even number of buttons
        {
            for(int x = 0; x < buttons.size(); x++)
            {
                c.gridwidth = 1;
                c.gridheight = 1;
                c.gridx = x % 2; // 0 or 1
                c.gridy = x / 2; // 0 - whatever
                add(buttons.get(x), c);
            }
        }
        else //odd number of buttons
        {
            for(int x = 0; x < buttons.size(); x++)
            {
                c.gridx = x % 2;
                c.gridy = x / 2;
                c.gridwidth = 1;
                c.gridheight = 1;
                add(buttons.get(x), c);

                if(x == buttons.size() - 1)
                {
                    c.gridx = 0;
                    c.gridy = x / 2;
                    c.gridwidth = 2;
                    c.gridheight = 1;
                    add(buttons.get(x), c);
                }
            }
        }
    }

    public void disableAllButtons()
    {
        for(var button : buttons)
        {
            button.setEnabled(false);
        }
    }

    public void enableAllButtons()
    {
        for(var button : buttons)
        {
            button.setEnabled(true);
        }
    }

    public void rebuild(int count)
    {
        removeAll();
        constructButtons(count);
        revalidate();
        repaint();
    }
}

class CustomButton extends JButton
{
    public CustomButton(String text, Integer value)
    {
        super(text);
        setFont(new Font("Consolas", Font.PLAIN, 12));
        setMaximumSize(new Dimension(100, 100));
        setMaximumSize(new Dimension(100, 100));
        setPreferredSize(new Dimension(100, 100));
        setMargin(new Insets(5, 70, 5, 70));

        this.addActionListener(e ->
                InputQueue.enqueue(value.toString()));
    }
}

