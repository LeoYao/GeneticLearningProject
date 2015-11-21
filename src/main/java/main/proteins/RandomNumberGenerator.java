package main.proteins;

import java.util.Random;

/**
 * Created by Leo on 11/6/15.
 */
public class RandomNumberGenerator {
    private static Random random = new Random();

    /**
     *
     * @param start inclusive
     * @param end exclusive
     * @return
     */

    public static int generate(int start, int end)
    {
        int offset = end - start;
        return random.nextInt(offset) + start;
    }
}
