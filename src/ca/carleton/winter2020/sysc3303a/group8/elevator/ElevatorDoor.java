package ca.carleton.winter2020.sysc3303a.group8.elevator;

public class ElevatorDoor {

    private final ElevatorSubsystem ELEVATOR;
    
    private boolean doorOpen;
    
    public ElevatorDoor(ElevatorSubsystem elevator) {
        ELEVATOR = elevator;
        
        doorOpen = true;
    }
    
    public boolean isDoorOpen() {
        return doorOpen;
    }
    
    public void openDoor() {
        if(!doorOpen) {
            doorOpen = true;
            System.out.println("Elevator " + ELEVATOR.ELEVATOR_ID + " opened door.");
        }
    }
    
    public void closeDoor() {
        if(doorOpen) {
            doorOpen = false;
            System.out.println("Elevator " + ELEVATOR.ELEVATOR_ID + " closed door.");
        }
    }
}
