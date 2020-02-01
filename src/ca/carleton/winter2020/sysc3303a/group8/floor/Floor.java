package ca.carleton.winter2020.sysc3303a.group8.floor;

import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

public class Floor {
	
	int floorNum;
	boolean moving;
	boolean upLamp;
	boolean downLamp;
	ButtonPanel Button = new ButtonPanel(floorNum);
	
	Direction moveDirection;
	
	public Floor(int floor, boolean moving, Direction direction) {
		floorNum = floor;
		this.moving = moving;
		upLamp = false;
		downLamp = false;
		this.moveDirection = direction;
        
	}	
	
	public int getFloorNum() { 
		return(this.floorNum); 
	}

	public Direction getDirection() { 
		return(this.moveDirection);
	}
	
	public void setLamp() { 
		if (moving && moveDirection==Direction.UP) {
			upLamp = true; 
			downLamp = false;
		}else if(moving && moveDirection==Direction.DOWN) {
			upLamp = false;
			downLamp = true;
		}else if(!moving && moveDirection==Direction.HOLD) {
			upLamp = false;
			downLamp = false;
		}
		
	}

}