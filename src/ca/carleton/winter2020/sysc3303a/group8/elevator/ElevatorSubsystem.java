package ca.carleton.winter2020.sysc3303a.group8.elevator;

import java.util.ArrayList;
import java.util.List;

import ca.carleton.winter2020.sysc3303a.group8.scheduler.Scheduler;
import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;
import ca.carleton.winter2020.sysc3303a.group8.utils.SortedDeque;

public class ElevatorSubsystem extends Thread {

    public final Scheduler SCHEDULER;
    public final int ELEVATOR_ID;
    public final int BOTTOM_FLOOR;
    public final int TOP_FLOOR;
    public final ElevatorMotor MOTOR;
    public final List<ElevatorButton> BUTTONS;
    public final ElevatorDoor DOOR;

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
        
        upQueue = new SortedDeque<>();
        downQueue = new SortedDeque<>();
    }
    
    public synchronized void addStop(int floor) {
        if(floor > getCurrentFloor() && getDirection() == Direction.UP) {
            upQueue.add(floor);
        } else if (floor < getCurrentFloor() && getDirection() == Direction.DOWN) {
            downQueue.add(floor);
        }
    }
    
    public synchronized void addStop(Direction direction, int floor) {
        if(direction == Direction.UP) {
            upQueue.add(floor);
        } else if(direction == Direction.DOWN) {
            downQueue.add(floor);
        }
    }
    
    public Direction getDirection() {
        return MOTOR.getDirection();
    }
    
    public void pressButton(int floorNumber) {
        BUTTONS.get(floorNumber - BOTTOM_FLOOR).pressButton();
    }
    
    public synchronized void arriveAtFloor(int floorNumber) {
        BUTTONS.get(floorNumber - BOTTOM_FLOOR).floorArrived();
    }

    /**
     * @return the currentFloor
     */
    public int getCurrentFloor() {
        //TODO acquire mutex for the current floor it is at from the scheduler
        return 1;
    }
    
    private int getNextFloor() {
        if(getDirection() == Direction.UP) {
            return getCurrentFloor() + 1;
        } else if(getDirection() == Direction.DOWN) {
            return getCurrentFloor() - 1;
        }
        return getCurrentFloor();
    }

    @Override
    public void run() {
        int currentFloor = getCurrentFloor();
        if((getDirection() == Direction.UP && getNextFloor() == upQueue.getHigher(currentFloor)) ||
                (getDirection() == Direction.DOWN && getNextFloor() == downQueue.getLower(currentFloor))) {
            MOTOR.stop();
        } else if(getDirection() == Direction.HOLD) {
            if(!upQueue.isEmpty()) {
                MOTOR.moveUp();
            } else if(!downQueue.isEmpty()) {
                MOTOR.moveDown();
            }
        }
    }
}
