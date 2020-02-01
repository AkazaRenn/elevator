package ca.carleton.winter2020.sysc3303a.group8.floor;

import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

public class FloorButton {

	private Floor FLOOR;
	private Direction direction;
	boolean lampOn = false;
	
	public FloorButton(Floor floor,Direction direction) {	
		FLOOR = floor;
		this.direction = direction;		
	}

	public Direction getDirection() {
		return direction;
	}

	public boolean isLampOn() {
		return lampOn;
	}
	
	public void press() {
        if(!lampOn) {
            lampOn = true;
            FLOOR.addStop(direction);
        }
	}
}

