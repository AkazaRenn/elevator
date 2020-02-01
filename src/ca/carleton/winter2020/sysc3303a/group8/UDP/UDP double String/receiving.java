
//Author: Zhi Qiao(qiaoqiaojiushiwo@gmail.com)
//Author2: Zichen Wang

//this can 
import java.util.Scanner;
public class Receiving {
    static String databasefolder="/home/pi/Desktop/receivingtone/";
   

    public static void main(String[] args) throws Exception {
       
        
    	UDPReceiver receiver = new UDPReceiver();
        UDPReceiver receiver2 = new UDPReceiver();
        FileServer fileserver = new FileServer();
        UDPSender sender3 = new UDPSender();
        FileClient fileclient = new FileClient();
        boolean begin = true;
        
        DB db = new DB();
      
        
        System.out.println(" ");
        while(begin == true){
        
        Thread t1 = new Thread(receiver);
        Thread t2 = new Thread(receiver2);
        
        
        t1.start();
        t1.join();
        String flag = receiver.getMessage();
        
        //ADdig tone
        if(flag.equals("0")) {
            System.out.println("\naddTone selected");
            t2.start();
            t2.join();
            String filename = receiver2.getMessage();
            System.out.println("add: " + filename+" to database");
            
            String path=databasefolder+filename;
            
                if(!filename.isEmpty()) {
                    fileserver.applyreceive(1238, path);
                    
                }
            
            //Add a random number for ID, which is also UNIQUE  
            
            
            int id = db.getIdfromDB() + 1;
    
            db.addTone(id,filename,databasefolder+path);
            db.viewAllTone("jdbc:mysql://localhost:3306/idrums") ;
            
        }
        //Deleting Tong
        else if(flag.equals("1")) {
            System.out.println("deleteTone selected");
            t2.start();
            t2.join();
            String filename = receiver2.getMessage();
            
            System.out.println("delete :" + filename);
            
            db.deleteTone(filename);
            db.viewAllTone("jdbc:mysql://localhost:3306/idrums") ;
        }
        //Sending tone to Rpi 2
        else if(flag.equals("2")) {
            sender3.send(flag,1241);
            System.out.println("setTone selected");
            t2.start();
            t2.join();
            String filename = receiver2.getMessage();
            System.out.println("set:" + filename+" as new tone");
            if(!filename.isEmpty()) {
                fileserver.applyreceive(1238, filename);
            }
            String path=databasefolder+filename;
            for(int y = 0;y<100000;y++){}
            if(!filename.isEmpty()) {
                fileclient.applysend(path,1240);
            }
            

        }
        System.out.println(" ");
    }
        
        
        
    }
    
 
    
}
