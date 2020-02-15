package ca.carleton.winter2020.sysc3303a.group8.scheduler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import utils.Direction;
import Scheduler.*;
import SchedulerStates;

class SchedulerTest {
	private Scheduler s;
	
	public void setUp() throws Exception {
		s = new Scheduler(1);		
	}
	public void tearDown() throws Exception {
		s = null;
	}

    @Test
    void test() {
        		assertEquals(SchedulerStates.WAITING, s.getStates());
		s.receiveStop(Direction.DOWN, 2);
		assertEquals(SchedulerStates.R_ELEVATOR, s.getStates());
		s.receiveFromFloor();
		assertEquals(SchedulerStates.R_FLOOR, s.getStates());
    }

}
