package by.itacademy.robot;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;
import lombok.Getter;

import static by.itacademy.robot.Service.DayPeriod.*;
import static by.itacademy.robot.Service.*;
import static by.itacademy.robot.SpareFactory.*;


public class Scientist implements Runnable {

    private final RobotsDump robotsDump;
    private final Servant servant;
    private final Map<Spare, Integer> robotSpares = new EnumMap<>(Spare.class);
    @Getter
    private int robots = 0;


    public Scientist(RobotsDump robotsDump) {
        this.robotsDump = robotsDump;
        this.servant = new Servant(robotsDump);
    }

    @Override
    public void run() {
        boolean workedThisNight = false;
        while (!Service.isCompetitionFinished) {
            synchronized (robotsDump) {
                if (getPeriodOfDay().equals(NIGHT) && robotsDump.opened()) {
                    System.out.println(thName() + "Scientist has night");
                    Map<Spare, Integer> collectedSpares = servant.collectSpares();
                    addNewSpares(collectedSpares);
                    checkAndBuild();
                    workedThisNight = true;
                }
                if (workedThisNight) {
                    try {
                        workedThisNight = false;
                        System.out.println(thName() + " Scientist is waiting");
                        robotsDump.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println(thName() + " Finished successfully");
    }

    private void addNewSpares(Map<Spare, Integer> spares) {
        spares.keySet().forEach(key -> robotSpares.merge(key, 1, (oldSpareAmount, newSpareAmount) -> oldSpareAmount + newSpareAmount));
    }

    private void checkAndBuild() {
        boolean allSpares = Stream.of(Spare.values()).allMatch(robotSpares::containsKey);
        boolean canBeBuild = robotSpares.values().stream().allMatch(detailCount -> detailCount > 0);

        System.out.println(thName() + "Spares that scientist have" + robotSpares);
        if (canBeBuild && allSpares) {
            robotSpares.keySet().forEach(key -> robotSpares.merge(key, 1, (oldValue, newValue) -> oldValue - newValue));
            robots++;
            System.out.println(thName() + "Number of robots being built to current moment" + robots);
        }
    }

    private String thName() {
        return Thread.currentThread().getName();
    }

}
