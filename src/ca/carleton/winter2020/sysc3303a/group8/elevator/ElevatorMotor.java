package ca.carleton.winter2020.sysc3303a.group8.elevator;

import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

/**
 * Motor in the elevator system controlling the movement of the elevator
 * 
 * @author Frank Xu 101050120
 */
public class ElevatorMotor {
    
    private final ElevatorSubsystem ELEVATOR;
     private ElevatorStates states;
    private Direction direction;
    
    public ElevatorMotor(ElevatorSubsystem elevator) {
        ELEVATOR = elevator;
         states = ElevatorStates.NOTINUSE;
        direction = Direction.HOLD;
    }
    
    /**
     * Control the elevator to move upwards
     */
    public void moveUp() {
        direction = Direction.UP;
        states = ElevatorStates.INUSE;
        System.out.println("Elevator " + ELEVATOR.ELEVATOR_ID + " starts moving up.");
    }
    
    /**
     * Control the elevator to move downwards
     */
    public void moveDown() {
        direction = Direction.DOWN;
        states = ElevatorStates.INUSE;
        System.out.println("Elevator " + ELEVATOR.ELEVATOR_ID + " starts moving down.");
    }
    
    /**
     * Control the elevator to stop
     */
    public void stop() {
        direction = Direction.HOLD;
        states = ElevatorStates.NOTINUSE;
        System.out.println("Elevator " + ELEVATOR.ELEVATOR_ID + " stops.");
    }
    
    /**
     * Return the direction of the elevator
     * 
     * @return direction of the elevator
     */
    public Direction getDirection() {
        return direction;
    }
    public ElevatorStates getStates(){
        return states;
    }
}