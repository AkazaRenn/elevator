package ca.carleton.winter2020.sysc3303a.group8.elevator;

/**
 * Door in the elevator system
 * 
 * @author Frank Xu 101050120
 */
public class ElevatorDoor {

    private final ElevatorSubsystem ELEVATOR;
    
    private boolean open;
    
    public ElevatorDoor(ElevatorSubsystem elevator) {
        ELEVATOR = elevator;
        
        open = true;
    }
    
    /**
     * Return whether the door is open
     * 
     * @return whether the door is open
     */
    public boolean isOpen() {
        return open;
    }
    
    /**
     * Open the door
     */
    public void open() {
        if(!open) {
            open = true;
            System.out.println("Elevator " + ELEVATOR.ELEVATOR_ID + " opened door.");
        }
    }
    
    /**
     * Close the door
     */
    public void close() {
        if(open) {
            open = false;
            System.out.println("Elevator " + ELEVATOR.ELEVATOR_ID + " closed door.");
        }
    }
}
