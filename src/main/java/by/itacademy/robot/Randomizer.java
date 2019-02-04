package by.itacademy.robot;

import java.util.Random;

public class Randomizer {

    public static int randomInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}
