package ca.carleton.winter2020.sysc3303a.group8.elevator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import ca.carleton.winter2020.sysc3303a.group8.utils.Command;
import ca.carleton.winter2020.sysc3303a.group8.utils.Utils;

/**
 * Simulates a single car of the elevator subsystem
 * 
 * @author Frank Xu
 */
public class Car extends Thread {

    public final int CAR_PORT;
    public final int ID;

    private DatagramSocket socket;
    private List<String> lamps;
    private int currentFloor;
    private boolean running;

    public Car(int port, int id, int initialFloor) {
        super("car " + id);
        CAR_PORT = port;
        ID = id;

        lamps = new ArrayList<>();
        socket = Utils.newSocket(CAR_PORT);
        currentFloor = initialFloor;
    }

    @Override
    public void run() {
        running = true;
        byte[] bytes = new byte[8];
        String[] data;
        DatagramPacket packet;

        System.out.println("Car " + ID + ": pending signal...");

        while (running) {
            packet = Utils.recvPacket(socket, bytes);
            data = Utils.parseData(bytes);
            switch (Command.valueOfCode(data[0])) {
            case CMD:
                onCmd(packet, data[1]); // deal with CMD
                break;
            case DATA:
                packet.setData(Utils.buildData(Command.ACK, data[1]));
                Utils.sendPacket(socket, packet);
                System.out.println("Car " + ID + ": lamp: " + lamps.toString());
                break;
            case ERROR:
                onError(packet, data[1]);
                break;
            default:
            }
        }
    }

    /**
     * Controls the car to move when receiving a command
     * 
     * @param command the detailed command
     */
    private void move(Command command) {
        switch (command) {
        case UP:
            Utils.sleep(Utils.MOVE_SINGLE_FLOOR_TIME);
            currentFloor++;
            break;
        case DOWN:
            Utils.sleep(Utils.MOVE_SINGLE_FLOOR_TIME);
            currentFloor--;
            break;
        case DOOR_OPEN:
        case DOOR_CLOSE:
            Utils.sleep(Utils.DOOR_OPEN_CLOSE_TIME);
            break;
        default:
        }
    }

    /**
     * Respond to CMD
     * 
     * @param packet      packet received, used to respond
     * @param commandCode code of the command to respond
     */
    private void onCmd(DatagramPacket packet, String commandCode) {
        move(Command.valueOfCode(commandCode)); // move car to position
        packet.setData(Utils.buildData(Command.ACK, commandCode));
        Utils.sendPacket(socket, packet);
        
        switch (Command.valueOfCode(commandCode)) {
        case UP:
            packet.setData(Utils.buildData(Command.DATA, currentFloor));
            Utils.sendPacket(socket, packet);
            System.out.println("Car " + ID + ": up to " + currentFloor);
            break;
        case DOWN:
            packet.setData(Utils.buildData(Command.DATA, currentFloor));
            Utils.sendPacket(socket, packet);
            System.out.println("Car " + ID + ": down to " + currentFloor);
            break;
        case DOOR_OPEN:
            System.out.println("Car " + ID + ": opened door at floor: " + currentFloor);
            break;
        case DOOR_CLOSE:
            System.out.println("Car " + ID + ": closed door at floor: " + currentFloor);
            break;
        case STOP:
            System.out.println("Car " + ID + ": stopped at floor: " + currentFloor);
            break;
        default:
        }
    }

    /**
     * Respond to ERROR
     * 
     * @param packet      packet received, used to respond
     * @param commandCode code of the command to respond
     */
    private void onError(DatagramPacket packet, String errorCode) {
        switch (Command.valueOfCode(errorCode)) {
        case ERROR_DOOR_JAM:
            System.out.println("ERROR: car " + ID + "door jammed");
            break;
        case ERROR_STUCK:
            System.out.println("ERROR: car " + ID + "stucked");
            break;
        default:
        }
    }

    public void stopRunning() {
        running = false;
    }

}
