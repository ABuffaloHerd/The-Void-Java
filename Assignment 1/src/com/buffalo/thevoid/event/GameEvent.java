package com.buffalo.thevoid.event;

// W-w-w-what's going on? on?
// https://www.youtube.com/watch?v=k85mRPqvMbE
public enum GameEvent
{
    NEWGAME("New Game"),
    LOADGAME("Load Game"),
    SAVEGAME("Save Game"),
    BATTLE("Fight!"),
    SHOP("Shop"),
    BOSS("Boss battle"),
    EXBOSS("Final boss"),
    LEVELUP("Level up"),
    PLAYERWIN("You win!"), // only seen in battle
    PLAYERLOSE("You died!"), // same here
    EXIT("Exit"),
    RESUPPLY("Resupply");

    public final String text;

    GameEvent(String text)
    {
        this.text = text;
    }
}
