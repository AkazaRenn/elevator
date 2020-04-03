package ca.carleton.winter2020.sysc3303a.group8.elevator;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import ElevatorSystem.*;
import junit.framework.*;

public class ElevatorTest extends TestCase{
	private Car e;
	private ElevatorDirection direction;
	private ElevatorStatus status;
	private ElevatorControl control;

	public void setUp() throws Exception {
		
		e = new Car("4", direction.E_UP);
		control = new ElevatorControl(5000, 3, "4");
	}
	
	public void tearDown() throws Exception {
		e = null;
		control = null;
	}

	public void test() {
		assertEquals("4", e.getCurrentFloor());
		assertEquals(4, e.getIntFloor());
		assertEquals(ElevatorDirection.E_UP, e.getDirection());
		Car e2 = new Car("4", direction.E_HOLD); //creating second elevator object
		assertEquals(ElevatorStatus.E_IN_USE, e.getStatus());
		assertEquals(4, control.toInt("4"));
	}
	
	public void TestcreatePacketData() throws UnknownHostException {
		
		String data = "\0" + "1" + "\0" + "3" + "\0"; 
		byte[] dataB = data.getBytes(); 
		DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, InetAddress.getLocalHost(), 5000);
		assertEquals(packet, control.createPacket("1", "3", 5000));
	}
	
	

}
