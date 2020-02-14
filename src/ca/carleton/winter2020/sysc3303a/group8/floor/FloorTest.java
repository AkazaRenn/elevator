package ca.carleton.winter2020.sysc3303a.group8.floor;

//import ca.carleton.winter2020.sysc3303a.group8.scheduler.Scheduler;
import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

public class FloorTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Scheduler scheduler = new Scheduler(5);
		FloorSubsystem floors = new FloorSubsystem(0, 2);
		
		// Test UP 
		floors.FloorInfor();
		floors.setStop(Direction.UP, 1);
		floors.FloorInfor();
		floors.setStop(Direction.UP, 2);
		floors.FloorInfor();
		floors.ElevatorMoving(0);
		floors.ElevatorMoving(1);
		floors.ElevatorMoving(2);
		
		
		// Test Down
		floors.setStop(Direction.DOWN, 0);
		floors.FloorInfor();
		floors.ElevatorMoving(1);

	}

}
