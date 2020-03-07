package ca.carleton.winter2020.sysc3303a.group8.scheduler;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;


import Scheduler.*;
import junit.framework.*;

public class SchedulerTest extends TestCase{
	private Scheduler s;
	
	public void setUp() throws Exception {
		s = new Scheduler();
		
	}

	public void tearDown() throws Exception {
		s = null;
	}
	
	public void Test() throws UnknownHostException {
		
		String data = "\0" + "1" + "\0" + "3" + "\0";
		byte[] dataB = data.getBytes();
		DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, InetAddress.getLocalHost(), 8008);
		assertEquals(packet, s.createPacket("1", "3", 8008));
		
		assertEquals(8, s.calculateSuitability(7, 5, 5, "IDLE", "IDLE", "WORKING"));
		assertEquals(1, s.calculateSuitability(7, 5, 6, "DOWN", "IDLE", "WORKING"));
		assertEquals(7, s.calculateSuitability(7, 5, 3, "DOWN", "DOWN", "WORKING"));
		assertEquals(6, s.calculateSuitability(7, 5, 3, "DOWN", "IDLE", "WORKING"));
		assertEquals(1, s.calculateSuitability(7, 2, 1, "UP", "UP", "WORKING"));
		assertEquals(5, s.calculateSuitability(7, 2, 5, "UP", "IDLE", "WORKING"));
		assertEquals(6, s.calculateSuitability(7, 2, 5, "UP", "UP", "WORKING"));
		
		String a = "Group 4";
		byte b[] = a.getBytes();
		String ans[] = {"Group 4"};
		assertEquals(ans, s.parsePacket(b));
	}
}