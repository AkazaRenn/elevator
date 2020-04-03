package ca.carleton.winter2020.sysc3303a.group8.elevator;

import java.util.ArrayList;
import java.util.List;

import ca.carleton.winter2020.sysc3303a.group8.utils.Utils;

/**
 * Elevator subsystem of the project.
 * 
 * @author Frank Xu 101050120
 */
public class ElevatorSubsystem extends Thread {

    private List<Car> cars;

    public ElevatorSubsystem() {
        cars = new ArrayList<>();
        for (int i = 0; i < Utils.CAR_PORTS.length; i++) {
            cars.add(new Car(Utils.CAR_PORTS[i], i + 1, 1));
        }
    }

    public void main() {
        for (Car car : cars) {
            car.start();
        }
    }

    public static void main(String[] args) {
        ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();
        elevatorSubsystem.main();
    }
}
