package ca.carleton.winter2020.sysc3303a.group8.floor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.carleton.winter2020.sysc3303a.group8.utils.Command;
import ca.carleton.winter2020.sysc3303a.group8.utils.Utils;

/**
 * Class reading simulating floor buttons that sends inputs to the system
 * @author Frank Xu
 */
public class FloorButtons extends Thread {

    public static final String INPUT_FILE_PATH = "misc/input.txt";
    public static final String INPUT_PATTERN = "\\d{2}:\\d{2}:\\d{2}.\\d*\\s\\d{1,2}\\s[A-z]{2,4}\\s\\d{1,2}";

    private FloorSubsystem floorSubsystem;

    public FloorButtons(FloorSubsystem floorSubsystem, String name) {
        super(name);
        this.floorSubsystem = floorSubsystem;
    }

    @Override
    public void run() {
        List<String> requests = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_PATH));
            String line = null;
            while ((line = br.readLine()) != null) {
                Matcher matcher = Pattern.compile(INPUT_PATTERN).matcher(line);
                if (matcher.find()) {
                    requests.add(new String(matcher.group()));
                }
            }
            br.close();
        } catch (Throwable e) {
            System.exit(1);
        }

        request(requests);
    }

    /**
     * Make requests based on the given list of request strings
     * @param requests a list of strings containing request detials
     */
    public void request(List<String> requests) {
        int time = 0;
        int prevTime = 0;

        // read each request and sleep by a given period of time
        for (String s : requests) {
            String input[] = s.split(" ");
            Scanner sc = new Scanner(input[0].replace(':', ' '));
            time = (int)((((sc.nextInt() * 60) + sc.nextInt()) * 60 + sc.nextFloat()) * 1000);
            sc.close();

            int startFloor = Integer.parseInt(input[1]);
            int destFloor = Integer.parseInt(input[3]);
            Command targetDirection = Command.valueOf(input[2].toUpperCase());

            Utils.sleep(prevTime == 0 ? 0 : time - prevTime);
            prevTime = time;

            // send request
            for (Floor floor : floorSubsystem.getFloors()) {
                if (floor.getFloorNum() == startFloor) {
                    floorSubsystem.request(input[0], startFloor, destFloor, targetDirection);
                    if (targetDirection == Command.UP) {
                        floor.setUpLamp(true);
                    } else if (targetDirection == Command.DOWN) {
                        floor.setDownLamp(true);
                    }
                }
            }
        }
    }

}