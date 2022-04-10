package com.buffalo.thevoid.factory;

import com.buffalo.thevoid.data.Config;
import com.buffalo.thevoid.entity.Enemy;

import java.util.Random;

public class MonsterFactory
{
    /**
     * *slaps function* This bad boy generates an enemy based on level.
     * @param level determines how hard the enemy is.
     * @return a new enemy.
     */
    public static Enemy generateEnemy(int level)
    {
        Random r = new Random();
        if(level < 1)
        {
            level = 1;
        }
        else if (level == 0xFFFFFF) // Testing purposes
            return new Enemy(MonsterNames.MonsterNameList[r.nextInt(MonsterNames.MonsterNameListSize)],
                    1, 1, 1, 1);

        String name = MonsterNames.MonsterNameList[r.nextInt(MonsterNames.MonsterNameListSize)];
        int atk = level * 15 * Config.DIFFICULTY;
        int def = (level * 10) + Config.DIFFICULTY;
        int res = ((level - 1) * (Config.DIFFICULTY - 1));
        int maxhp = level * 100;

        return new Enemy(name, atk, def, res, maxhp);
    }
}
