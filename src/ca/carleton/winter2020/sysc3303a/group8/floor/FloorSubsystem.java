package ca.carleton.winter2020.sysc3303a.group8.floor;

import java.util.ArrayList;

import ca.carleton.winter2020.sysc3303a.group8.scheduler.Scheduler;
import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

/** FloorSubsystem.java
 * This class is the FloorSubsystem side for a simple echo server based on UDP/IP. 
 * The FloorSubsystem sends a character string to the echo server, 
 * then waits for the server to send it back to the Floor Subsystem.
 * @author George and Dennis Liu
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
	
	Direction currentDirection = Direction.HOLD;
	
	static ArrayList<Floor> floors;

	public FloorSubsystem(Scheduler scheduler, int bottomFloor, int topFloor) {
		//Send messages to this scheduler
		SCHEDULER = scheduler;
		BOTTOM_FLOOR = bottomFloor;
		TOP_FLOOR = topFloor;
		
		floors = new ArrayList<Floor>(topFloor - bottomFloor + 1);
        for(int i = bottomFloor; i <= topFloor; i++) {
        	floors.add(new Floor(this,i));
        }
		
	}
	
	public void sendStop(Direction direction, int floorNum) {
		SCHEDULER.receiveStop(direction, floorNum);
	}
	

//	public static void main(String args[]) throws IOException {
//		FloorSubsystem floorSubsystem = new FloorSubsystem();
//		System.out.println("floor set");
//   }
}
