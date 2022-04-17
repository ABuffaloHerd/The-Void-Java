package com.buffalo.thevoid.event;

// heheheha grr heheheha grr
// Throw arguments between objects (threading gives me a headache)
// Like C#'s event system.

public interface IEventHandler<E>
{
    void handleEvent(Object sender, E args);
}
