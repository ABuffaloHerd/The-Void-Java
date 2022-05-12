package com.buffalo.thevoid.factory;

import javax.swing.JButton;
import java.awt.event.ActionListener;

public class ButtonFactory
{
    public static JButton createButton(String text, ActionListener action)
    {
        JButton button = new JButton(text);

        button.addActionListener(action);

        return button;
    }
}
