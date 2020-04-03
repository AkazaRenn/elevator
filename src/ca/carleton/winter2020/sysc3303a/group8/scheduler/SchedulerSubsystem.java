package ca.carleton.winter2020.sysc3303a.group8.scheduler;

import java.awt.Color;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ca.carleton.winter2020.sysc3303a.group8.utils.CarStatus;
import ca.carleton.winter2020.sysc3303a.group8.utils.Command;
import ca.carleton.winter2020.sysc3303a.group8.utils.Utils;

/**
 * Scheduler subsystem of the elevator that intakes and sends commands from and
 * to both the elevator subsystem and floor subsystem
 * 
 * @author Frank Xu
 *
 */
public class SchedulerSubsystem {

    private DatagramSocket socket;
    private List<List<String>> requestsLists;
    private List<CarAgent> carAgents;
    private View view;
    private boolean running;

    public SchedulerSubsystem() {
        socket = Utils.newSocket(Utils.SCHEDULER_PORT);
        carAgents = new ArrayList<>();
        requestsLists = new ArrayList<>();
        for (int i = 0; i < Utils.CAR_COUNT; i++) {
            List<String> requests = Collections.synchronizedList(new ArrayList<String>());
            requestsLists.add(requests);
            carAgents.add(new CarAgent(Utils.CAR_PORTS[i], i, requests, 1));
        }

        view = new View();
    }

    public void run() {
        System.out.println("Scheduler: running");
        boolean blinking = true;
        running = true;

        byte[] buffer = new byte[8];

        for (CarAgent carAgent : carAgents) {
            carAgent.start();
        }

        while (running) {
            // reset all lights
            for (int i = 0; i < Utils.CAR_COUNT; i++) {
                for (int j = 0; j < Utils.FLOOR_COUNT; j++) {
                    view.getFloors(j, i).setBackground(Color.BLUE);
                }
                view.getDirections(0, i).setBackground(Color.WHITE);
                view.getDirections(1, i).setBackground(Color.WHITE);
            }

            for (CarAgent agent : carAgents) {
                if (agent.status != CarStatus.SUSPENDED) {
                    switch (agent.currentDirection) {
                    case UP:
                        view.getDirections(0, agent.CAR_ID).setBackground(Color.GREEN);
                    case DOWN:
                        view.getDirections(1, agent.CAR_ID).setBackground(Color.GREEN);
                    default:
                    }
                    view.getFloors(Utils.FLOOR_COUNT - agent.currentFloor, agent.CAR_ID).setBackground(Color.RED);
                } else {
                    if (blinking) { // keep blinking means there's something wrong
                        view.getFloors(Utils.FLOOR_COUNT - agent.currentFloor, agent.CAR_ID).setBackground(Color.RED);
                    }
                }
            }
            blinking = !blinking;
            DatagramPacket packet = Utils.recvPacket(socket, buffer);
            System.out.println("Scheduler: received packet " + new String(buffer));
            String[] data = Utils.parseData(buffer);

            if (Command.valueOfCode(data[0]) == Command.CMD) {
                handleFloorCommand(Command.valueOfCode(data[1]), packet.getPort());
            } else {
                System.out.println("Scheduler: received an unknown packet");
            }
        }
        socket.close();
        return;
    }

    /**
     * Handles the command from floor subsystem
     * 
     * @param command command received
     * @param port    port where the respond is sent to
     */
    public void handleFloorCommand(Command command, int port) {

        byte[] buffer = new byte[1024];
        DatagramPacket packet;

        if (command == Command.FLOOR_BUTTON) {
            System.out.println("Scheduler: responding");
            Utils.sendPacket(socket, Utils.buildData(Command.ACK, command.getCommandCode()), Utils.FLOOR_HOSTNAME,
                    port);

            // get data
            packet = Utils.recvPacket(socket, buffer);

            // handling packet
            String[] data = Utils.parseData(packet.getData());
            System.out.println("Scheduler: received packet " + new String(buffer));
            System.out.println(Arrays.toString(data));

            if (Command.valueOfCode(data[0]) == Command.DATA) {
                Utils.sendPacket(socket, Utils.buildData(Command.ACK, data[1]), Utils.FLOOR_HOSTNAME, port);

                data = data[1].split(" ");
                CarAgent bestCar = findBestCar(Integer.parseInt(data[1]), Command.valueOf(data[2]));
                requestsLists.get(bestCar.CAR_ID).add(data[1] + " " + data[2] + " " + data[3]); // add to request List
            }
        }
    }

    /**
     * Find the best car to be sent to the floor
     * 
     * @param targetFloor     the floor the car is going to
     * @param targetDirection direction of the floor
     * @return agent of the best car
     */
    private CarAgent findBestCar(int targetFloor, Command targetDirection) {
        CarAgent bestCar = null;
        int bestVal = 0;
        for (CarAgent agent : carAgents) {
            int fit = getFloorDiff(agent.currentFloor, targetFloor, agent.currentDirection, targetDirection,
                    agent.status);
            if (fit >= bestVal) {
                bestCar = agent;
            }
        }
        return bestCar;
    }

    /**
     * Calculate the floor difference with every car to determine which car to be
     * sent to the floor
     * 
     * @param currentFloor     current floor of the car
     * @param targetFloor      the floor the car is going to
     * @param currentDirection current direction of the car
     * @param targetDirection  the direction of the floor the car is going to
     * @param status           current status of the car
     * @return the floor difference
     */
    private int getFloorDiff(int currentFloor, int targetFloor, Command currentDirection, Command targetDirection,
            CarStatus status) {
        int mark = 0;
        int floorDiff = currentFloor - targetFloor; // if -ive, call is above, if +ive, call is below

        switch (status) {
        case SUSPENDED:
            return -1;
        case IDLE:
            return Utils.FLOOR_COUNT + 1 - Math.abs(floorDiff);
        default:
            switch (currentDirection) {
            case DOWN:
                if (floorDiff < 0) {
                    return 1;
                } else if (targetDirection == currentDirection) {
                    return Utils.FLOOR_COUNT + 2 - floorDiff;
                } else {
                    return Utils.FLOOR_COUNT + 1 - floorDiff;
                }
            case UP:
                if (floorDiff > 0) {
                    return 1;
                } else if (targetDirection == currentDirection) {
                    return Utils.FLOOR_COUNT + 2 + floorDiff;
                } else {
                    return Utils.FLOOR_COUNT + 1 + floorDiff;
                }
            default:
                return mark;
            }
        }
    }
    
    public void stopRunning() {
        running = false;
    }

    public static void main(String[] args) {
        SchedulerSubsystem scheduler = new SchedulerSubsystem();
        scheduler.run();
    }
}
