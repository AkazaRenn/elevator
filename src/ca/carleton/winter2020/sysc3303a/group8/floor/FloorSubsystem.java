package ca.carleton.winter2020.sysc3303a.group8.floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import ca.carleton.winter2020.sysc3303a.group8.utils.Command;
import ca.carleton.winter2020.sysc3303a.group8.utils.Utils;

/**
 * FloorSubsystem.java This class is the FloorSubsystem side for a simple echo
 * server based on UDP/IP. The FloorSubsystem sends a character string to the
 * echo server, then waits for the server to send it back to the Floor
 * Subsystem.
 * 
 * @author Zhi Qiao and Dennis Liu
 * @author Frank Xu
 */

public class FloorSubsystem {

    private DatagramSocket socket;
    private List<Floor> floors;

    private boolean running;

    public FloorSubsystem(String name) {
        socket = Utils.newSocket(Utils.FLOOR_PORT);
        floors = new ArrayList<Floor>();
    }

    public void main() {
        running = true;
        byte[] bytes = new byte[100];
        String[] data = new String[2];

        while (running) {
            DatagramPacket packet = Utils.recvPacket(socket, bytes);
            data = Utils.parseData(bytes);
            if (Integer.parseInt(data[0]) == Command.CMD.getCommandCode()) {
                respondArrival(data, packet.getPort());
            }
        }
    }

    /**
     * Build a request byte array
     * @param time time of the request sent
     * @param reqFloor floor the request is received
     * @param tgtFloor floor the request targets to
     * @param direction direction of the movement
     * @return a request byte array built from the params
     */
    private byte[] buildRequest(String time, int reqFloor, int tgtFloor, Command direction) {
        if (direction == Command.UP) {
            floors.get(reqFloor).setUpLamp(true);
        } else if (direction == Command.DOWN) {
            floors.get(reqFloor).setDownLamp(true);
        }
        System.out.println(
                "Floor: got request from floor " + reqFloor + " to " + tgtFloor + ", " + direction + " lamp on.");
        return Utils.buildData(Command.DATA, time + " " + reqFloor + " " + direction + " " + tgtFloor);
    }

    /**
     * Send a request of elevator to the scheduler
     * @param time time of the request sent
     * @param reqFloor floor the request is received
     * @param tgtFloor floor the request targets to
     * @param direction direction of the movement
     */
    public void request(String time, int reqFloor, int tgtFloor, Command direction) {
        Utils.sendPacket(socket, Utils.buildData(Command.CMD, Command.FLOOR_BUTTON.getCommandCode()),
                Utils.SCHEDULER_HOSTNAME, Utils.SCHEDULER_PORT);
        System.out.println("Floor: sent request for an elevator, pending response...");

        byte[] data = new byte[100];
        Utils.recvPacket(socket, data);
        String[] msg = Utils.parseData(data);
        if (Command.valueOfCode(msg[0]) == Command.ACK) {
            if (msg[1].equals(String.valueOf(Command.FLOOR_BUTTON.getCommandCode()))) {
                System.out.println("Floor: ACK received.");
                data = buildRequest(time, reqFloor, tgtFloor, direction);
                Utils.sendPacket(socket, data, Utils.SCHEDULER_HOSTNAME, Utils.SCHEDULER_PORT);
                System.out.println("Floor: sent input to Scheduler, pending response...");
                Utils.recvPacket(socket, data);
                msg = Utils.parseData(data);
                System.out.println("Floor: ACK received. Scheduler data: " + msg[1]);
            }
        }
    }

    /**
     * Send a respond of an incoming elevator arrival signal
     * @param msg the message received
     * @param port port of the scheduler, used for responding
     */
    private void respondArrival(String[] msg, int port) {
        byte[] bytes = new byte[100];
        if (Integer.parseInt(msg[0]) == Command.CMD.getCommandCode()) {
            if (Command.valueOfCode(msg[1]) == Command.ELEVATOR_ARRIVED) {
                System.out.println("Floor: elevator arrival received.");
                Utils.sendPacket(socket, Utils.buildData(Command.ACK, Command.ELEVATOR_ARRIVED.getCommandCode()),
                        Utils.SCHEDULER_HOSTNAME, port);
                System.out.println("Floor: sent ACK, pending floor number...");
                Utils.recvPacket(socket, bytes);
                String[] data = Utils.parseData(bytes);
                int floorNum = Integer.parseInt(data[1]);
                if (floors.get(floorNum).getUpLamp()) {
                    floors.get(floorNum).setUpLamp(false);
                } else if (floors.get(floorNum).getDownLamp()) {
                    floors.get(floorNum).setDownLamp(false);
                }
                System.out.println("Floor: floor number received.");
                Utils.sendPacket(socket, Utils.buildData(Command.ACK, data[1]), Utils.SCHEDULER_HOSTNAME, port);
                System.out.println("Floor: sent ACK.");
            }
        }
    }

    public boolean addFloor(Floor floor) {
        return floors.add(floor);
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void stopRunning() {
        running = false;
    }

    public static void main(String args[]) throws IOException {
        FloorSubsystem floorSubsystem = new FloorSubsystem("floor");
        Thread input = new FloorButtons(floorSubsystem, "input");

        for (int i = 0; i < Utils.FLOOR_COUNT; i++) {
            floorSubsystem.addFloor(new Floor(1, i + 1));
        }

        System.out.println("Starting Floor Subsystem.");
        input.start();
        floorSubsystem.main();
    }

}
