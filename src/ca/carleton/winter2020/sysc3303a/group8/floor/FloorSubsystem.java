package ca.carleton.winter2020.sysc3303a.group8.floor;

import java.util.ArrayList;

import ca.carleton.winter2020.sysc3303a.group8.scheduler.Scheduler;
import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

/** FloorSubsystem.java
 * This class is the FloorSubsystem side for a simple echo server based on UDP/IP. 
 * The FloorSubsystem sends a character string to the echo server, 
 * then waits for the server to send it back to the Floor Subsystem.
 * @author Zhi Qiao and Dennis Liu
 * */

public class FloorSubsystem {
	int numFloors;
	int upperFloor;
	int lowerFloor;
	
	int bottomFloor = 0;
	int totalFloor = 7;

    public final Scheduler SCHEDULER;
    public final int BOTTOM_FLOOR;
    public final int TOP_FLOOR;
    public int Stop;
    private int stopNum;
	
	Direction currentDirection = Direction.HOLD;
	
	public ArrayList<Floor> floors;

	public FloorSubsystem(Scheduler scheduler, int bottomFloor, int topFloor) {
		//Send messages to this scheduler
		SCHEDULER = scheduler;
		BOTTOM_FLOOR = bottomFloor;
		TOP_FLOOR = topFloor;
		stopNum = 0;
		floors = new ArrayList<Floor>(topFloor - bottomFloor + 1);
        for(int i = bottomFloor; i <= topFloor; i++) {
        	floors.add(new Floor(this,i));
        	floors.get(i).setDirecionLamp(currentDirection);
        }
		
	}
	
	public void setStop(Direction direction, int floorNum) {
		SCHEDULER.receiveStop(direction, floorNum);
		Stop = floorNum;
		stopNum++;
	}
	
	public boolean ElevatorArrive() {
		for (int i=bottomFloor; i<totalFloor;){
			if(floors.get(i).ElevatorArrive()) {
				if(stopNum!= 0 ) {
					floors.get(i).setDirecionLamp(currentDirection);
					stopNum--;
				}else {
					floors.get(i).setDirecionLamp(Direction.HOLD);
				}
				return true;
			}
			i++;
			return false;
		}
		return false;
	}

//	public static void main(String args[]) throws IOException {
//		FloorSubsystem floorSubsystem = new FloorSubsystem();
//		System.out.println("floor set");
//   }
}
