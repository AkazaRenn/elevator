package ca.carleton.winter2020.sysc3303a.group8.floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.carleton.winter2020.sysc3303a.group8.utils.Command;
import ca.carleton.winter2020.sysc3303a.group8.utils.Direction;

/** FloorSubsystem.java
 * This class is the FloorSubsystem side for a simple echo server based on UDP/IP. 
 * The FloorSubsystem sends a character string to the echo server, 
 * then waits for the server to send it back to the Floor Subsystem.
 * @author Zhi Qiao and Dennis Liu
 * @author Frank Xu
 * */

public class FloorSubsystem {

	DatagramPacket sendPacket;
	DatagramPacket receivePacket;
	DatagramSocket floorSocket;
	/* ## HEADER AND COMMAND IDENTIFIERS ## */
	final int ACK = 1;
	final int CMD = 2;
	final int DATA = 3;
	final int ERROR = 0;

	final int FLOORPORT = 1200;					// port of floor subsystem
	final int SCHEDPORT = 1000;					// port of scheduler
	
	 public int BOTTOM_FLOOR;
	 public int TOP_FLOOR;
	 private int ELEVATOR_COUNT;
	
	int floorMin = 0;
	int floorTotal = 22;
	int elevatorTotal = 4;
	
	Direction currentDirection = Direction.HOLD;
	int requestCount = 0;
	
	ArrayList<Floor> floors = new ArrayList<Floor>();	

	public FloorSubsystem() {
			
        try {
        	floorSocket = new DatagramSocket(FLOORPORT);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
		
	}
	
	public void createFloors(int bottomFloor, int topFloor, int carNum) {
		BOTTOM_FLOOR = bottomFloor;
        TOP_FLOOR = topFloor;
        ELEVATOR_COUNT = carNum;
        
        floors = new ArrayList<Floor>(topFloor - bottomFloor + 1);
        for(int i = bottomFloor; i <= topFloor; i++) {
                Floor newFloor = new Floor(this, i, carNum, 1);
                newFloor.setDirecionLamp(Direction.HOLD);
                floors.add(newFloor);
        }
	}
			
			

	/**
	 * Places string in a byte array for sending
	 * 
	 * @param packetType, ins
	 * @param byte array of data
	 */
	public byte[] createPacketData(int packetType, String ins) {
		
		String data;
		
		// error
		if (packetType == 0) {			
			data = "\0" + ERROR + "\0" + ins + "\0";					
		}
		// ack
		else if (packetType == 1) {			
			data = "\0" + ACK + "\0" + ins + "\0";
		}
		// cmd
		else if (packetType == 2) {
			data = "\0" + CMD + "\0" + ins + "\0";
		}
		// data
		else {
			data = "\0" + DATA + "\0" + ins + "\0";
		}		
		
		return data.getBytes();
	}	

	/**
	 * Converts byte array into string array using default charset.
	 * data[0] is the header, data[1] is the data or command (ex "0x10")
	 * 
	 * @param msg
	 * @return str
	 */
	public String[] readPacketData(byte[] msg) {
		// Converts byte array into string array using default charset.
		// data[0] is the header, data[1] is the data or command (ex "0x10")
		String data = new String(msg);
		String[] str;
		str = data.replaceFirst("\0", "").split("\0");
		
		return str;
	}
	

	/**
	 * Set a datagram packet and block until message is received
	 * 
	 * @param socket, msg
	 * @return packet
	 */
	public DatagramPacket receive(DatagramSocket socket, byte[] msg) {
		DatagramPacket packet = new DatagramPacket(msg, msg.length);

		// Block until a datagram packet is received
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return packet;
	}


	/**
	 * Create a service request to send to the scheduler, setting lamps as necessary
	 * 
	 * @param start, dest, direction
	 * @return msg
	 */
	public byte[] createServiceRequest(String time, int start, int dest, Direction direction) {
		
		byte msg[] = new byte[100];
		String message = time + " " + start + " " + direction + " " + dest;
		msg = createPacketData(DATA, message);
		
		System.out.println("Floor Subsystem: Sending elevator request to go from floor " + 
								start + " to " + dest + ", heading " + direction + ". Turning direction lamp on.");
		 
		return msg;
	}	

	/**
	 * Create and send a message
	 * 
	 * @param msg, port
	 * 
	 */
	public void send(byte[] msg, int port, DatagramSocket socket) {
		
		//create a service request message
		try {
	        sendPacket = new DatagramPacket(msg, msg.length,
	                                        InetAddress.getLocalHost(), port);
	     } catch (UnknownHostException e) {
	        e.printStackTrace();
	        System.exit(1);
	     }
		 
		//send the service request message
		try {
	        socket.send(sendPacket);

	     } catch (IOException e) {
	        e.printStackTrace();
	        System.exit(1);
	     }
		 //System.out.println("Floor Subsystem: Sending ");
		 System.out.println(String.format("Floor Subsystem: Sending packet ( string >> %s, byte array >> %s ).", 
				 new String(sendPacket.getData()), sendPacket.getData()));
		 
		
	}
	

	/**
	 * Send a service request containing the start floor, destination floor and target direction
	 * 
	 * @param start, dest, dir
	 * 
	 */
	public void sendServiceRequest(String time, int start, int dest, Direction dir, DatagramSocket socket) {
		
		byte[] buffer = new byte[100];
		byte[] response = new byte[100];
		String[] msg = new String[2];
		String[] data = new String[2];
		String[] acknowledgment = new String[2];
		buffer = createPacketData(CMD, "0x10");
		send(buffer, SCHEDPORT, socket);
		System.out.println("Floor Subsystem: Requesting to send elevator input. Waiting for acknowledgment...");
//		Block until acknowledgment message is received
		receive(socket, buffer);
		msg = readPacketData(buffer);
		if (Integer.parseInt(msg[0]) == ACK) {
			if (msg[1].equals("0x10")) {
				System.out.println("Floor Subsystem: CMD acknowledgment received. Sending input to Scheduler");
				response = createServiceRequest(time, start, dest, dir);
				send(response, SCHEDPORT, socket);
				System.out.println("Floor Subsystem: Waiting for acknowledgment of data packet...");
//				Block until acknowledgment message is received
				receive(socket, buffer);
				data = readPacketData(buffer);
				acknowledgment = readPacketData(response);
				System.out.println("Floor Subsystem: Data packet acknowledged. Scheduler data is: " + data[1]);
			}
		}
	}
	
	/**
	 * Listen for messages sent from the scheduler
	 * 
	 * @param ins
	 * @param request
	 */
	public void running() {
		boolean listening = true;
		byte[] buffer = new byte[100];
		String[] data = new String[2];
		int tempPort = 0;
		
		while (listening) {
			try {
//				Block until Scheduler starts exchange signaling elevator has arrived
				tempPort = receive(floorSocket, buffer).getPort();
				data = readPacketData(buffer);
				if (Integer.parseInt(data[0]) == CMD) {
					//cmdRequest(data, tempPort);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				listening = false;
			}
		}
	}	


    public static void main(String[] args) throws Throwable {
    	FloorSubsystem system = new FloorSubsystem();
    	system.createFloors(1, 20, 2);
		Thread inputHandler;
		inputHandler = new Thread(new UserInputHandler(system), "User Input Handler");
		inputHandler.start();
		system.running();
    }
}
