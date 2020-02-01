package ca.carleton.winter2020.sysc3303a.group8.elevator;

import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

public class ElevatorMotor {
    
    private final ElevatorSubsystem ELEVATOR;
    
    private Direction direction;
    
    public ElevatorMotor(ElevatorSubsystem elevator) {
        ELEVATOR = elevator;

        direction = Direction.HOLD;
    }
    
    public void moveUp() {
        direction = Direction.UP;
        System.out.println("Elevator " + ELEVATOR.ELEVATOR_ID + " starts moving up.");
    }
    
    public void moveDown() {
        direction = Direction.DOWN;
        System.out.println("Elevator " + ELEVATOR.ELEVATOR_ID + " starts moving down.");
    }
    
    public void stop() {
        direction = Direction.HOLD;
        System.out.println("Elevator " + ELEVATOR.ELEVATOR_ID + " stops.");
    }
    
    public Direction getDirection() {
        return direction;
    }
}
