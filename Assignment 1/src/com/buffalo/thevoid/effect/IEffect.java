package com.buffalo.thevoid.effect;

public interface IEffect
{
    void firstApply();
    void onProcess();
    void onCure();
    void finish();
}