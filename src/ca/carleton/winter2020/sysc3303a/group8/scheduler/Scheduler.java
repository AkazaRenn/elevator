package ca.carleton.winter2020.sysc3303a.group8.scheduler;


import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import ca.carleton.winter2020.sysc3303a.group8.utils.Command;




public class Scheduler {							

	final int HOSTPORT = 1000;
	final int EPORT1 = 1100;
	final int EPORT2 = 1101;
	final int FPORT = 1200;
	DatagramSocket hostSocket;											
	DatagramSocket floorSocket;											

	public static final int FLOORS = 6;	
	//elevator1
	private BlockingQueue<String> upQ1;									
	private BlockingQueue<String> downQ1;	
	private ElevatorHandler handler1;
	//elevator2
	private BlockingQueue<String> upQ2;										
	private BlockingQueue<String> downQ2;									
	private ElevatorHandler handler2;

	
	public Scheduler() {		
		try {
			hostSocket = new DatagramSocket(HOSTPORT);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}		
		upQ1 = new ArrayBlockingQueue<String>(100);
		downQ1 = new ArrayBlockingQueue<String>(100);
		handler1 = new ElevatorHandler(EPORT1, FPORT, upQ1, downQ1, 1, "IDLE", 1);
		
		upQ2 = new ArrayBlockingQueue<String>(100);
		downQ2 = new ArrayBlockingQueue<String>(100);			
		handler2 = new ElevatorHandler(EPORT2, FPORT, upQ2, downQ2, 1, "IDLE", 2);
	}
	
	
	public void run() {
		System.out.println("main: running.");
		boolean listening = true;		
		byte[] buffer = new byte[8];
		DatagramPacket rPacket = new DatagramPacket(buffer, buffer.length);			
		handler1.start();
		handler2.start();

		while (listening) {
			try {
				hostSocket.receive(rPacket);
				System.out.println(String.format("main: received packet ( string >> %s, byte array >> %s ).\n", new String(rPacket.getData()), rPacket.getData()));
				String[] rPacketParsed = parsePacket(rPacket.getData());
				
				if (rPacketParsed[0].equals(Byte.toString(Command.CMD.getCommandByte()))) {					
					
					handleFloorCommand(rPacketParsed[1], rPacket.getPort());				
				}
				else {
					System.out.println("main: unknown packet.");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				listening = false;
			}			
		}
		hostSocket.close();
		return;
	}
	
	
	
	public void handleFloorCommand(String cmd, int port) {
		
		byte[] data = new byte[1024];
		DatagramPacket aPacket = createPacket(Command.ACK, cmd, port);
		DatagramPacket dPacket = new DatagramPacket(data, data.length);
				
		if (cmd.equals(Byte.toString(Command.FLOOR_BUTTON.getCommandByte()))) {
			try {
				floorSocket = new DatagramSocket();
				System.out.println("main: acking.");
				floorSocket.send(aPacket);
				
				hostSocket.receive(dPacket);
				
				String[] dPacketParsed = parsePacket(dPacket.getData());				
				System.out.println(String.format("main: received data ( string >> %s, byte array >> %s ).\n", new String(dPacket.getData()), dPacket.getData()));
				System.out.println(Arrays.toString(dPacketParsed));
				
				if (dPacketParsed[0].equals(Byte.toString(Command.DATA.getCommandByte()))) {
					aPacket = createPacket(Command.ACK, dPacketParsed[1], port);
					System.out.println("main: acking data.");
					floorSocket.send(aPacket);
									
					String[] temp = dPacketParsed[1].split(" ");
					
					int FS1 = calculateSuitability(FLOORS, handler1.currentFloor, Integer.parseInt(temp[1]), handler1.currentDirection, temp[2], handler1.status);
					int FS2 = calculateSuitability(FLOORS, handler2.currentFloor, Integer.parseInt(temp[1]), handler2.currentDirection, temp[2], handler2.status);
					
					int maxFS = Math.max(FS1, FS2);
					
					if (maxFS == FS1) {
						if (temp[2].equals("UP")) {
							upQ1.add(dPacketParsed[1]);
						}
						else {
							downQ1.add(dPacketParsed[1]);
						}						
					}
					else{
						if (temp[2].equals("UP")) {
							upQ2.add(dPacketParsed[1]);
						}
						else {
							downQ2.add(dPacketParsed[1]);
						}										
					}				
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				System.exit(1);				
			}						
		}
	}
	
	

	public static int calculateSuitability(int n, int currentFloor, int requestedFloor, String currentElevatorDir, String requestedDir, String status) {
		
		int calculated = 0;
		int distance = currentFloor - requestedFloor; // if -ive, call is above, if +ive, call is below
		
		if (status.equals("SUSPENDED")) {
			return -1;
		}
		
		if (currentElevatorDir.equals("IDLE")) {
			distance = Math.abs(distance);
			calculated = n + 1 - distance;
		}
		else if (currentElevatorDir.equals("DOWN")) {
			if (distance < 0) {
				calculated = 1;
			}
			else if (distance > 0 && requestedDir.equals(currentElevatorDir)) {
				calculated = n + 2 - distance;
			}
			else if (distance > 0 && !requestedDir.equals(currentElevatorDir)) {
				calculated = n + 1 - distance;
			}
		}
		else if (currentElevatorDir.equals("UP")) {
			if (distance > 0) {
				calculated = 1;
			} 
			else if (distance < 0 && requestedDir.equals(currentElevatorDir)) {
				distance = Math.abs(distance);
				calculated = n + 2 - distance;
			}
			else if (distance < 0 && !requestedDir.equals(currentElevatorDir)) {
				distance = Math.abs(distance);
				calculated = n + 1 - distance;
			}
		}
		return calculated; 
	}
	
	
	public static String[] parsePacket(byte[] bytes) {
		
		String packet = new String(bytes);
		String[] parsed = packet.replaceFirst("\0", "").split("\0");
		
		return parsed;		
	}
	
	
	public static DatagramPacket createPacket(Command ack2, String ins, int port) {
		String data;
		DatagramPacket packet = null;
		String ack =Byte.toString(ack2.getCommandByte());
		
		if (ack == "0") {			
			data = "\0" + Command.ERROR + "\0" + ins + "\0";					
		}
		else if (ack == "1") {			
			data = "\0" + Command.ACK + "\0" + ins + "\0";
		}
		else if (ack == "2") {
			data = "\0" + Command.CMD + "\0" + ins + "\0";
		}
		else {
			data = "\0" + Command.DATA + "\0" + ins + "\0";
		}		
		
		try {			
			packet = new DatagramPacket(data.getBytes(), data.getBytes().length, InetAddress.getLocalHost(), port);
		}
		catch (UnknownHostException uhe) {			
			System.out.println("unable to create packet (UnknownSchedulerException), exiting.");
			System.exit(1);
		}
		return packet;
	}

	
	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		scheduler.run();
	}
}




class ElevatorHandler extends Thread {
	
