package fr.thedarven.utils;

import java.util.Random;

public class RandomHelper {

    private static final Random random = new Random();

    /**
     * Generate random value between zero (inclusive) and max (exclusive).
     *
     * @param max Max value excluded.
     */
    public static int generate(int max) {
        return generate(0, max);
    }

    /**
     * Generate random value between min (inclusive) and max (exclusive).
     *
     * @param min Min value included.
     * @param max Max value excluded.
     */
    public static int generate(int min, int max) {
        return random.nextInt(max - min) + min;
    }

}
