package udp;

import java.net.*;

public class UDPReceiver implements Runnable  {

	private final static int PACKETSIZE = 100 ;
	String message = "";
	public UDPReceiver(){
		
	}
	public void applyreceive(int port) throws Exception{ 
		// Convert the argument to ensure that is it valid
		DatagramSocket socket = new DatagramSocket( port ) ;
		System.out.println( "waiting for operation id " ) ;
		receive(socket);
		socket.close();
	}
	public void receive(DatagramSocket s) throws Exception{
		DatagramPacket packet = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE ) ;
		s.receive( packet ) ;
		message = new String(packet.getData()).trim();
		System.out.println("operation id: " + message ) ;
	}
	public static void main( String args[] ) throws Exception{
		UDPReceiver receiver = new UDPReceiver();
		receiver.applyreceive(1234);
	}
	public String getMessage() {
		return this.message;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		UDPReceiver receiver = new UDPReceiver();
		
		try {
			receiver.applyreceive(1236);
			message = receiver.getMessage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

