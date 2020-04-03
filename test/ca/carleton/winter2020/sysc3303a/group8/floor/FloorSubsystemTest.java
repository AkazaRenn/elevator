package ca.carleton.winter2020.sysc3303a.group8.floor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.io.*;
import FloorSystem.*;
import FloorSystem.FloorSubsystem.Direction;
import junit.framework.*;

public class FloorTest extends TestCase{
	private Floor floor;
	private FloorSubsystem fs;
	private FloorButtons uih;
	
	public void setUp() throws Exception {
		floor = new Floor(3, 4, true, false);
		fs = new FloorSubsystem();
		uih = new FloorButtons(fs);
	}

	public void tearDown() throws Exception {
		floor = null;
		fs = null;
	}

	public void testFloor() {
		int floorNo = 4;
		int numElev = 3;
		assertEquals(floorNo, floor.getFloorNum());
		assertEquals(numElev, floor.getCarCount());
	}
	

	public void TestcreatePacketData(){
		String ins = "3";
		String data = "\0" + 1 + "\0" + ins + "\0";
		byte[] dataB = data.getBytes();
		assertEquals(dataB, fs.buildData(1, "3"));
	}
	
	
	public void Test() throws SocketException{
		String a = "Group 4";
		byte b[] = a.getBytes();
		String ans[] = {"Group 4"};
		assertEquals(ans, fs.parseMsg(b));
		
		String s = "Group4";
		byte msg[] = s.getBytes();
		DatagramPacket packet = new DatagramPacket(msg, msg.length);
		DatagramSocket socket = new DatagramSocket();
		assertEquals(packet, fs.receive(socket, msg));
	}
	
}
