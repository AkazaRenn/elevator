package ca.carleton.winter2020.sysc3303a.group8.utils;

public enum Command {

    // data headers buf[0]
    ACK((byte)0x01),
    CMD((byte)0x02),
    DATA((byte)0x03),
    ERROR((byte)0x04),

    // data contents buf[1]
    FLOOR_BUTTON((byte)0x10),
    ELEVATOR_ARRIVED((byte)0x11),
    UP_PICKUP((byte)0x33),
    UP_DROPOFF((byte)0x32),
    DOWN_PICKUP((byte)0x31),
    DOWN_DROPOFF((byte)0x30),
    DOOR_OPEN((byte)0x3A),
    DOOR_CLOSE((byte)0x3B),
    STOP((byte)0x3C),
    ERROR_DOOR_JAM((byte)0xE0),
    ERROR_STUCK((byte)0xE1);
    
    private byte commandByte;
    
    private Command(byte commandByte) {
        this.commandByte = commandByte;
    }

    public byte getCommandByte() {
        return commandByte;
    }
}
