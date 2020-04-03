package ca.carleton.winter2020.sysc3303a.group8.utils;

public enum Command {
    ACK(1),
    CMD(2),
    DATA(3),
    ERROR(0),
    
    FLOOR_BUTTON(10),
    ELEVATOR_ARRIVED(11),
    DOOR_OPEN(12),
    DOOR_CLOSE(13),
    
    UP(20),
    DOWN(21),
    STOP(22),
    
    ERROR_DOOR_JAM(999),
    ERROR_STUCK(998);
    
    private int commandCode;
    
    private Command(int commandCode) {
        this.commandCode = commandCode;
    }

    public int getCommandCode() {
        return commandCode;
    }
    
    public static Command valueOfCode(int commandCode) {
        for (Command c : values()) {
            if (c.commandCode == commandCode) {
                return c;
            }
        }
        return null;
    }
    
    public static Command valueOfCode(String commandCode) {
        return valueOfCode(Integer.parseInt(commandCode));
    }
}
