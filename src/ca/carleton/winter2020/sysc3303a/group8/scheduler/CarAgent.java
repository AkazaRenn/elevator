package ca.carleton.winter2020.sysc3303a.group8.scheduler;

import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ca.carleton.winter2020.sysc3303a.group8.utils.CarStatus;
import ca.carleton.winter2020.sysc3303a.group8.utils.Command;
import ca.carleton.winter2020.sysc3303a.group8.utils.Utils;

/**
 * Agent that connects a single car and the scheduler
 * 
 * @author Frank Xu
 */
class CarAgent extends Thread {

    public static final String CAR_HOSTNAME = "localhost";

    public final int CAR_PORT;
    public final int CAR_ID;

    private Collection<String> requests; // ArrayList with requests
    private DatagramSocket socket;
    private boolean running;

    protected volatile CarStatus status;
    protected volatile Command currentDirection;
    protected volatile int currentFloor;
    protected volatile boolean pickingUp;

    public CarAgent(int carPort, int carId, Collection<String> requests, int currentFloor) {
        super("car agent " + carId);
        CAR_PORT = carPort;
        CAR_ID = carId;

        this.requests = requests;
        socket = Utils.newSocket();
        this.currentFloor = currentFloor;
        currentDirection = Command.STOP;
        status = CarStatus.IDLE;
        pickingUp = false;
    }

    @Override
    public void run() {
        running = true;
        System.out.println("Car agent " + CAR_ID + ": starting");

        while (running) {
            if (status == CarStatus.IDLE && !requests.isEmpty()) {
                handleRequest(findNextRequest());
            }
        }
        socket.close();
    }

    /**
     * Find the next request to respond to
     * 
     * @return the request string
     */
    private String findNextRequest() {
        String bestRequest = "";
        int closestRequest = 100;

        for (String request : requests) {
            String singleRequest = request.split(" ")[0];
            // find the closest
            if (Math.abs(Integer.parseInt(singleRequest) - currentFloor) < closestRequest) {
                bestRequest = request;
                closestRequest = Integer.parseInt(singleRequest) - currentFloor;
            }
        }
        requests.remove(bestRequest); // request will be handled, remove from list
        closestRequest = Integer.parseInt(bestRequest.split(" ")[0]);
        if (closestRequest < currentFloor) {
            currentDirection = Command.DOWN;
        } else if (closestRequest > currentFloor) {
            currentDirection = Command.UP;
        } else {
            currentDirection = Command.STOP;
        }
        status = CarStatus.MOVING;

        return bestRequest;
    }

