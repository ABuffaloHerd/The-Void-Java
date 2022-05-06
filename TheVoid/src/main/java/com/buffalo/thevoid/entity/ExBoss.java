package com.buffalo.thevoid.entity;

import java.util.function.Function;

public class ExBoss extends Boss
{
    public Function<IEntity, Integer> specialAttack2;
    public Function<IEntity, Integer> specialAttack3;
    /**
     * ExBosses are the ultimate enemy. They should be able to wipe the player dead in one hit.
     *
     * @param name          use your head
     * @param quotes        the boss likes to say things
     * @param MaxHP         how much hp does it have
     * @param def           ignored but assigned anyway
     * @param res           ignored but assigned anyway
     * @param attack        custom attack method.
     * @param specialAttack custom special attack method.
     * @param specialAttack2 custom special attack method 2. The player should have spent about 10 hours grinding to survive this.
     * @param specialAttack3 custom special attack method 3. This one should be able to incinerate the player despite all their efforts.
     */
    public ExBoss(String name, String[] quotes, int MaxHP, int def, int res, Function<IEntity, Integer> attack,
                  Function<IEntity, Integer> specialAttack, Function<IEntity, Integer> specialAttack2, Function<IEntity, Integer> specialAttack3)
    {
        super(name, quotes, MaxHP, def, res, attack, specialAttack);

        this.specialAttack2 = specialAttack2;
        this.specialAttack3 = specialAttack3;
    }
}
