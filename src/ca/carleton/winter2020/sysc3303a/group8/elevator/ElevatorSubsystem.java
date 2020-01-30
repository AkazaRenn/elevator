package ca.carleton.winter2020.sysc3303a.group8.elevator;

import ca.carleton.winter2020.sysc3303a.group8.utils.SortedDeque;

public class ElevatorSubsystem extends Thread {

    private boolean goingUp;
    private boolean isMoving;
    private int currentFloor;
    private int lowestFloor;
    private int highestFloor;
    private SortedDeque<Integer> upQueue;
    private SortedDeque<Integer> downQueue;
    
    public ElevatorSubsystem(int lowestFloor, int highestFloor) {
        goingUp = true;
        isMoving = false;
        currentFloor = 1;
        this.lowestFloor = lowestFloor;
        this.highestFloor = highestFloor;
        upQueue = new SortedDeque<>();
        downQueue = new SortedDeque<>();
    }
    
    public synchronized void addStop(boolean goingUp, int floor) {
        if(goingUp) {
            upQueue.add(floor);
        } else {
            downQueue.add(floor);
        }
    }
    
    private void goToFloor(int floor) {
        if(!isMoving) {
            accelerate();
        }
    }
    
    private void accelerate() {
        
    }
    
    private void decelerate() {
        
    }
    
    private void openDoor() {
        
    }
    
    /**
     * @return the goingUp
     */
    public synchronized boolean isGoingUp() {
        return goingUp;
    }

    /**
     * @return the currentFloor
     */
    public synchronized int getCurrentFloor() {
        return currentFloor;
    }

    @Override
    public void run() {
        if(goingUp) {
            while(currentFloor != upQueue.peek()) {
                //TODO keepGoing
            }
        } else {
            while(currentFloor != downQueue.peek()) {
                //TODO keepGoing
            }
        }
    }
}
