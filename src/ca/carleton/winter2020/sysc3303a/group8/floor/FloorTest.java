package ca.carleton.winter2020.sysc3303a.group8.floor;

//import ca.carleton.winter2020.sysc3303a.group8.scheduler.Scheduler;
import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

public class FloorTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Scheduler scheduler = new Scheduler(5);
		FloorSubsystem floors = new FloorSubsystem(0, 6);
		// Test UP 
		floors.FloorInfor(0);
		System.out.println("set first stop at 2");
		floors.setStop(Direction.UP, 2);
		floors.FloorInfor(0);
		System.out.println("set secoond stop at 4");
		floors.setStop(Direction.UP, 4);
		floors.FloorInfor(0);
		// move to top floor
		System.out.println("move to top floor");
		for(int i =0 ;i<=6;i++) {
			floors.ElevatorMoving(i);
		}
		floors.FloorInfor(6);
		
		
		// Test Down
		System.out.println("set secoond stop at 4");
		floors.setStop(Direction.DOWN, 4);
		floors.FloorInfor(6);
		for(int i =6 ;i>=4;i--) {
			floors.ElevatorMoving(i);
		}
		floors.FloorInfor(4);

	}

}
