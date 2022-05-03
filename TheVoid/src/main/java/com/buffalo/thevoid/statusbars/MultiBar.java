package com.buffalo.thevoid.statusbars;

public class MultiBar extends ProgressBar
{
    private final int perBar;

    /**
     * Creates a new MultiBar object. MultiBars split their contents into multiple bars.
     * <p>
     * For example: <br>
     *
     * Dr. Eggman : [##########] x10 <br>
     * BadlandsChugs : [##########----------] x 50
     *
     * </p>
     * @param full   The character to symbolize a full cell.
     * @param empty  The character to symbolize an empty cell.
     * @param label  The name to put on this bar.
     * @param Max    The maximum value this object can work with. Must be > 0
     * @param length How many characters long is this bar?
     * @param perBar How much of the total value per bar?
     */
    public MultiBar(char full, char empty, String label, int Max, int length, int perBar)
    {
        super(full, empty, label, Max, length);
        this.perBar = perBar;
    }

    @Override
    public String toString()
    {
        // Value of a space
        // If i have 20 per bar and a length of 10
        // How much is a space? 2
        int perSpace = perBar / length;

        // Number of bars
        // If i have length of 10, max of 30 and value of 20
        // How many bars do i need? 2
        int bars = value / perBar;

        // Value of the last bar which is the only one that is drawn
        // Say it perBar is 10, current is 25 and max is 30
        // Expected output would be
        // [#####-----] x 2 with length of 10
        // One current bar, and two full extras

        // Value of a full bar is perBar
        // So how many of these full bars go into the current value
        // and what is the remainder?
        int valueOfLastBar = value % perBar;
        // Because 25 % 10 is 5

        // Due to an oversight with the mod operator
        if (valueOfLastBar < 1 && value > 0)
        {
            valueOfLastBar = perBar;
        }
        // Should ensure that a full bar is drawn.
        // UNLESS the value is actually 0

        // Finally,
        // How many spaces do i fill?
        // Translate this bar to spaces
        int filled = valueOfLastBar / perSpace;

        // Now with all the understanding done the coding is easy.
        StringBuilder output = new StringBuilder();

        output.append(String.format("%s : ", label));
        output.append('[');

        // Filled spaces
        for (int x = 0; x < filled; x++)
            output.append(full);

        // Empty spaces
        for (int x = 0; x < length - filled; x++)
            output.append(empty);

        if (bars > 1)
            output.append(String.format("] [x%d] =(%-3d / %2d)=", bars - 1, value, max));
        else
            output.append(String.format("] =(%3d / %-3d)=", value, max));

        return output.toString();
    }
}
