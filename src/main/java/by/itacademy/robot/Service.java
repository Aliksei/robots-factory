package by.itacademy.robot;

import static by.itacademy.robot.Service.DayPeriod.*;
import static java.lang.String.*;

public class Service {

    private static volatile DayPeriod dayPeriod = DAY;
    public static final int nights = 100;
    public static volatile boolean isCompetitionFinished = false;

    public static void main(String[] args) throws InterruptedException {

        RobotsDump robotDump = new RobotsDump();

        Scientist scientist1 = new Scientist(robotDump);
        Scientist scientist2 = new Scientist(robotDump);

        Thread scientistThread2 = new Thread(scientist2);
        scientistThread2.setName("[Thread Scientist 2] ");
        scientistThread2.start();

        Thread scientistThread1 = new Thread(scientist1);
        scientistThread1.setName("[Thread Scientist 1] ");
        scientistThread1.start();

        SpareFactory factory = new SpareFactory(robotDump);
        Thread factoryThread = new Thread(factory);
        factoryThread.setName("[Thread Factory] ");
        factoryThread.start();

        for (int i = 0; i < nights * 2; i++) {
            Thread.sleep(50);
            changePeriod();
        }
        isCompetitionFinished = true;

        factoryThread.join();
        scientistThread1.join();
        scientistThread2.join();

        System.out.println("Scientist 1 built : " + scientist1.getRobots() + " Robots");
        System.out.println("Scientist 2 built : " + scientist2.getRobots() + " Robots");
        System.out.println("Spares left on robotDump is : " + RobotsDump.getSpares().size());

        int firstScientistResult = scientist1.getRobots();
        int secondScientistResult = scientist2.getRobots();
        String winner;
        if (firstScientistResult > secondScientistResult) {
            winner = format("Winner is [Scientist 1] with result [%d robots]", firstScientistResult);
        } else if (firstScientistResult == secondScientistResult) {
            winner = format("Winner wasn't found, both scientist built [%d robots]  ", secondScientistResult);
        } else {
            winner = format("Winner is [Scientist 2] with result [%d robots]", secondScientistResult);
        }
        System.out.println(winner);
    }

    public static synchronized DayPeriod getPeriodOfDay() {
        return dayPeriod;
    }

    public enum DayPeriod {
        DAY, NIGHT
    }

    private static synchronized void changePeriod() {
        if (DAY.equals(dayPeriod)) {
            dayPeriod = NIGHT;
            System.out.println("Night Happened");
        } else {
            System.out.println("Day Happened");
            dayPeriod = DAY;
        }
    }

}
