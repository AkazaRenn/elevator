package ca.carleton.winter2020.sysc3303a.group8.elevator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

public class ElevatorSubsystemTest {

    private static final int ELEVATOR_ID = 1;
    private static final int BOTTOM_FLOOR = -2;
    private static final int TOP_FLOOR = 5;
    
    private ElevatorSubsystem elevator;
    
    @BeforeEach
    public void setup() {
        elevator = new ElevatorSubsystem(null, ELEVATOR_ID, BOTTOM_FLOOR, TOP_FLOOR);
    }

    @Test
    public void getAndChangeDirection() {
        assertEquals(elevator.getDirection(), Direction.HOLD);
        elevator.moveUp();
        assertEquals(elevator.getDirection(), Direction.UP);
        elevator.stopMoving();
        assertEquals(elevator.getDirection(), Direction.HOLD);
        elevator.moveDown();
        assertEquals(elevator.getDirection(), Direction.DOWN);
    }

    // @Test
    // public void addAndArriveAtStops() {
    //     elevator.addStop(floor);
    //     elevator.updateCurrentFloor(-1);
    // }

}
