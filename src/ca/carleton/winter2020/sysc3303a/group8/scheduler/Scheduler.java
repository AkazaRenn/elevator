package ca.carleton.winter2020.sysc3303a.group8.scheduler;


import java.util.ArrayList;
import java.util.List;

import ca.carleton.winter2020.sysc3303a.group8.floor.FloorSubsystem;
import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;
import ca.carleton.winter2020.sysc3303a.group8.scheduler.SchedulerStates;

/**
 * Scheduler subsystem of the project.
 * 
 * @author Changlin (Dennis) Liu 101048980
 */
public class Scheduler extends Thread {

    public final List<Elevator> ELEVATORS;
    public final FloorSubsystem FLOORSYS;
    public final int MAX_FLOOR = 10;
    public final int MIN_FLOOR = 1;
    public final int DELAY = 250;
    private SchedulerStates states;
    
    public Scheduler(int elevatorNumber) {
        ELEVATORS = new ArrayList<>(elevatorNumber);
        for(int id = 0; id < elevatorNumber; id++) {
            ELEVATORS.add(new Elevator(this,id,MIN_FLOOR,MAX_FLOOR));
        }
        FLOORSYS = new FloorSubsystem(MIN_FLOOR,MAX_FLOOR);
        states = SchedulerStates.WAITING;
    }
    
    public void receiveStop(Direction direction, int floor) {
        //TODO some logic to select an elevator
        ELEVATORS.get(0).addStop(direction, floor);
    }

    public synchronized void arriveAtFloor(int elevatorId, int floor) {
        ELEVATORS.get(elevatorId).setCurrentFloor(floor);
        states = SchedulerStates.R_ELEVATOR;
    }

    public void addStop(int elevatorId, int floor) {
        ELEVATORS.get(elevatorId).addStop(floor);
    }

    public void addStop(int elevatorId, int floor, Direction direction) {
        ELEVATORS.get(elevatorId).addStop(direction, floor);
    }
    
    public void setfloorLamp(int elevatorId) {
//        for (int i = MIN_FLOOR; i < MAX_FLOOR; i++){
//            FLOORSYS.get(i).setFloorLamp(ELEVATORS.get(elevatorId).getCurrentFloor());
//            FLOORSYS.get(i).setDirecionLamp(ELEVATORS.get(elevatorId).getDirection());
//        }
    }
    public SchedulerStates getStates(){
        return states;
    }
    
    public void receiveFromFloor(){
        states = SchedulerStates.R_FLOOR;
    }

    @Override
    public void run() {
        //TODO
    }
    
}
