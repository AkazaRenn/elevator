package ca.carleton.winter2020.sysc3303a.group8.elevator;

/**
 * Button in the elevator system deciding which floor the elevator would stop at
 * 
 * @author Frank Xu 101050120
 */
public class ElevatorButton {
    
    public final String BUTTON_TEXT;
    private final ElevatorSubsystem ELEVATOR;
    private final int FLOOR_NUMBER;
    
    private boolean lampOn;
    
    public ElevatorButton(String buttonText, ElevatorSubsystem elevator, int floorNumber) {
        BUTTON_TEXT = buttonText;
        ELEVATOR = elevator;
        FLOOR_NUMBER = floorNumber;
        
        lampOn = false;
    }
    
    public ElevatorButton(ElevatorSubsystem elevator, int floorNumber) {
        this(((Integer)floorNumber).toString(), elevator, floorNumber);
    }
    
    /**
     * Press the button
     */
    public void press() {
        if(!lampOn) {
            ELEVATOR.addStop(FLOOR_NUMBER);
            lampOn = true;
        }
    }

    /**
     * Turn on the lamp of the button
     */
    public void turnOnLamp() {
        if(!lampOn) {
            lampOn = true;
        }
    }
    
    /**
     * Turn off the lamp when arriving at the corresponding floor
     */
    public void floorArrived() {
        if(lampOn) {
            lampOn = false;
        }
    }
    
    /**
     * Return whether the lamp of the button is on
     * 
     * @return whether the lamp of the button is on
     */
    public boolean isLampOn() {
        return lampOn;
    }
}
