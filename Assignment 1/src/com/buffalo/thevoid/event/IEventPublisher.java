package com.buffalo.thevoid.event;

// <?> means ieventhandler uses a generic class
// raise isn't part of this interface because it's up to the implementing class to decide how to raise events.
public interface IEventPublisher
{
    void addEventHandler(IEventHandler<?> e);
    void removeEventHandler(IEventHandler<?> f); // Not used but declared here anyway.
}
