package com.buffalo.thevoid.gui;

import com.buffalo.thevoid.entity.Player;

import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends EntityPanel
{
    private final JTextField mpField;

    public PlayerPanel(String title)
    {
        super(title);
        mpField = new JTextField(hpField.getColumns()); // copy the width of the hp field
        mpField.setBackground(Color.black);
        mpField.setForeground(Color.cyan);
        mpField.setFont(new Font("Consolas", Font.PLAIN, 16));
        mpField.setEditable(false);
        mpField.setVisible(true);

        // Inherited fields from EntityPanel
        addComponent(this.hpField, 0, 0, 2, 1);
        addComponent(DEFLabel, 0, 2, 1, 1);
        addComponent(RESLabel, 1, 2, 1, 1);

        // Add the mp field
        addComponent(mpField, 0, 1, 2, 1);

        // TODO: add image + labels for weapon and spell
    }

    public void updateDisplay(Player p)
    {
        super.updateDisplay(p);
        mpField.setText(Integer.toString(p.getMP()));
    }

}
