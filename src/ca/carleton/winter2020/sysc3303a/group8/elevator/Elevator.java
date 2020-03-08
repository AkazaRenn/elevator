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

	private static int elevator_id;
	private ElevatorSubsystem elevatorSubSystem;
	private DatagramSocket sendSocket, receiveSocket;
	private DatagramPacket sendPacket,receivePacket;
	
	public Elevator(int port, int id) {
		this.elevator_id = id;
		
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
			
		}
	}
}
