package ca.carleton.winter2020.sysc3303a.group8.floor;

import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

public class Floor {
	
	public FloorSubsystem FLOORSYS;
	
	int floorNum;
	boolean moving;
	boolean upLamp = false;
	boolean downLamp = false;
	
	public FloorButton UPBUTTON;
	public FloorButton DOWNBUTTON;
	
	Direction moveDirection;
	
	public Floor(FloorSubsystem floorsys,int floor) {
		FLOORSYS = floorsys;
		floorNum = floor;
		UPBUTTON = new FloorButton(this,Direction.UP);
		DOWNBUTTON = new FloorButton(this,Direction.DOWN);
	}	
	
	public int getFloorNum() { 
		return floorNum; 
	}

	public void addStop(Direction direction) {
		FLOORSYS.sendStop(direction,floorNum);
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