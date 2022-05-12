package com.buffalo.thevoid.gui;

import com.buffalo.thevoid.entity.Entity;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class EntityPanel extends JPanel
{
    protected @Getter JLabel DEFLabel, RESLabel;

    protected @Getter JTextField hpField;

    public EntityPanel()
    {
        setLayout(new GridBagLayout());

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        GridBagConstraints labelConstraints = new GridBagConstraints();

        fieldConstraints.gridheight = 2;
        fieldConstraints.gridwidth = 2;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.weightx = 1;
        fieldConstraints.weighty = 0.0;
        fieldConstraints.gridx = 0;
        fieldConstraints.gridy = 0;

        labelConstraints.gridheight = 2;
        labelConstraints.gridwidth = 2;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.weightx = 0.5;
        labelConstraints.weighty = 0.0;

        // Insert the hp field first
        hpField = new JTextField(50);
        hpField.setBackground(Color.BLACK);
        hpField.setForeground(Color.GREEN);
        hpField.setEditable(false);
        add(hpField, fieldConstraints);

        // Then the defense and resistance labels
        DEFLabel = new JLabel("DEF: ");
        RESLabel = new JLabel("RES: ");

        labelConstraints.gridx = 0;
        labelConstraints.gridy = 1;
        add(DEFLabel, labelConstraints);

        labelConstraints.gridx = 1;
        add(RESLabel, labelConstraints);
    }

    public void update(Entity entity)
    {
        hpField.setText(entity.getHealthBar().toString());
        DEFLabel.setText("DEF: " + entity.getDEF());
        RESLabel.setText("RES: " + entity.getRES());
    }
}
