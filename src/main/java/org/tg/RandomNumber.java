package org.tg;
import java.util.Random;

public class RandomNumber {
    public static int[] getRandomNumberInts(int min, int max) {
        Random random = new Random();
        int[] randomNumbers = random.ints(min, (max + 1)).distinct().limit(10).toArray();
        return randomNumbers;
    }
    public static int getRandomNumberInt(int min, int max) {
        Random random = new Random();
        return random.ints(min, (max + 1)).findFirst().getAsInt();
    }
}