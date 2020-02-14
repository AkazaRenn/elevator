package ca.carleton.winter2020.sysc3303a.group8.floor;

import java.util.ArrayList;

//import ca.carleton.winter2020.sysc3303a.group8.scheduler.Scheduler;
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

    //private final Scheduler SCHEDULER;
    public final int BOTTOM_FLOOR;
    public final int TOP_FLOOR;
    private int stopNum = 0;
	
	Direction defaultDirection = Direction.HOLD;
	
	public ArrayList<Floor> floors;
//Scheduler scheduler,
	public FloorSubsystem( int bottomFloor, int topFloor) {
		//Send messages to this scheduler
		//SCHEDULER = scheduler;
		BOTTOM_FLOOR = bottomFloor;
		TOP_FLOOR = topFloor;
		stopNum = 0;
		floors = new ArrayList<Floor>(topFloor - bottomFloor + 1);
        for(int i = bottomFloor; i <= topFloor; i++) {
        	floors.add(new Floor(this,i));
        	floors.get(i).setDirecionLamp(defaultDirection);
        }
		
	}
	/*
	 * set a stop floor for elevator
	 */
	public void setStop(Direction direction, int floorNum) {
		//SCHEDULER.receiveStop(direction, floorNum);
		floors.get(floorNum).setDirecionLamp(direction);
		floors.get(floorNum).DetectArrive();
		stopNum++;
	}
	
	/*
	 * detecting whether the elevator arrives a floor
	 * if arrives, set direction lamp and floor number lamp
	 */
	public void ElevatorMoving(int currentFloor) {
		for (Floor floor:floors){
			if(floor.ElevatorArrive()) {
				if(stopNum!= 0 ) { // arrive a stop but still other stop 
					floor.setDirecionLamp(defaultDirection);
					floor.setFloorLamp(currentFloor);
					floor.notArrive();
					stopNum--;
					FloorInfor();
				}else { // arrive a stop and no more stop
					floor.setDirecionLamp(defaultDirection);
					floor.setFloorLamp(currentFloor);
					floor.notArrive();
					FloorInfor();
				}
			} 
			// not a stop
			floor.setFloorLamp(currentFloor);
			floor.notArrive();
		}
	}
	
	public void FloorInfor() {
		System.out.println("floor information: ");
		for (Floor floor:floors){
			System.out.print("current floor: ");
			System.out.println(floor.getFloorNum());
			System.out.print("floor lamp: ");
			System.out.println(floor.getFloorLamp());
			if (floor.getUpLamp()) {
				System.out.println("up lamp on\n");
			}else if(floor.getDownLamp()) {
				System.out.println("down lamp on\n");
			}else {
				System.out.println("no direction lamp on\n");
			}
		}
		System.out.println("end\n");
	}

//	public static void main(String args[]) throws IOException {
//		FloorSubsystem floorSubsystem = new FloorSubsystem();
//		System.out.println("floor set");
//   }
}
