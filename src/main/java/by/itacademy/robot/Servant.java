package by.itacademy.robot;

import java.util.Map;

import static by.itacademy.robot.SpareFactory.*;
import static java.lang.Thread.*;


public class Servant {

    private final RobotsDump robotDump;

    public Servant(RobotsDump robotDump) {
        this.robotDump = robotDump;
    }

    public Map<Spare, Integer> collectSpares() {
        int randomInt = Randomizer.randomInt(1, 4);
        System.out.println(currentThread().getName() + "Looking for [ " + randomInt + "] spares");
        return robotDump.giveSpares(randomInt);
    }

}
