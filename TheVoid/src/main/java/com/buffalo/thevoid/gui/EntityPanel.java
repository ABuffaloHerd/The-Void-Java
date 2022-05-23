package com.buffalo.thevoid.gui;

import com.buffalo.thevoid.entity.Entity;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class EntityPanel extends JPanel
{
    protected @Getter JLabel DEFLabel, RESLabel;

    protected @Getter JTextField hpField;

    public EntityPanel(String title)
    {
        setBorder(BorderFactory.createTitledBorder(title));

        GridBagLayout layout = new GridBagLayout();

        setLayout(layout);
        //defense and resistance labels
        DEFLabel = new JLabel("");
        RESLabel = new JLabel("");

        // hp field first
        hpField = new JTextField(50);
        hpField.setFont(new Font("Consolas", Font.PLAIN, 16));
        hpField.setBackground(Color.BLACK);
        hpField.setForeground(Color.GREEN);
        hpField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.GRAY));
        hpField.setEditable(false);

        privateAdd();

        updateDisplay(null);
    }

    // Extracted method so that constructor can be reused.
    private void privateAdd()
    {
        GridBagConstraints constraints = new GridBagConstraints();

        addComponent(hpField, 0, 0, 2, 1);
        addComponent(DEFLabel, 0, 1, 1, 1);
        addComponent(RESLabel, 1, 1, 1, 1);
    }

    /**
     * Adds a component to the panel. Assuming you're using a GridBagLayout.
     * @param component The component to add.
     * @param x The x coordinate of the component.
     * @param y The y coordinate of the component.
     * @param spanX The number of columns the component should span.
     * @param spanY The number of rows the component should span.
     */
    protected void addComponent(Component component, int x, int y, Integer spanX, Integer spanY)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = (spanX == null) ? 1 : spanX;
        c.gridheight = (spanY == null) ? 1 : spanY;

        add(component, c);
    }

    public void updateDisplay(Entity entity)
    {
        if (entity == null)
        {
            hpField.setText("┬┴┬┴┤ ͜ʖ ͡°) ├┬┴┬┴i see you");
            DEFLabel.setText("DEF: -0");
            RESLabel.setText("RES: -0");
            return;
        }
        hpField.setText(entity.getHealthBar().toString());
        DEFLabel.setText("DEF: " + entity.getDEF());
        RESLabel.setText("RES: " + entity.getRES());
    }
}
