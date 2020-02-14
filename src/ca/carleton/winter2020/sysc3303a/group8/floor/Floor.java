package ca.carleton.winter2020.sysc3303a.group8.floor;

import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

/**
 * floor information
 * 
 * @author Zhi Qiao 101026761 
 * @author Dennis Liu
 */

public class Floor {
	
	public FloorSubsystem FLOORSYS;
	
	private final int floorNum;
	private int floorLamp;
	
	private boolean upLamp;
	private boolean downLamp;
	
	private FloorButton upButton;
	private FloorButton downButton;
	
	private boolean ArriveSensor; 
	
	public Floor(FloorSubsystem floorsys,int floor) {
		FLOORSYS = floorsys;
		floorNum = floor;
		floorLamp = floor;
		upLamp = false;
		downLamp = false;
		upButton = new FloorButton(this,Direction.UP);
		downButton = new FloorButton(this,Direction.DOWN);
		ArriveSensor = false;
	}	
	
	public int getFloorNum() { 
		return floorNum; 
	}
	
	public int getFloorLamp() { 
		return floorLamp; 
	}
	
	public boolean getUpLamp() { 
		return upLamp; 
	}
	public boolean getDownLamp() { 
		return downLamp; 
	}
	
	public void setFloorLamp(int elevatorCurrentFloor) {
		floorLamp = elevatorCurrentFloor;
	}
	
	public void setDirecionLamp(Direction elevatorMoveDirection) { 
		if (elevatorMoveDirection==Direction.UP) {
			upLamp = true; 
			downLamp = false;
		}else if(elevatorMoveDirection==Direction.DOWN) {
			upLamp = false;
			downLamp = true;
		}else if(elevatorMoveDirection==Direction.HOLD) {
			upLamp = false;
			downLamp = false;
		}		
	}
	
	public void ButtonPressed(FloorButton button) {
		setDirecionLamp(button.getDirection());
		FLOORSYS.setStop(button.getDirection(),floorNum);
	}
	
	public void DetectArrive() {
		this.ArriveSensor = true;
	}
	
	public void notArrive() {
		this.ArriveSensor = false;
	}
	
	public boolean ElevatorArrive(){
		return ArriveSensor;
	}

}
