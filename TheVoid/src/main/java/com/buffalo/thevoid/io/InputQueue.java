package com.buffalo.thevoid.io;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Global input queue. Keeps input from buttons or textbox for the game to dequeue and process.
 */
public class InputQueue
{
    public static final Queue<String> queue = new LinkedList<>();

    public static void enqueue(String s)
    {
        queue.add(s);
        System.out.println("InputQueue: " + s);
        System.out.println(queue);
    }

    public static String dequeue()
    {
        return queue.isEmpty() ? null : queue.poll();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for(String s : queue)
        {
            sb.append(s);
        }

        return sb.toString();
    }
}
