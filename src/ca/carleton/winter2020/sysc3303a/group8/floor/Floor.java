package src;

public class Floor {
	
	int floorNum;
	boolean moving;
	boolean upLamp;
	boolean downLamp;
	ButtonPanel Button = new ButtonPanel(floorNum);
	
	Direction moveDirection;
	
	public Floor(int floor, boolean moving, Direction idle) {
		floorNum = floor;
		this.moving = moving;
		upLamp = false;
		downLamp = false;
		this.moveDirection = idle;
        
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
		}else if(!moving && moveDirection==Direction.IDLE) {
			upLamp = false;
			downLamp = false;
		}
		
	}

}