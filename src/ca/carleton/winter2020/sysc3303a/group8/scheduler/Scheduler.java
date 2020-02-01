package ca.carleton.winter2020.sysc3303a.group8.scheduler;


import ca.carleton.winter2020.sysc3303a.group8.elevator.ElevatorSubsystem;
import ca.carleton.winter2020.sysc3303a.group8.floor.FloorSubsystem;
import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

/**
 * Scheduler subsystem of the project.
 * 
 * @author Changlin (Dennis) Liu 101048980
 */
public class Scheduler {

	public final ElevatorSubsystem ELEVATOR;
	public final FloorSubsystem FLOORSYS;
	public final int MAX_FLOOR = 10;
	public final int MIN_FLOOR = 1;
	public final int DELAY = 250;
	public final int ELEVATORID = 5;
	
	
	public Scheduler() {
		ELEVATOR = new ElevatorSubsystem(this,ELEVATORID,MIN_FLOOR,MAX_FLOOR);
		FLOORSYS = new FloorSubsystem(this,MIN_FLOOR,MAX_FLOOR);
	}
	
	public void run() {
		
	}
	
	public void receiveStop(Direction direction, int floor) {
		ELEVATOR.addStop(direction, floor);
	}
	
}
