package filetcp;

import java.io.*;
import java.net.*;

public class FileServer {
	public FileServer() {
	
	}
	public static void main(String[] args) throws Exception{
		
		ServerSocket s = new ServerSocket(4333);
		Socket sr= s.accept();
		
		
		
	
		InputStream is=sr.getInputStream();
		FileOutputStream fr= new FileOutputStream("M:\\receive\\900001 clap.wav");
		
		
		byte b[]= new byte[40000];
		is.read(b,0,b.length);
		fr.write(b,0,b.length);
		
	
		
		
		
		
//		FileInputStream fr= new FileInputStream("M:\\90000 clap.wav");
//		byte b[]=new byte[40000];
//		fr.read(b, 0, b.length);
//		OutputStream os=sr.getOutputStream();
//		os.write(b,0,b.length);
//		
		
		
//		ServerSocket s2 = new ServerSocket(4334);
//		Socket sr2= s2.accept();
//		FileInputStream fr2= new FileInputStream("M:\\Bass.wav");
//		byte b2[]=new byte[300000];
//		fr2.read(b2, 0, b2.length);
//		OutputStream os2=sr2.getOutputStream();
//		os2.write(b2,0,b2.length); 
	}
}
