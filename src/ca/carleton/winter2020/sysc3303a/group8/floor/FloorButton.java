package ca.carleton.winter2020.sysc3303a.group8.floor;

import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;
/**
 * floor button
 * 
 * @author Zhi Qiao 101026761 
 * @author Dennis Liu
 */
public class FloorButton {

    private final Floor FLOOR;
    private final Direction DIRECTION;
    boolean lampOn = false;
    
    public FloorButton(Floor floor, Direction direction) {    
        FLOOR = floor;
        DIRECTION = direction;        
    }

    public Direction getDirection() {
        return DIRECTION;
    }

    public boolean isLampOn() {
        return lampOn;
    }
    
    public void press(Direction DIRECTION) {
        if(!lampOn) {
            lampOn = true;
            FLOOR.buttonPressed(this);
        }
    }
}