    /**
     * Respond to the request
     * 
     * @param request the request to be responded to
     */
    private void handleRequest(String request) {
        byte[] buffer = new byte[8];
        String[] data;

        String[] spltRequest = request.split(" ");
        String targetFloor = spltRequest[0];
        Command targetDirection = Command.valueOf(spltRequest[1]);
        Set<String> targetFloors = new HashSet<String>(); // stores all the requested floors

        boolean lampChanged = false;
        boolean stop = false;

        if (currentDirection != Command.STOP) {
            while (status == CarStatus.MOVING) {
                // send direction to car and receive ACK
                sendCommand(Command.CMD, currentDirection.getCommandCode(), CAR_PORT);
                data = recvCommand(buffer);
                System.out.println(Arrays.toString(data));

                // receive car's response and respond
                data = recvCommand(buffer);
                System.out.println(Arrays.toString(data));
                currentFloor = Integer.parseInt(data[1]);
                sendCommand(Command.ACK, data[1], CAR_PORT);

                if (data[1].equals(targetFloor)) {
                    status = CarStatus.PENDING;
                }
            }
        } else {
            status = CarStatus.PENDING;
        }

        currentDirection = targetDirection;
        pickingUp = true;

        // send stop to car and receive ACK
        sendCommand(Command.CMD, Command.STOP.getCommandCode(), CAR_PORT);
        data = recvCommand(buffer);
        System.out.println(Arrays.toString(data));

        // send update to floor and receive ACK
        sendArrival("" + currentFloor);
        // send open door
        sendCommand(Command.CMD, Command.DOOR_OPEN.getCommandCode(), CAR_PORT);
        data = recvCommand(buffer);
        System.out.println(Arrays.toString(data));

        // send car lamp update and receive ACK
        targetFloors.add(spltRequest[2]);
        sendCommand(Command.DATA, spltRequest[2], CAR_PORT);
        data = recvCommand(buffer);
        System.out.println(Arrays.toString(data));

        // send close door
        sendCommand(Command.CMD, Command.DOOR_CLOSE.getCommandCode(), CAR_PORT);
        data = recvCommand(buffer);
        System.out.println(Arrays.toString(data));

        while (status == CarStatus.PENDING) {
            pickingUp = false;

            // send another direction to car and receive ACK
            sendCommand(Command.CMD, currentDirection.getCommandCode(), CAR_PORT);
            data = recvCommand(buffer);
            System.out.println(Arrays.toString(data));

            // receive car's response and respond
            data = recvCommand(buffer);
            System.out.println(Arrays.toString(data));
            currentFloor = Integer.parseInt(data[1]);
            sendCommand(Command.ACK, data[1], CAR_PORT);

            // check for intermediate stops
            if (!requests.isEmpty()) {
                for (String s : requests) {
                    spltRequest = s.split(" ");
                    if (spltRequest[0].equals(data[1])) {
                        if (spltRequest[1].equals(targetDirection.toString())) {
                            requests.remove(s); // request will be handled, remove
                            stop = true;
                            pickingUp = true;

                            // add the floor to targets and turn on the lamp if it did not
                            if (targetFloors.add(spltRequest[2])) {
                                lampChanged = true;
                            }
                        } else {
                            pickingUp = false;
                        }
                    }
                }
            }

            // arrived at target floor
            if (targetFloors.contains(data[1])) {
                stop = true;
                lampChanged = true;
                targetFloors.remove(data[1]);
            }

            // has stopped
            if (stop) {
                // send stop and receive ACK
                sendCommand(Command.CMD, Command.STOP.getCommandCode(), CAR_PORT);
                data = recvCommand(buffer);
                System.out.println(Arrays.toString(data));

                // send update to floor and receive ACK
                sendArrival("" + currentFloor);
                sendCommand(Command.CMD, Command.DOOR_OPEN.getCommandCode(), CAR_PORT);
                data = recvCommand(buffer);
                System.out.println(Arrays.toString(data));

                if (lampChanged) {
                    // send elevator lamp updates and receive ACK
                    sendCommand(Command.DATA, lampChanged ? "lampChanged" : "lampUnchanged", CAR_PORT);
                    data = recvCommand(buffer);
                    System.out.println(Arrays.toString(data));
                }

                // send close door and receive ACK
                sendCommand(Command.CMD, Command.DOOR_CLOSE.getCommandCode(), CAR_PORT);
                data = recvCommand(buffer);
                System.out.println(Arrays.toString(data));
            }

            // no pending targets
            if (targetFloors.isEmpty()) {
                status = CarStatus.IDLE;
                currentDirection = Command.STOP;
            } else {
                System.out.println("Car agent " + CAR_ID + ": resetting");
                stop = false;
                lampChanged = false;
            }
        }
    }

    /**
     * Send the arrival of a car to floor
     * 
     * @param floor floor the car arrived
     */
    private void sendArrival(String floor) {
        byte[] buffer = new byte[8];
        String[] data;

        // send arrival and receive ACK
        sendCommand(Command.CMD, Command.ELEVATOR_ARRIVED.getCommandCode(), Utils.FLOOR_PORT);
        data = recvCommand(buffer);
        System.out.println(Arrays.toString(data));

        // send floor number and receive ACK
        sendCommand(Command.DATA, floor, Utils.FLOOR_PORT);
        data = recvCommand(buffer);
        System.out.println(Arrays.toString(data));
    }

    /**
     * Build and send a command to the car
     * 
     * @param command    the command
     * @param code       command code of detailed command
     * @param targetPort port of the car
     */
    private void sendCommand(Command command, int code, int targetPort) {
        sendCommand(command, Integer.toString(code), targetPort);
    }

    /**
     * Build and send a command to the car
     * 
     * @param command    the command
     * @param data       data of the command
     * @param targetPort port of the car
     */
    private void sendCommand(Command command, String data, int targetPort) {
        Utils.sendPacket(socket, Utils.buildData(command, data), CAR_HOSTNAME, targetPort);
        System.out.println("Car agent " + CAR_ID + ": sending command " + command + ": " + new String(data));
    }

    /**
     * Receive data and print the detail
     * 
     * @param bytes where the data receives will be stored
     * @return the parsed data
     */
    private String[] recvCommand(byte[] bytes) {
        Utils.recvPacket(socket, bytes);
        System.out.println("Car agent " + CAR_ID + ": received command: " + new String(bytes));
        return Utils.parseData(bytes);
    }

    public void stopRunning() {
        running = false;
    }
}
