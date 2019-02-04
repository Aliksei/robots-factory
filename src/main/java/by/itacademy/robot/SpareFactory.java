package by.itacademy.robot;

import java.util.LinkedList;
import java.util.List;

import static by.itacademy.robot.Randomizer.*;
import static by.itacademy.robot.Service.DayPeriod.*;
import static by.itacademy.robot.Service.*;
import static java.lang.Thread.*;
import static java.util.Arrays.*;

public class SpareFactory implements Runnable {

    private final RobotsDump robotsDump;

    public SpareFactory(RobotsDump robotsDump) {
        this.robotsDump = robotsDump;
    }

    public synchronized List<Spare> deliverSpares(int number) {
        List<Spare> randomSpares = new LinkedList<>();
        for (int i = 0; i < number; i++) {
            randomSpares.add(asList(Spare.values())
                    .get(randomInt(0, 8)));
        }
        return randomSpares;
    }

    @Override
    public void run() {
        boolean workedThisNight = false;
        while (!isCompetitionFinished) {
            synchronized (robotsDump) {
                if (getPeriodOfDay().equals(DAY) && workedThisNight) {
                    System.out.println(currentThread().getName() + "Factory is Closing");
                    workedThisNight = false;
                    robotsDump.setIsOpened(false);
                } else if (getPeriodOfDay().equals(NIGHT) && !workedThisNight) {
                    int sparesAmount = randomInt(1, 4);
                    robotsDump.takeSpares(deliverSpares(sparesAmount));
                    System.out.println(currentThread().getName() + "Just sent " + sparesAmount + " items to robotsDump");
                    workedThisNight = true;
                    robotsDump.setIsOpened(true);
                    robotsDump.notifyAll();
                    System.out.println(currentThread().getName() + "Factory is Opened");
                }
            }
        }
        synchronized (robotsDump) {
            robotsDump.notifyAll();
        }
    }

    public enum Spare {
        BODY,
        HEAD,
        RIGHT_HAND,
        RIGHT_FOOT,
        LEFT_HAND,
        LEFT_FOOT,
        CPU,
        RAM,
        HDD
    }

}
