package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceiver2 implements Runnable  {

	private final static int PACKETSIZE = 100 ;
	String message = "";
	public UDPReceiver2(){
		
	}
	public void applyreceive(int port) throws Exception{ 
		// Convert the argument to ensure that is it valid
		DatagramSocket socket = new DatagramSocket( port ) ;
		System.out.println( "waiting for file path") ;
		receive(socket);
		socket.close();
	}
	public void receive(DatagramSocket s) throws Exception{
		DatagramPacket packet = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE ) ;
		s.receive( packet ) ;
		message = new String(packet.getData()).trim();
		System.out.println( "file path: " + message) ;
	}
	public String getMessage() {
		return this.message;
	}
	public static void main( String args[] ) throws Exception{
		UDPReceiver2 receiver = new UDPReceiver2();
		receiver.applyreceive(1234);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		UDPReceiver2 receiver2 = new UDPReceiver2();
		
		try {
			receiver2.applyreceive(1237);
			message = receiver2.getMessage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
