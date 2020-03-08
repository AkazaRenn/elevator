package ca.carleton.winter2020.sysc3303a.group8.elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ca.carleton.winter2020.sysc3303a.group8.scheduler.Scheduler;
import ca.carleton.winter2020.sysc3303a.group8.utils.Command;
import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

public class Elevator extends Thread {

	public static final String ACK = "1";
	public static final String CMD = "2";
	public static final String DATA = "3";
	public static final String ERROR = "0";
	public static final String FLOOR_BUTTON = "0x10";						
	public static final String ELEVATOR_ARRIVED = "0x11"; 					
	public static final String UP_PICKUP = "0x33";							
	public static final String UP_DROPOFF = "0x32"; 						
	public static final String DOWN_PICKUP = "0x31";						
	public static final String DOWN_DROPOFF = "0x30";						
	public static final String DOOR_OPEN = "0x3A";							
	public static final String DOOR_CLOSE = "0x3B";							
	public static final String STOP = "0x3C";								
	public static final String ERROR_DOOR_JAM = "0xE0";						
	public static final String ERROR_STUCK = "0xE1";	
	
	private static int elevator_id;
	private ElevatorSubsystem elevatorSubSystem;
	private DatagramSocket sendSocket, receiveSocket;
	private DatagramPacket sendPacket,receivePacket;
	
	public Elevator(int port, int id, int num_floor) {
		this.elevator_id = id;
		elevatorSubSystem = new ElevatorSubsystem(id,num_floor);
		try {
			sendSocket = new DatagramSocket(port);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	public void run() {
		routine();
	}
	
	public static String[] packetToString(byte[] msg) {
		String section = new String(msg);
		String[] ins = section.replaceFirst("\0", "").split("\0");
		return ins;
	}
	
	public void routine() {
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data,data.length);
		String[] msg;
		
		while(true) {
			try {
				System.out.println("Elevator " + elevator_id + ": waiting");
				receiveSocket.receive(receivePacket);
				System.out.println("Elevator " + elevator_id + ": received");
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			msg = packetToString(receivePacket.getData());
			
			switch(msg[0]) {
			
			case CMD:
				switch(msg[1]) {
				
				case UP_DROPOFF:
					break;
				case UP_PICKUP:
					break;
				case DOWN_DROPOFF:
					break;
				case DOWN_PICKUP:
					break;
				case DOOR_OPEN:
					break;
				case DOOR_CLOSE:
					break;
				case STOP:
					break;
				
				}
				
				break;
			case ACK:
				break;
			case DATA:
				break;
			}
		}
	}
}
