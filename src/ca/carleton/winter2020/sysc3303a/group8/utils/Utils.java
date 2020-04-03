package ca.carleton.winter2020.sysc3303a.group8.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Utils {
    
    public static final String SCHEDULER_HOSTNAME = "localhost";
    public static final String FLOOR_HOSTNAME = "localhost";
    public static final String ELEVATOR_HOSTNAME = "localhost";
    
    public static final int SCHEDULER_PORT = 50000;
    public static final int FLOOR_PORT = 50010;
    public static final int[] CAR_PORTS = {50020, 50021, 50022};
    
    public static final int MOVE_SINGLE_FLOOR_TIME = 6000;
    public static final int DOOR_OPEN_CLOSE_TIME = 2000;

    public static final int BOTTOM_FLOOR = 0;
    public static final int FLOOR_COUNT = 20;
    public static final int CAR_COUNT = 3;
    
    /**
     * Wrapping DatagramSocket.receive()
     */
    public static DatagramPacket recvPacket(DatagramSocket socket, byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return packet;
    }
    
    /**
     * Wrapping new DatagramPacket and DatagramSocket.send()
     */
    public static void sendPacket(DatagramSocket socket, byte[] data, String hostname, int port) {
        DatagramPacket packet = null;
        try {
            packet = new DatagramPacket(data, data.length, InetAddress.getByName(hostname), port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        sendPacket(socket, packet);
    }
    
    /**
     * Wrapping DatagramSocket.send()
     */
    public static void sendPacket(DatagramSocket socket, DatagramPacket packet) {
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Wrapping new DatagramSocket()
     */
    public static DatagramSocket newSocket() {
        try {
            return new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
    
    /**
     * Wrapping new DatagramSocket(int port)
     */
    public static DatagramSocket newSocket(int port) {
        try {
            return new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
    
    /**
     * Build data from command and info
     */
    public static byte[] buildData(Command command, String info) {
        return ("\0" + command.getCommandCode() + "\0" + info + "\0").getBytes();
    }

    /**
     * Build data from command and info
     */
    public static byte[] buildData(Command command, int code) {
        return ("\0" + command.getCommandCode() + "\0" + code + "\0").getBytes();
    }

    /**
     * Parse the data received into a readable string
     * @param data
     * @return a readable string from the data received
     */
    public static String[] parseData(byte[] data) {
        return new String(data).replaceFirst("\0", "").split("\0");
    }
    
    /**
     * Wrapping up Thread.sleep()
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
