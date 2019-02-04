package by.itacademy.robot;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Getter;

import static by.itacademy.robot.SpareFactory.*;
import static java.lang.Thread.*;
import static java.util.Arrays.*;

public class RobotsDump {

    @Getter
    private static final List<Spare> spares = new LinkedList<>();

    private static volatile boolean isOpened = false;

    static {
        for (int i = 0; i < 20; i++) {
            spares.add(asList(Spare.values())
                    .get(Randomizer.randomInt(0, 8)));
        }
    }

    public synchronized void takeSpares(final List<Spare> newSpares) {
        spares.addAll(newSpares);
    }

    public synchronized Map<Spare, Integer> giveSpares(final int requiredSpares) {
        Map<Spare, Integer> collectedSpares = new EnumMap<>(Spare.class);

        int sparesCount = spares.size();
        System.out.println(currentThread().getName() + " RobotDump has :" + sparesCount + " spares");
        if (sparesCount >= requiredSpares) {
            collectedSpares = collectRandomDetails(requiredSpares);

        } else if (sparesCount < requiredSpares && sparesCount != 0) {
            collectedSpares = collectRandomDetails(sparesCount);
        }
        return collectedSpares;
    }

    private synchronized Map<Spare, Integer> collectRandomDetails(final int requiredNumber) {
        Map<Spare, Integer> collectedSpares = new EnumMap<>(Spare.class);
        for (int i = 0; i < requiredNumber; i++) {
            Spare obtainedDetail = spares.remove(Randomizer.randomInt(0, spares.size() - 1));
            collectedSpares.merge(obtainedDetail, 1, (oldVal, newVal) -> oldVal + newVal);
        }
        return collectedSpares;
    }

    public synchronized void setIsOpened(boolean value) {
        isOpened = value;
    }

    public synchronized boolean opened() {
        return isOpened;
    }

}