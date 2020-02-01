package ca.carleton.winter2020.sysc3303a.group8.scheduler;

import ca.carleton.winter2020.sysc3303a.group8.elevator.ElevatorSubsystem;
import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;
import ca.carleton.winter2020.sysc3303a.group8.utils.SortedDeque;

/**
 * Structure containing data of a single elevator car
 * 
 * @author Frank Xu 101050120
 */
public class Elevator {
    private final ElevatorSubsystem ELEVATOR;

    private int currentFloor;
    private SortedDeque<Integer> upQueue;
    private SortedDeque<Integer> downQueue;

    public Elevator(ElevatorSubsystem elevator) {
        ELEVATOR = elevator;
        
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
    public void addStop(int floor) {
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
    public void addStop(Direction direction, int floor) {
        if(direction == Direction.UP) {
            upQueue.add(floor);
        } else if(direction == Direction.DOWN) {
            downQueue.add(floor);
        }
    }

    /**
     * Return the next floor the elevator will stop at in the job queues
     * 
     * @return the next floor the elevator will stop at in the job queues
     */
    public Integer getNextStoppingFloor() {
        switch(ELEVATOR.getDirection()) {
            case UP:
                if(upQueue.getHigher(currentFloor) != null) {
                    return upQueue.getHigher(currentFloor);
                } else {
                    return downQueue.getLower(currentFloor);
                }
            case DOWN:
                if(downQueue.getLower(currentFloor) != null) {
                    return downQueue.getLower(currentFloor);
                } else {
                    return upQueue.getHigher(currentFloor);
                }
            default:
                return null;
        }
    }
    
    /**
     * Calculate the next floor the elevator will be arriving at in preparation 
     * of stops
     * 
     * @return next floor the elevator will be arriving at
     */
    private int getNextFloor() {
        switch(ELEVATOR.getDirection()) {
            case UP:
                return currentFloor + 1;
            case DOWN:
                return currentFloor - 1;
            default:
                return currentFloor;
        }
    }
    
    /**
     * Setter of currentFloor
     * @param currentFloor currentFloor the elevator is at
     */
    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
        ELEVATOR.updateCurrentFloor(currentFloor);
    }
}