	private DatagramSocket eSocket;					
	private int eport;								
	private int fport;								
	private BlockingQueue<String> upQ;			
	private BlockingQueue<String> downQ;			
	
	protected volatile String currentDirection;		
	protected volatile int currentFloor;			
	protected volatile String status;				
	protected int id;
	
	
	public ElevatorHandler(int eport, int fport, BlockingQueue<String> upQ, BlockingQueue<String> downQ, int currentFloor, String currentDirection, int id) {
		super("request thread");
		try {
			eSocket = new DatagramSocket();
			eSocket.setSoTimeout(10000);
			this.eport = eport;
			this.fport = fport;
			this.upQ = upQ;
			this.downQ = downQ;
			this.currentFloor = currentFloor;
			this.currentDirection = currentDirection;
			this.status = "WORKING";
			this.id = id;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}	
	
	
	@Override
	public void run() {
		
		boolean running = true;
		String pIns, dIns;
		String srcFloor, direction;
		String[] parsedData;
		System.out.println("sub: a new thread is running.");		
				
		while (running) {
			
			String request = upQ.poll();
			
			if (request == null) {
				request = downQ.poll();
			}
			
			if (request != null && status.equals("WORKING")) {
				parsedData = request.split(" ");
				srcFloor = parsedData[1];
				direction = parsedData[2];			
				
				if (Integer.parseInt(srcFloor) > currentFloor) {
					pIns = Byte.toString(Command.UP_PICKUP.getCommandByte());
					currentDirection = "UP";
				}
				else if (Integer.parseInt(srcFloor) < currentFloor) {
					pIns = Byte.toString(Command.DOWN_PICKUP.getCommandByte());
					currentDirection = "DOWN";
				}
				else {
					pIns = Byte.toString(Command.STOP.getCommandByte());	// elevator is already there			
				}
				performPickup(pIns, request);
				
				if (status.equals("WORKING")) {
					// drop off direction
					if (direction.equals("UP")) {
						dIns = Byte.toString(Command.UP_DROPOFF.getCommandByte());
						currentDirection = "UP";
					}
					else { 
						dIns = Byte.toString(Command.DOWN_DROPOFF.getCommandByte());
						currentDirection = "DOWN";
					}		
					performDropoff(dIns, request);					
				}				
			}
			else {
				continue;
			}
		}			
		eSocket.close();
		return;
	}
	
	
	public void performPickup(String ins, String request) {
		
		String srcFloor, destFloor;
		String[] parsedData;
		boolean keepMoving = (ins.equals(Byte.toString(Command.STOP.getCommandByte())) ? false : true); 
		String error = Byte.toString(Command.ERROR_STUCK.getCommandByte());
		
		parsedData = request.split(" ");
		srcFloor = parsedData[1];
		destFloor = parsedData[3];
		
		byte[] buffer = new byte[8];
		DatagramPacket cPacket = Scheduler.createPacket(Command.CMD, ins, eport);
		DatagramPacket aPacket = new DatagramPacket(buffer, buffer.length);
		DatagramPacket dPacket = Scheduler.createPacket(Command.DATA, destFloor, eport);
		DatagramPacket rPacket = new DatagramPacket(buffer, buffer.length);
		DatagramPacket ePacket = new DatagramPacket(buffer, buffer.length);
		String[] rPacketParsed;
		
		System.out.println(String.format("sub-%d: forwarding command packet ( string >> %s, byte array >> %s ).", this.id, new String(cPacket.getData()), cPacket.getData()));
		
		try {
			try {			
				eSocket.send(cPacket);	
				eSocket.receive(aPacket);
				String[] aPacketParsed = Scheduler.parsePacket(aPacket.getData());				
				System.out.println(String.format("sub-%d: received ack ( string >> %s, byte array >> %s ).", this.id, new String(aPacket.getData()), aPacket.getData()));
				System.out.println(Arrays.toString(aPacketParsed));
				System.out.println(String.format("sub-%d: sending elevator button ( string >> %s, byte array >> %s ).", this.id, new String(dPacket.getData()), dPacket.getData()));
				eSocket.send(dPacket);
				eSocket.receive(aPacket);
				aPacketParsed = Scheduler.parsePacket(aPacket.getData());				
				System.out.println(String.format("sub-%d: received ack ( string >> %s, byte array >> %s ).", this.id, new String(aPacket.getData()), aPacket.getData()));
				System.out.println(Arrays.toString(aPacketParsed));
				
				while (keepMoving) {
					eSocket.receive(rPacket);
					
					rPacketParsed = Scheduler.parsePacket(rPacket.getData());				
					System.out.println(String.format("sub-%d: received positional update ( string >> %s, byte array >> %s ).", this.id, new String(rPacket.getData()), rPacket.getData()));
					System.out.println(Arrays.toString(rPacketParsed));
					
					currentFloor = Integer.parseInt(rPacketParsed[1]);
					if (rPacketParsed[1].equals(srcFloor)) {
						cPacket = Scheduler.createPacket(Command.CMD, Byte.toString(Command.STOP.getCommandByte()), eport);
						System.out.println(String.format("sub-%d: sending stop ( string >> %s, byte array >> %s ).\n", this.id, new String(cPacket.getData()), cPacket.getData()));
						keepMoving = false;
					}
					else {
						cPacket = Scheduler.createPacket(Command.ACK, rPacketParsed[1], eport);
						System.out.println(String.format("sub-%d: sending continue ( string >> %s, byte array >> %s ).\n", this.id, new String(cPacket.getData()), cPacket.getData()));					
					}				
					eSocket.send(cPacket);
					
					if (!keepMoving) {
						eSocket.receive(aPacket);
						aPacketParsed = Scheduler.parsePacket(aPacket.getData());				
						System.out.println(String.format("sub-%d: received ack ( string >> %s, byte array >> %s ).", this.id, new String(aPacket.getData()), aPacket.getData()));
						System.out.println(Arrays.toString(aPacketParsed));	
					}
				}					
				
				sendPositionToFloor(srcFloor);
				
				error = Byte.toString(Command.ERROR_DOOR_JAM.getCommandByte());
				
				cPacket = Scheduler.createPacket(Command.CMD, Byte.toString(Command.DOOR_OPEN.getCommandByte()), eport);
				System.out.println(String.format("sub-%d: sending open door ( string >> %s, byte array >> %s ).\n", this.id, new String(cPacket.getData()), cPacket.getData()));
				eSocket.send(cPacket);
				
				eSocket.receive(aPacket);
				
				aPacketParsed = Scheduler.parsePacket(aPacket.getData());				
				System.out.println(String.format("sub-%d: received ack ( string >> %s, byte array >> %s ).", this.id, new String(aPacket.getData()), aPacket.getData()));
				System.out.println(Arrays.toString(aPacketParsed));
				
				cPacket = Scheduler.createPacket(Command.CMD, Byte.toString(Command.DOOR_CLOSE.getCommandByte()), eport);
				System.out.println(String.format("sub-%d: sending close door ( string >> %s, byte array >> %s ).\n", this.id, new String(cPacket.getData()), cPacket.getData()));
				eSocket.send(cPacket);
				
				eSocket.receive(aPacket);
				
				aPacketParsed = Scheduler.parsePacket(aPacket.getData());				
				System.out.println(String.format("sub-%d: received ack - done ( string >> %s, byte array >> %s ).", this.id, new String(aPacket.getData()), aPacket.getData()));
				System.out.println(Arrays.toString(aPacketParsed));		
			}
			catch (SocketTimeoutException ste) {
				System.out.println(String.format("sub-%d: error encountered, taking elevator out of operation.", this.id));
				ePacket = Scheduler.createPacket(Command.ERROR, error, eport);							
				eSocket.send(ePacket);
				
				eSocket.receive(aPacket);
				status = "SUSPENDED";
				
				return;
			}
		}
		catch (IOException ioe) {
			System.out.println(String.format("sub-%d: unable to communicate with elevator system, quitting.", this.id));
			ioe.printStackTrace();
			System.exit(1);
		}		
	}
	
	
	public void performDropoff(String ins, String request) {
		String destFloor;
		String[] parsedData;
		boolean keepMoving = true;
		String error = Byte.toString(Command.ERROR_STUCK.getCommandByte());
		
		parsedData = request.split(" ");
		destFloor = parsedData[3];
		
		byte[] buffer = new byte[8];
		DatagramPacket cPacket = Scheduler.createPacket(Command.CMD, ins, eport);
		DatagramPacket aPacket = new DatagramPacket(buffer, buffer.length);
		DatagramPacket rPacket = new DatagramPacket(buffer, buffer.length);
		DatagramPacket ePacket = new DatagramPacket(buffer, buffer.length);
		String[] rPacketParsed;
		
		System.out.println(String.format("sub-%d: forwarding command packet ( string >> %s, byte array >> %s ).", this.id, new String(cPacket.getData()), cPacket.getData()));
		
		try {
			try {
				eSocket.send(cPacket);		
				eSocket.receive(aPacket);
				String[] aPacketParsed = Scheduler.parsePacket(aPacket.getData());				
				System.out.println(String.format("sub-%d: received ack ( string >> %s, byte array >> %s ).", this.id, new String(aPacket.getData()), aPacket.getData()));
				System.out.println(Arrays.toString(aPacketParsed));
				
				while (keepMoving) {
					eSocket.receive(rPacket);
					
					rPacketParsed = Scheduler.parsePacket(rPacket.getData());				
					System.out.println(String.format("sub-%d: received positional update ( string >> %s, byte array >> %s ).", this.id, new String(rPacket.getData()), rPacket.getData()));
					System.out.println(Arrays.toString(rPacketParsed));
					
					currentFloor = Integer.parseInt(rPacketParsed[1]);
					if (rPacketParsed[1].equals(destFloor)) {
						cPacket = Scheduler.createPacket(Command.CMD, Byte.toString(Command.STOP.getCommandByte()), eport);
						System.out.println(String.format("sub-%d: sending stop ( string >> %s, byte array >> %s ).\n", this.id, new String(cPacket.getData()), cPacket.getData()));
						keepMoving = false;
					}
					else {
						cPacket = Scheduler.createPacket(Command.ACK, rPacketParsed[1], eport);
						System.out.println(String.format("sub-%d: sending continue ( string >> %s, byte array >> %s ).\n", this.id, new String(cPacket.getData()), cPacket.getData()));					
					}				
					eSocket.send(cPacket);
				}
				
				eSocket.receive(aPacket);
				
				
				aPacketParsed = Scheduler.parsePacket(aPacket.getData());				
				System.out.println(String.format("sub-%d: received ack ( string >> %s, byte array >> %s ).", this.id, new String(aPacket.getData()), aPacket.getData()));
				System.out.println(Arrays.toString(aPacketParsed));
				
				error = Byte.toString(Command.ERROR_DOOR_JAM.getCommandByte());
				
				cPacket = Scheduler.createPacket(Command.CMD, Byte.toString(Command.DOOR_OPEN.getCommandByte()), eport);
				System.out.println(String.format("sub-%d: sending open door ( string >> %s, byte array >> %s ).\n", this.id, new String(cPacket.getData()), cPacket.getData()));
				eSocket.send(cPacket);
				
				eSocket.receive(aPacket);
				
				aPacketParsed = Scheduler.parsePacket(aPacket.getData());				
				System.out.println(String.format("sub-%d: received ack ( string >> %s, byte array >> %s ).", this.id, new String(aPacket.getData()), aPacket.getData()));
				System.out.println(Arrays.toString(aPacketParsed));
				
				cPacket = Scheduler.createPacket(Command.CMD, Byte.toString(Command.DOOR_CLOSE.getCommandByte()), eport);
				System.out.println(String.format("sub-%d: sending close door ( string >> %s, byte array >> %s ).\n", this.id, new String(cPacket.getData()), cPacket.getData()));
				eSocket.send(cPacket);
				
				eSocket.receive(aPacket);
				
				aPacketParsed = Scheduler.parsePacket(aPacket.getData());				
				System.out.println(String.format("sub-%d: received ack - done ( string >> %s, byte array >> %s ).", this.id, new String(aPacket.getData()), aPacket.getData()));
				System.out.println(Arrays.toString(aPacketParsed));
				
				currentDirection = "IDLE";		
			}
			catch (SocketTimeoutException ste) {
				System.out.println(String.format("sub-%d: error encountered, taking elevator out of operation.", this.id));
				ePacket = Scheduler.createPacket(Command.ERROR, error, eport);							
				eSocket.send(ePacket);
				
				eSocket.receive(aPacket);
				status = "SUSPENDED";
				
				return;
			}
		}
		catch (IOException ioe) {
			System.out.println(String.format("sub-%d: unable to communicate with elevator system, exiting.", this.id));
			ioe.printStackTrace();
			System.exit(1);
		}		
	}
	
	
	public void sendPositionToFloor(String floor) {
		
		byte[] buffer = new byte[8];
		DatagramPacket sPacket = Scheduler.createPacket(Command.CMD, Byte.toString(Command.ELEVATOR_ARRIVED.getCommandByte()), fport);		
		DatagramPacket rPacket = new DatagramPacket(buffer, buffer.length);
		String[] rPacketParsed;
		
		
		try {
			DatagramSocket tempSocket = new DatagramSocket();
			System.out.println(String.format("sub-%d: sending position update to floor ( string >> %s, byte array >> %s ).\n", this.id, new String(sPacket.getData()), sPacket.getData()));
			tempSocket.send(sPacket);
			
			tempSocket.receive(rPacket);
			
			rPacketParsed = Scheduler.parsePacket(rPacket.getData());				
			System.out.println(String.format("sub-%d: received ack ( string >> %s, byte array >> %s ).", this.id, new String(rPacket.getData()), rPacket.getData()));
			System.out.println(Arrays.toString(rPacketParsed));
			
			sPacket = Scheduler.createPacket(Command.DATA, floor, fport);
			System.out.println(String.format("sub-%d: sending floor number ( string >> %s, byte array >> %s ).\n", this.id, new String(sPacket.getData()), sPacket.getData()));
			tempSocket.send(sPacket);
			
			tempSocket.receive(rPacket);
			
			rPacketParsed = Scheduler.parsePacket(rPacket.getData());				
			System.out.println(String.format("sub-%d: received ack - done ( string >> %s, byte array >> %s ).", this.id, new String(rPacket.getData()), rPacket.getData()));
			System.out.println(Arrays.toString(rPacketParsed));
			
			tempSocket.close();
			return;
		} 
		catch (Exception e) {			
			e.printStackTrace();
		}		
	}
}
