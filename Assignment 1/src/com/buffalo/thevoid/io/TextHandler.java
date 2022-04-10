package com.buffalo.thevoid.io;

import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * Static methods that have to do with the console
 */
public class TextHandler
{
    private static final Scanner s = new Scanner(System.in);

    /**
     * Validates input to match the system used here.
     * @param question ask
     * @param lower lower bound
     * @param upper upper bound
     * @return a number. Not this function's problem to deal with it.
     */
    public static int validInt(String question, int upper, int lower)
    {
        boolean lock = true;
        int input = 0xFFFFFF;
        System.out.println(question);

        while(lock)
        {
            try
            {
                input = s.nextInt();
                if (input > upper || input < lower)
                    throw new OutOfBoundsException();

                lock = false;
            }
            catch (OutOfBoundsException e)
            {
                System.out.println("Input must be above " + (upper + 1) + " and below " + (lower - 1));
            }
            catch(InputMismatchException e)
            {
                System.out.println("Input a valid integer.");
                // Flush
                s.nextLine();
            }
        }

        return input;
    }

    // Clears console
    public static void clearConsole()
    {
        // Since stackoverflow lied to me
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Do this instead
        for(int x = 0; x < 50; x++)
        {
            System.out.println();
        }
    }

    // Pause for dramatic effect
    public static void wait(int millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException ignore)
        {

        }
    }
}

// Private user defined exception
class OutOfBoundsException extends Exception
{
    public OutOfBoundsException()
    {
        super();
    }
}
