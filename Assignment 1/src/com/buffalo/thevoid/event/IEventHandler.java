package com.buffalo.thevoid.event;

// heheheha grr heheheha grr
// https://stackoverflow.com/questions/1530461/alternate-of-c-sharp-events-in-java
// Throw arguments between objects (threading gives me a headache)

public interface IEventHandler<E>
{
    void handleEvent(Object sender, E args);
}
