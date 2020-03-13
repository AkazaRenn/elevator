package ca.carleton.winter2020.sysc3303a.group8.floor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.carleton.winter2020.sysc3303a.group8.utils.Command;
import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

/**
 * floor information
 * 
 * @author Zhi Qiao 101026761 
 * @author Dennis Liu
 * @author Frank Xu
 */

public class Floor {
    
    private final FloorSubsystem FLOORSYS;
    
    private final int FLOOR_NUM;
    private final int CAR_COUNT;
    
    private List<Integer> floorLamps;
    
    private boolean upLamp;
    private boolean downLamp;
    
    private FloorButton upButton;
    private FloorButton downButton;
    
    private boolean arrived; 
    
    public Floor(FloorSubsystem floorsys, int floorNum, int carCount, int initialFloor) {
        FLOORSYS = floorsys;
        FLOOR_NUM = floorNum;
        CAR_COUNT = carCount;
        floorLamps = new ArrayList<Integer>(carCount);
        for(int i = 0; i < carCount; i++) {
            floorLamps.add(initialFloor);
        }
        upLamp = false;
        downLamp = false;
        upButton = new FloorButton(this,Direction.UP);
        downButton = new FloorButton(this,Direction.DOWN);
        arrived = false;
    }    
    
    public int getFloorNum() { 
        return FLOOR_NUM; 
    }
    
    public int getFloorLamp(int carNum) { 
        return floorLamps.get(carNum); 
    }
    
    public boolean getUpLamp() { 
        return upLamp; 
    }
    
    public boolean getDownLamp() { 
        return downLamp; 
    }
    
    public void setFloorLamp(int elevatorCurrentFloor, int carNum) {
        floorLamps.set(carNum, elevatorCurrentFloor);
    }
    
    public void setDirecionLamp(Direction defaultDirection) { 
        if (defaultDirection==Direction.UP) {
            upLamp = true; 
            downLamp = false;
        }else if(defaultDirection==Direction.DOWN) {
            upLamp = false;
            downLamp = true;
        }else if(defaultDirection==Direction.HOLD) {
            upLamp = false;
            downLamp = false;
        }        
    }
    
    public void buttonPressed(FloorButton button) {
//        setDirecionLamp(button.getDirection());
        //FLOORSYS.setStop(button.getDirection(),FLOOR_NUM);
    }
    
    public void detectArrive() {
        this.arrived = true;
    }
    
    public void notArrive() {
        this.arrived = false;
    }
    
    public boolean ElevatorArrive(){
        return arrived;
    }

}
