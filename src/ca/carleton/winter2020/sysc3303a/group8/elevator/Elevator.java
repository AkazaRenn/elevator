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
	private DatagramPacket sendPacket,receivePacket,ackPacket;
	public static Elevator elevator1, elevator2;
	
	private static int status = 0;
	private static int send = 0;
	
	public Elevator(int port, int id, int num_floor) {
		elevator_id = id;
		elevatorSubSystem = new ElevatorSubsystem(id,num_floor);
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(port);
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
	
	public static DatagramPacket createPacket(String packetType, String code, int port) {
		String data = "";
		DatagramPacket packet = null;

		if (packetType.equals("1")) {				// ACK
			data = "\0" + Command.ACK + "\0" + code + "\0";
		} else if (packetType.equals("2")) {				//CMD
			data = "\0" + CMD + "\0" + code + "\0";
		} else if (packetType.equals("3")) {				//CMD
			data = "\0" + DATA + "\0" + code + "\0";
		} else if (packetType.equals("0")) {
			data = "\0" + ERROR + "\0" + code + "\0"; // ERROR
		}

		try {			
			packet = new DatagramPacket(data.getBytes(), data.getBytes().length, InetAddress.getLocalHost(), port);
		}catch (UnknownHostException uhe) {	
			System.out.println("ELEVATOR: unable to create packet (UnknownHostException), exiting.");
			System.exit(1);
		}

		return packet;
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
					System.out.println("cmd, UP for drop off");
					sendPacket = createPacket(DATA, Integer.toString(elevatorSubSystem.getCurrentFloor()),receivePacket.getPort());
					System.out.println("ELEVATOR " + elevator_id + ": Elevator moved up, now at Floor " + elevatorSubSystem.getCurrentFloor());
					status = 0;
					send = 1;
					
					break;
				case UP_PICKUP:
					System.out.println("cmd, UP for pick up");
					sendPacket = createPacket(DATA, Integer.toString(elevatorSubSystem.getCurrentFloor()),receivePacket.getPort());
					System.out.println("ELEVATOR " + elevator_id + ": wait for data " + elevatorSubSystem.getCurrentFloor());
					status = 1;
					send = 0;
					
					break;
				case DOWN_DROPOFF:
					System.out.println("cmd, DOWN for drop off");
					sendPacket = createPacket(DATA, Integer.toString(elevatorSubSystem.getCurrentFloor()),receivePacket.getPort());
					System.out.println("ELEVATOR " + elevator_id + ": Elevator moved down, now at Floor " + elevatorSubSystem.getCurrentFloor());
					status = 0;
					send = 1;
					break;
				case DOWN_PICKUP:
					System.out.println("cmd, DOWN for pick up");
					sendPacket = createPacket(DATA, Integer.toString(elevatorSubSystem.getCurrentFloor()),receivePacket.getPort());
					System.out.println("ELEVATOR " + elevator_id + ": wait for data " + elevatorSubSystem.getCurrentFloor());
					status = 1;
					send = 0;
					break;
				case DOOR_OPEN:
					System.out.println("cmd, open door");
					break;
				case DOOR_CLOSE:
					System.out.println("cmd, close door");
					break;
				case STOP:
					System.out.println("cmd, stop");
					break;
				}
				
				ackPacket=createPacket(ACK, msg[1],receivePacket.getPort());
				try {
					sendSocket.send(ackPacket);
				} catch (IOException e1) {
					e1.printStackTrace();
					System.exit(1);
				}
				if (send == 1) {
					try {
						sendSocket.send(sendPacket);
					} catch (IOException e1) {
						e1.printStackTrace();
						System.exit(1);
					}
					send = 0;
				}		
				
				break;
			case ACK:
				System.out.println("ack");
				/*----- ACK packet received -----*/
				switch (msg[1]) {
				case UP_PICKUP:	
					System.out.println("ELEVATOR " + elevator_id + ": Elevator moved UP, now at Floor " + elevatorSubSystem.getCurrentFloor());
					sendPacket = createPacket(DATA, Integer.toString(elevatorSubSystem.getCurrentFloor()),receivePacket.getPort()); 		// send elevator location
					status = 1;				
					break;		// end UP_PICKUP

				case DOWN_PICKUP:
					System.out.println("ELEVATOR " + elevator_id + ": Elevator moved DOWN, now at Floor " + elevatorSubSystem.getCurrentFloor());
					sendPacket = createPacket(DATA, Integer.toString(elevatorSubSystem.getCurrentFloor()),receivePacket.getPort()); 
					status = 1;
					break;		// end DOWN_PICKUP

				case UP_DROPOFF:
					System.out.println("ELEVATOR " + elevator_id + ": Elevator moved UP, now at Floor " + elevatorSubSystem.getCurrentFloor());
					sendPacket = createPacket(DATA, Integer.toString(elevatorSubSystem.getCurrentFloor()),receivePacket.getPort());
					status = 0;
					break;		// end UP_DROPOFF

				case DOWN_DROPOFF:
					System.out.println("ELEVATOR " + elevator_id + ": Elevator moved DOWN, now at Floor " + elevatorSubSystem.getCurrentFloor());
					sendPacket = createPacket(DATA, Integer.toString(elevatorSubSystem.getCurrentFloor()),receivePacket.getPort());
					status = 0;		// elevator job drop off
					break;		// end DOWN_DROPOFF
					
				
				}// end CMD switch
				
				/*--- send elevator location message ---*/
				try {
					sendSocket.send(sendPacket);
				} catch (IOException e1) {
					e1.printStackTrace();
					System.exit(1);
				}
				
				break;		// end ACK
			
			case DATA:
				System.out.println("data");
				/*----- DATA packet received -----*/
				switch (msg[1]) {
				case UP_PICKUP:
					sendPacket = createPacket(DATA, Integer.toString(elevatorSubSystem.getCurrentFloor()),receivePacket.getPort());
					System.out.println("ELEVATOR " + elevator_id + ": Elevator moved UP, now at Floor " + elevatorSubSystem.getCurrentFloor());
					status = 1;							// elevator job pick up
					send = 1;
					break;

				case DOWN_PICKUP:
					sendPacket = createPacket(DATA, Integer.toString(elevatorSubSystem.getCurrentFloor()),receivePacket.getPort());
					System.out.println("ELEVATOR " + elevator_id + ": Elevator moved DOWN, now at Floor " + elevatorSubSystem.getCurrentFloor());
					status = 1;							// elevator job pick up
					send = 1;
					break;

				case STOP:
					status = 1;							// elevator job pick up
					send = 0;
					break;				
				}// end CMD switch
					
				/*--- send ACK message ---*/
				ackPacket = createPacket(ACK, msg[1], receivePacket.getPort());
				try {
					sendSocket.send(ackPacket);
				} catch (IOException e1) {
					e1.printStackTrace();
				    System.exit(1);
				}
				/*--- send elevator location ---*/
				if (send == 1) {
					try {
						sendSocket.send(sendPacket);
					} catch (IOException e1) {
						e1.printStackTrace();
						System.exit(1);
					}
				}// end update location
				break;
			}
		}
	}
	

	public static void main(String[] args) {
		elevator1 = new Elevator(1100,1,20);
		elevator2 = new Elevator(1101,2,20);
		
		System.out.println("Elevator Start");
		elevator1.start();
		elevator2.start();
	}
	
}
