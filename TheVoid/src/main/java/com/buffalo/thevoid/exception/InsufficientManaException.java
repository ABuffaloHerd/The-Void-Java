package com.buffalo.thevoid.exception;

// Exception for when a spell is used with insufficient mana.
public class InsufficientManaException extends Exception
{
    public final String message;
    public InsufficientManaException(String args)
    {
        super();
        this.message = args;
    }
}
