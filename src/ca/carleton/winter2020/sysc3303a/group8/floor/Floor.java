package ca.carleton.winter2020.sysc3303a.group8.floor;

import ca.carleton.winter2020.sysc3303a.group8.utils.Command;

/**
 * floor information
 * 
 * @author Zhi Qiao 101026761
 * @author Dennis Liu
 * @author Frank Xu
 */

public class Floor {

    private final int FLOOR_NUM;
    private final int CAR_COUNT;

    private boolean upLamp;
    private boolean downLamp;
    private Command[] lampDirentions;

    public Floor(int floorNum, int carCount) {
        FLOOR_NUM = floorNum;
        CAR_COUNT = carCount;
        upLamp = false;
        downLamp = false;
        lampDirentions = new Command[carCount];
        for (int i = 0; i < carCount; i++) {
            lampDirentions[i] = Command.STOP;
        }

    }

    public int getFloorNum() {
        return FLOOR_NUM;
    }

    public int getCarCount() {
        return CAR_COUNT;
    }

    public boolean getUpLamp() {
        return upLamp;
    }

    public boolean getDownLamp() {
        return downLamp;
    }

    public Command getDirection(int floorNum) {
        if (floorNum < CAR_COUNT) {
            return lampDirentions[floorNum];
        } else {
            return Command.STOP;
        }
    }

    public void setUpLamp(boolean on) {
        upLamp = on;
    }

    public void setDownLamp(boolean on) {
        downLamp = on;
    }

}
