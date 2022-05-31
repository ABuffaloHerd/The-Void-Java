package com.buffalo.thevoid.io;

import com.buffalo.thevoid.gui.Mediator;
import com.buffalo.thevoid.main.Program;

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

    // Has no limits.
    public static int validInt(String question)
    {
        boolean lock = true;
        int input = 0xFFFFFF;
        System.out.println(question);

        while(lock)
        {
            try
            {
                input = s.nextInt();
                lock = false;
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

    /**
     * inputQueue version. Runs in a thread.
     * @return the next valid input in the queue.
     */
    public static int validInt(int upper, int lower)
    {
        System.out.println("Waiting for input...");
        Integer[] input = new Integer[1]; // needs to be nullable. is also a bit hacky.
        Thread t = new Thread(() ->
        {
            boolean lock = true;

            // Empty the queue
            if(!InputQueue.queue.isEmpty())
            {
                InputQueue.queue.clear();
            }

            while(lock && Program.gameRunning)
            {
                try
                {
                    // Check to see that the queue is not empty
                    if(InputQueue.queue.isEmpty())
                    {
                        System.out.println("Empty queue. Waiting...");
                        wait(100);
                        continue;
                    }
                    // Now we can parse the input
                    System.out.println("Parsing input...");
                    input[0] = Integer.parseInt(InputQueue.dequeue());
                    if (input[0] > upper || input[0] < lower)
                        throw new OutOfBoundsException();
                    else
                    {
                        lock = false;
                        continue;
                    }
                }
                catch (OutOfBoundsException e)
                {
                    Mediator.sendToLog(null, "Input must be above " + (upper + 1) + " and below " + (lower - 1));
                }
                catch(InputMismatchException | NumberFormatException e)
                {
                    Mediator.sendToLog(null, "Input a valid integer.");
                }
                catch(NullPointerException e)
                {
                    System.out.println("Found no valid integer in input queue. Thread rerunning.");
                }

                wait(100);
            }
        });

        // Start the thread
        t.start();

        // Wait for it to finish before returning.
        // This halts the main thread.
        try
        {
            if(Program.gameRunning)
                t.join();
        }
        catch (InterruptedException ignore) {}

        // STOP
        t.stop();

        return input[0];
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
