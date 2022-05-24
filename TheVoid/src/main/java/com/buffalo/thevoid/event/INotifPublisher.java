package com.buffalo.thevoid.event;

/**
 * Event interface minus the args.
 */
public interface INotifPublisher
{
    void ping();
    void addListener(INotifListener listener);
    void removeListener(INotifListener listener); // Optional, throw exception if not needed
}
