package ca.carleton.winter2020.sysc3303a.group8.utils;

public enum Direction {
    UP((byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01),
    DOWN((byte)0x00, (byte)0x00, (byte)0x00, (byte)0x02),
    HOLD((byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00);
    
    private byte[] commandBytes;
    
    private Direction(byte ... commandBytes) {
        this.commandBytes = commandBytes;
    }

    public byte[] getCommandBytes() {
        return commandBytes;
    }
}
