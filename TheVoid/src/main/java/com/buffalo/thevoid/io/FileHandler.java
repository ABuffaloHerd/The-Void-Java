package com.buffalo.thevoid.io;

import com.buffalo.thevoid.data.Config;
import com.buffalo.thevoid.data.Tuple;
import com.buffalo.thevoid.entity.Player;
import com.buffalo.thevoid.utils.Utilities;

import java.io.*;

/**
 * "The split() method is preferred and recommended even though it is comparatively slower than StringTokenizer.
 *  This is because it is more robust and easier to use than StringTokenizer."
 *  - https://www.geeksforgeeks.org/difference-between-stringtokenizer-and-split-method-in-java/
 *   You tell me to use string tokenizer while recommending netbeans.
 */
public class FileHandler
{
    // Loads boss quotes from the files in the resources folder.
    // Loads 10 lines.
    public static String[] loadBossQuotes(String bossname) throws IOException
    {
        String[] returnMe = new String[10];
        int counter = 0;

        try(BufferedReader br = new BufferedReader(new FileReader(Config.cwd + "/resources/" + bossname + ".txt")))
        {
            while(br.ready())
            {
                returnMe[counter] = br.readLine();
                counter++;
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Unable to read " + bossname + "'s file.");
            System.out.println(Config.cwd + "/resources/" + bossname + ".txt");
            return null;
        }
        catch (ArrayIndexOutOfBoundsException ignore)
        {
            // Array full, return what we have
        }

        return returnMe;
    }

    /**
     * Reads config.cfg located in the resources folder.
     * Returns the Integer class rather than primitive int so that null can be returned if an attribute is not found.
     * This makes sure that the config file is properly formatted. It's going to complain.
     * @param attribute The attribute to read.
     * @return The value of the attribute.
     */
    public static Integer readConfig(String attribute)
    {
        Integer value = null;

        try(BufferedReader br = new BufferedReader(new FileReader(Config.cwd + "/resources/config.cfg")))
        {
            while(br.ready())
            {
                String line = br.readLine();
                if (line.startsWith("#")) // Ignore comments
                    continue;

                if(line.startsWith("[" + attribute + "]"))
                {
                    String[] parts = line.split(" ");

                    value = Integer.parseInt(parts[1]);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Unable to read config file.");
            System.out.println(Config.cwd + "/resources/config.cfg");
            System.exit(-1);
        }

        return value;
    }

    /**
     * Writes player data to player data file. Does not serialize the player object. (lambdas hate that)
     * This class doesn't have the loadPlayer method because that's NewGame's job.
     * @param p The player to write.
     */
    public static void savePlayer(Player p)
    {
        p.refreshStats(); // make sure player is at max stats before writing to file

        Tuple<Integer, Integer> allUnlocked = Utilities.getWeaponCount();

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(Config.cwd + "/resources/save/player.dat")))
        {
            bw.write("Name=" + p.name + "\n");
            bw.write("Level=" + p.getLevel() + "\n");
            bw.write("KillCount=" + p.getKillCount() + "\n");
            bw.write("HP=" + p.getMaxHP() + "\n");
            bw.write("MP=" + p.getMaxMP() + "\n");
            bw.write("DEF=" + p.getDEF() + "\n");
            bw.write("RES=" + p.getRES() + "\n");
            bw.write("WeaponsUnlocked=" + allUnlocked.getItem1() + "\n");
            bw.write("SpellsUnlocked=" + allUnlocked.getItem2() + "\n");
        }
        catch (IOException e)
        {
            System.out.println("Unable to write player data file.");
        }

        System.out.println("Player data saved.");

    }

}
