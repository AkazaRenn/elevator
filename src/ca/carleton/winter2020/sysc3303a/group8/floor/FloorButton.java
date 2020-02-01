package ca.carleton.winter2020.sysc3303a.group8.floor;

import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

public class FloorButton {

	private Direction direction;
	boolean state;
	
	public FloorButton(Direction direction) {
		state = false;					
		this.direction = direction;		
	}

	public Direction getDirection() {
		return direction;
	}

	public boolean getState() {
		return state;
	}
	
	public void setState(boolean state) {
		this.state = state;
	}

	public String getStateString() {
		if (state) {
			return "PRESSED";
		}else {
			return "UNPRESSED";
		}
	}
}

