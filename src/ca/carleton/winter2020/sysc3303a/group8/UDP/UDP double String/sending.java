package udp;

import java.util.Scanner;

public class sending {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		UDPSender sender = new UDPSender();
		UDPSender sender2 = new UDPSender();
		Scanner in;
	    in = new Scanner (System.in);
	    System.out.println("please enter operation code:");
	    String command1 = in.nextLine();
		sender.send(command1,1236);
		System.out.println("please enter file path:");
	    String command2 = in.nextLine();
		sender2.send(command2,1237);
		
	}

}
