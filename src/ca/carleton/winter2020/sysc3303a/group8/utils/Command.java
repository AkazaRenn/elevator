package ca.carleton.winter2020.sysc3303a.group8.utils;

public enum Command {
    UP((byte)0x01),
    DOWN((byte)0x02),
    HOLD((byte)0x00);
    
    private byte commandByte;
    
    private Command(byte commandByte) {
        this.commandByte = commandByte;
    }

    public byte getCommandByte() {
        return commandByte;
    }
}
