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
    private SortedDeque<Integer> upQueue;
    private SortedDeque<Integer> downQueue;
    
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
        upQueue = new SortedDeque<>();
        downQueue = new SortedDeque<>();
    }
    
    /**
     * Add a stop ignoring the direction, used for users inside the elevator to
     * leave at specific floor.
     * 
     * @param floor floor where the user wants to leave
     */
    public synchronized void addStop(int floor) {
        if(floor > currentFloor) {
            upQueue.add(floor);
        } else if (floor < currentFloor) {
            downQueue.add(floor);
        }
    }
    
    /**
     * Add a stop depending on the direction.
     * 
     * @param direction direction where the user wants to go
     * @param floor floor where the user is at
     */
    public synchronized void addStop(Direction direction, int floor) {
        if(direction == Direction.UP) {
            upQueue.add(floor);
        } else if(direction == Direction.DOWN) {
            downQueue.add(floor);
        }
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
    private int updateCurrentFloor() {
        //TODO acquire mutex for the current floor it is at from the scheduler
        BUTTONS.get(currentFloor - BOTTOM_FLOOR).floorArrived();
        return 1;
    }
    
    /**
     * Calculate the next floor the elevator will be arriving at in preparation 
     * of stops
     * 
     * @return next floor the elevator will be arriving at
     */
    private int getNextFloor() {
        if(getDirection() == Direction.UP) {
            return currentFloor + 1;
        } else if(getDirection() == Direction.DOWN) {
            return currentFloor - 1;
        }
        return currentFloor;
    }

    @Override
    public void run() {
        updateCurrentFloor();
        if((getDirection() == Direction.UP && getNextFloor() == upQueue.getHigher(currentFloor)) ||
                (getDirection() == Direction.DOWN && getNextFloor() == downQueue.getLower(currentFloor))) {
            MOTOR.stop();
            DOOR.open();
        } else if(upQueue.getHigher(currentFloor) != null) {
                MOTOR.moveUp();
        } else if(downQueue.getLower(currentFloor) != null) {
                MOTOR.moveDown();
        }
    }
}
