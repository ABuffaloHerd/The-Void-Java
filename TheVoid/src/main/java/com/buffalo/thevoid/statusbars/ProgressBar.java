package com.buffalo.thevoid.statusbars;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class ProgressBar
{
    protected int length;
    protected char full;
    protected char empty;

    protected @Getter @Setter int max;
    protected @Getter @Setter String label;

    protected @Getter int value;
    public void setValue(int value)
    {
        if (value < 0)
        {
            this.value = 0;
        }
        else if (value > max)
        {
            this.value = max;
        }
        else
        {
            this.value = value;
        }
    }

    /**
     * Creates a new ProgressBar object. ProgressBars are exactly what you think they are.
     * @param full The character to symbolize a full cell.
     * @param empty The character to symbolize an empty cell.
     * @param label The name to put on this bar.
     * @param Max The maximum value this object can work with. Must be > 0
     * @param length How many characters long is this bar?
     */
    public ProgressBar(char full, char empty, String label, int Max, int length)
    {
        this.full = full;
        this.empty = empty;
        this.length = length;
        this.max = Max;

        this.label = label;
        this.value = Max;
    }

    @Override
    public String toString()
    {
        // Need the value of one filled space in terms of the max value
        // If I have a max of 20 and a length of 10 how much is one space?
        // 20 I 10 = 2
        // I have a value of 14, max of 20 and length of 10. How many squares do I fill?
        // The value of one space is 2.
        // 14 / 2 = 7
        // Therefore,
        int filled = value / (max / length);

        StringBuilder output = new StringBuilder();

        output.append(String.format("%s : ", label));
        output.append('[');

        // Filled spaces
        for(int x = 0; x < filled; x++)
            output.append(full);

        // Empty spaces
        for(int x = 0; x < length - filled; x++)
            output.append(empty);

        output.append(String.format("] ~(%d / %d)~", value, max));

        return output.toString();
    }
}
