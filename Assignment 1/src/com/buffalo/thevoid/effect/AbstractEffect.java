package com.buffalo.thevoid.effect;

import com.buffalo.thevoid.entity.IEntity;

import java.util.function.BiFunction;

public abstract class AbstractEffect implements IEffect
{
    public BiFunction<IEntity, Integer, Integer> effect;
    private int duration;

    public AbstractEffect(BiFunction<IEntity, Integer, Integer> effect, int duration)
    {
        this.duration = duration;
        this.effect = effect;
    }

    @Override
    public void firstApply()
    {
        effect.apply(getEntity(), duration);
    }
}
