package com.buffalo.thevoid.gui;

import com.buffalo.thevoid.entity.Player;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class PlayerPanel extends EntityPanel
{
    private final JTextField mpField;
    private final JLabel levelLabel;

    public PlayerPanel(String title)
    {
        super(title);

        // MP bar
        mpField = new JTextField(hpField.getColumns()); // copy the width of the hp field
        mpField.setBackground(Color.black);
        mpField.setForeground(Color.cyan);
        mpField.setFont(new Font("Consolas", Font.PLAIN, 16));
        mpField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.MAGENTA));
        mpField.setEditable(false);
        mpField.setVisible(true);

        // Level label
        levelLabel = new JLabel("Level: ");

        // Inherited fields from EntityPanel
        addComponent(this.hpField, 0, 0, 2, 1);
        addComponent(DEFLabel, 0, 2, 1, 1);
        addComponent(RESLabel, 1, 2, 1, 1);

        // Add the mp field and level label
        addComponent(mpField, 0, 1, 2, 1);
        addComponent(levelLabel, 0, 3, 1, 1);

        // TODO: add image + labels for weapon and spell
    }

    public void updateDisplay(Player p)
    {
        super.updateDisplay(p);
        mpField.setText(p.getMagicBar().toString());
        levelLabel.setText("Level: " + p.getLevel());
    }

}
