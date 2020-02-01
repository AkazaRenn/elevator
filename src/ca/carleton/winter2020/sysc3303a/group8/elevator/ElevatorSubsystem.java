package ca.carleton.winter2020.sysc3303a.group8.elevator;

import java.util.ArrayList;
import java.util.List;

import ca.carleton.winter2020.sysc3303a.group8.scheduler.Scheduler;
import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;
import ca.carleton.winter2020.sysc3303a.group8.utils.SortedDeque;

/**
 * Elevator subsystem of the project.
 * 
 * @author Frank Xu 101050120
 */
public class ElevatorSubsystem extends Thread {

    public final Scheduler SCHEDULER;
    public final int ELEVATOR_ID;
    public final int BOTTOM_FLOOR;
    public final int TOP_FLOOR;
    public final ElevatorMotor MOTOR;
    public final List<ElevatorButton> BUTTONS;
    public final ElevatorDoor DOOR;

    private int currentFloor;
    
    public ElevatorSubsystem(Scheduler scheduler, int elevatorId, int bottomFloor, int topFloor) {
        SCHEDULER = scheduler;
        ELEVATOR_ID = elevatorId;
        BOTTOM_FLOOR = bottomFloor;
        TOP_FLOOR = topFloor;
        MOTOR = new ElevatorMotor(this);
        BUTTONS = new ArrayList<>(topFloor - bottomFloor + 1);
        for(int i = bottomFloor; i <= topFloor; i++) {
            BUTTONS.add(new ElevatorButton(this, i));
        }
        DOOR = new ElevatorDoor(this);
        
        currentFloor = 1;
    }
    
    /**
     * Return the direction of the elevator
     * 
     * @return the direction of the elevator
     */
    public Direction getDirection() {
        return MOTOR.getDirection();
    }
    
    /**
     * Press a button to floorNumber inside the elevator
     * 
     * @param floorNumber floor where the button indicates
     */
    public void pressButton(int floorNumber) {
        BUTTONS.get(floorNumber - BOTTOM_FLOOR).press();
    }

    /**
     * From the schedule update the current floor the elevator is at
     * 
     * @return the current floor the elevator is at
     */
    public int updateCurrentFloor(int floorNumber) {
        BUTTONS.get(currentFloor - BOTTOM_FLOOR).floorArrived();
        return floorNumber;
    }

    // @Override
    // public void run() {
    //     if((getDirection() == Direction.UP && getNextFloor() == upQueue.getHigher(currentFloor)) ||
    //             (getDirection() == Direction.DOWN && getNextFloor() == downQueue.getLower(currentFloor))) {
    //         MOTOR.stop();
    //         DOOR.open();
    //     } else if(upQueue.getHigher(currentFloor) != null) {
    //             MOTOR.moveUp();
    //     } else if(downQueue.getLower(currentFloor) != null) {
    //             MOTOR.moveDown();
    //     }
    // }
}
