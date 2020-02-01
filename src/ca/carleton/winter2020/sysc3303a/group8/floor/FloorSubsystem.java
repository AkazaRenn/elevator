package ca.carleton.winter2020.sysc3303a.group8.floor;

import java.io.IOException;
import java.util.ArrayList;

import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

/** FloorSubsystem.java
 * This class is the FloorSubsystem side for a simple echo server based on UDP/IP. 
 * The FloorSubsystem sends a character string to the echo server, 
 * then waits for the server to send it back to the Floor Subsystem.
 * @author George
 * */

public class FloorSubsystem {
	int numFloors;
	int upperFloor;
	int lowerFloor;
	
	int bottomFloor = 0;
	int totalFloor = 7;

	
	Direction currentDirection = Direction.HOLD;
	
	static ArrayList<Floor> floors;

	public FloorSubsystem() {
		floors = new ArrayList<Floor>();
		for (int i = 0; i < totalFloor - 1; i++) {
			floors.add(new Floor(i, false, Direction.HOLD));
		}
		
	}

	public static void main(String args[]) throws IOException {
		FloorSubsystem floorSubsystem = new FloorSubsystem();
		System.out.println("floor set");

   }
}
