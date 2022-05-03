package com.buffalo.thevoid.io;

import com.buffalo.thevoid.data.Config;
import com.buffalo.thevoid.event.IEventHandler;

import java.io.*;

// When this receives an event it writes that event to out.txt in the logs folder.
public class LogEventHandler implements IEventHandler<String>
{
    public static LogEventHandler Logger = new LogEventHandler();

    // Write the event
    @Override
    public void handleEvent(Object sender, String args)
    {
        try
        {
            BufferedWriter bufferedWriter;

            if(isEmpty())
                bufferedWriter = new BufferedWriter(new FileWriter(Config.cwd + "/logs/out.txt", false));
            else
                bufferedWriter = new BufferedWriter(new FileWriter(Config.cwd + "/logs/out.txt", true));

            bufferedWriter.write("Logged event: " + args);
            bufferedWriter.newLine();

            bufferedWriter.close();
        }
        catch(IOException ignore)
        {
            System.out.println("Cannot write to logfile out.txt. ");
        }
    }

    // Helper method to check if file is empty
    private boolean isEmpty()
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("./Assignment 1/logs/out.txt"));
            if (br.readLine() == null)
            {
                return true;
            }
        }
        catch (IOException e)
        {
            // The other function will make a file in this case, so it can be ignored.
        }

        return false;
    }

}
