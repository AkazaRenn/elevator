package ca.carleton.winter2020.sysc3303a.group8.elevator;

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
    
    public void pressButton() {
        if(!lampOn) {
            lampOn = true;
            ELEVATOR.addStop(FLOOR_NUMBER);
        }
    }
    
    public void floorArrived() {
        if(lampOn) {
            lampOn = false;
        }
    }
    
    public boolean isLampOn() {
        return lampOn;
    }
}
