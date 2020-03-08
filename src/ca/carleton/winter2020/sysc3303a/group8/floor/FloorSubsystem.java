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
    public static final Path INPUT_FILE = Paths.get("../input.txt"); 
    
    private static final String SCHEDULER_HOSTNAME = "localhost";
    private static final int SCHEDULER_PORT = 1000;
    private static final int SELF_PORT = 1200;
    
    private static final int INITIAL_FLOOR = 1;
    private static final int CAR_COUNT = 2;
    
    private static final Direction DEFAULT_DIRECTION = Direction.HOLD;
    
    public final int BOTTOM_FLOOR;
    public final int TOP_FLOOR;
    private final int ELEVATOR_COUNT;
    
    private DatagramSocket socket;
    private List<Floor> floors;
    
    public FloorSubsystem(int bottomFloor, int topFloor, int carNum) {
        //Send messages to this scheduler
        BOTTOM_FLOOR = bottomFloor;
        TOP_FLOOR = topFloor;
        ELEVATOR_COUNT = carNum;
        
        floors = new ArrayList<Floor>(topFloor - bottomFloor + 1);
        for(int i = bottomFloor; i <= topFloor; i++) {
                floors.add(new Floor(this, i, CAR_COUNT, INITIAL_FLOOR));
                floors.get(i).setDirecionLamp(DEFAULT_DIRECTION);
        }
        
        try {
            socket = new DatagramSocket(SELF_PORT);
        } catch (SocketException e) {
            System.exit(1);
        }
    }
    
    private byte[] createBuf(Command header, byte[] content) {
        byte[] bytes = new byte[content.length + 1];
        bytes[0] = header.getCommandByte();
        for(int i = 0; i < content.length; i++) {
            bytes[i + 1] = content[i];
        }
        
        return bytes;
    }
    
    private String[] readBuf(byte[] bytes) {
        return new String(Arrays.copyOfRange(bytes, 1, bytes.length)).split("\0");
    }
    
    /*
     * set a stop floor for elevator
     */
    public void setStop(Direction direction, Integer floorNum) {
        byte[] msg = null;
        if(direction == Direction.UP) {
            msg = createBuf(Command.UP_PICKUP, floorNum.toString().getBytes());
        } else {
            msg = createBuf(Command.DOWN_PICKUP, floorNum.toString().getBytes());
        }
        
        DatagramPacket packet = null;
        try {
            packet = new DatagramPacket(msg, msg.length, 
                    InetAddress.getByName(SCHEDULER_HOSTNAME), SCHEDULER_PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        floors.get(floorNum).setDirecionLamp(direction);
        floors.get(floorNum).detectArrive();
    }
    
    public void respondRequest() {
        byte[] bytes = {Command.ACK.getCommandByte()};
        DatagramPacket recvPacket = new DatagramPacket(new byte[1024], 1024);
        try {
            socket.send(new DatagramPacket(bytes, bytes.length, InetAddress.getByName(SCHEDULER_HOSTNAME), SCHEDULER_PORT));
            socket.receive(recvPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        

        String[] data = readBuf(recvPacket.getData());
        Floor floor = floors.get(Integer.parseInt(data[0]));
        if(floor.getUpLamp()) {
            floor.setDirecionLamp(Direction.UP);
        } else if(floor.getDownLamp()) {
            floor.setDirecionLamp(Direction.DOWN);
        }
        
        recvPacket.setData(createBuf(Command.ACK, data[0].getBytes()));
        try {
            socket.send(recvPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static void main(String[] args) throws Throwable {
        FloorSubsystem system = new FloorSubsystem(1, 5, 2);
        List<String> lines = Files.readAllLines(INPUT_FILE);
        for(String line : lines) {
            String[] data = line.split(" ");
            if(data[2].equals("up")) {
                system.setStop(Direction.UP, Integer.valueOf(data[1]));
            } else {
                system.setStop(Direction.DOWN, Integer.valueOf(data[1]));
            }
        }
        while(true) {
            system.respondRequest();
        }
    }
}
