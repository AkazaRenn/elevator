package tcp;

public class Test {

    static class Runnable1 implements Runnable{
        public Runnable1(){}
        public void run(){
            try{
                Thread.sleep(1200);
                UDPReceiver udpreceiver = new UDPReceiver();
                udpreceiver.run();
         

            }catch ( Exception ie){
                System.out.println(ie);
            }
        }     
    }


    static class Runnable2 implements Runnable{
        public Runnable2(){}
        public void run(){
            try{
                Thread.sleep(1500);
                UDPSender udpsender = new UDPSender();
                udpsender.run();

            }catch ( Exception ie){
                System.out.println(ie);
            }
        }     
    }
    
    static class Runnable3 implements Runnable{
        public Runnable3(){}
        public void run(){
            try{
                Thread.sleep(900);
                UDPReceiver2 udpreceiver2 = new UDPReceiver2();
                udpreceiver2.run();
         

            }catch ( Exception ie){
                System.out.println(ie);
            }
        }     
    }


    static class Runnable4 implements Runnable{
        public Runnable4(){}
        public void run(){
            try{
                Thread.sleep(1100);
                UDPSender2 udpsender2 = new UDPSender2();
                udpsender2.run();


            }catch ( Exception ie){
                System.out.println(ie);
            }
        }     
    }
    
    public static void main(String[] args) throws InterruptedException{        
        Thread t1 = new Thread(new Test.Runnable1());
        Thread t2 = new Thread(new Test.Runnable2());      
        Thread t3 = new Thread(new Test.Runnable3());
        Thread t4 = new Thread(new Test.Runnable4()); 
        
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        
        
        t1.join();
        t2.join();
        t3.join();
        t4.join();
       

        
       
    }     
}

