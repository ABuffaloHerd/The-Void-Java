package com.buffalo.thevoid.gui;

import com.buffalo.thevoid.io.InputQueue;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ButtonPanel extends JPanel
{
    private final int BUTTONCOUNT = 7;
    public final ArrayList<JButton> buttons = new ArrayList<>();

    public ButtonPanel()
    {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Controls"));
        GridBagConstraints c = new GridBagConstraints();

        // Button values start at 1, not 0
        for(int x = 1; x <= BUTTONCOUNT; x++)
        {
            buttons.add(new CustomButton(Integer.toString(x), x));
        }

        if(buttons.size() % 2 == 0) //even number of buttons
        {
            for(int x = 0; x < buttons.size(); x++)
            {
                c.gridx = x % 2; // Dunno if this works
                c.gridy = x / 2;
                add(buttons.get(x), c);
            }
        }
        else //odd number of buttons
        {
            for(int x = 0; x < buttons.size(); x++)
            {
                c.gridx = x % 2;
                c.gridy = x / 2;
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
}

class CustomButton extends JButton
{
    public CustomButton(String text, Integer value)
    {
        super(text);
        setMargin(new Insets(10, 70, 10, 70));

        this.addActionListener(e ->
                InputQueue.enqueue(value.toString()));
    }
}

