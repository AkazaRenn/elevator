package Test;/**
 * ThreadExample2.java - this application demonstrates how to define a
 * separate thread of execution by implementing java.lang.Runnable.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

class ThreadExample1 extends JFrame
{
	public static int missingIng = 3;
	public static boolean empty = true;
	
    /**
     * JTextArea for the factorial thread.
     */
    private JTextArea ta1;

    /**
     * JTextArea for the thread executing main().
     */
    private JTextArea status;

    /**
     * Build the GUI.
     */
    public ThreadExample1(String title) {
        super(title);
        Box box = Box.createVerticalBox();

        ta1 = new JTextArea(5,40);
        ta1.setEditable(false);
        JScrollPane pane1 =
            new JScrollPane(ta1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane1.setBorder(BorderFactory.createTitledBorder("Thread 1"));
        box.add(pane1);

        status = new JTextArea(5, 40);
        status.setEditable(false);
        JScrollPane pane2 =
            new JScrollPane(status, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane2.setBorder(BorderFactory.createTitledBorder("Status"));
        box.add(pane2);

        getContentPane().add(box);
    }

    public static void main(String[] args) {
        ThreadExample1 frame = new ThreadExample1("Thread Example 2");

        /* Instantiate an anonymous subclass of WindowAdapter, and register it
         * as the frame's WindowListener.
         * windowClosing() is invoked when the frame is in the process of being
         * closed to terminate the application.
         */
        frame.addWindowListener(
            new WindowAdapter() {
               public void windowClosing(WindowEvent e) {
                  System.exit(0);
               }
            }
        );

        // Size the window to fit the preferred size and layout of its
        // subcomponents, then show the window.
        frame.pack();
        frame.setVisible(true);

        Thread agent = new Thread(
        		new Agent(frame.ta1), "Factorial calculator");
        frame.status.append("Created: " + agent + '\n');
        
        Thread chef1 = new Thread(
        		new Chefone(frame.ta1), "Factorial calculator");
        frame.status.append("Created: " + chef1 + '\n');
       
       Thread chef2 = new Thread(
    		   	new Cheftwo(frame.ta1), "Factorial calculator");
       frame.status.append("Created: " + chef2 + '\n');
      
      	Thread chef3 = new Thread(
      			new Chefthree(frame.ta1), "Factorial calculator");
      	frame.status.append("Created: " + chef3+ '\n');

        frame.status.append("Starting thread\n");
        agent.start();
        
        frame.status.append("Starting thread\n");
        chef1.start();
        
        frame.status.append("Starting thread\n");
        chef2.start();

        frame.status.append("Starting thread\n");
        chef3.start();
    }
}

/**
 * This thread calculates 0! through 20!, where
 *  0! = 1
 *  n! = n * (n-1)!, n > 0
 */
class Agent implements Runnable
{
    /**
     * The text area where this thread's output will be displayed.
     */
    private JTextArea transcript;

    public Agent(JTextArea transcript) {
        this.transcript = transcript;
    }

    public void run() {
        for (int n = 1; n <= 20; n++) {
        	while(!ThreadExample1.empty)
        	{
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {}
        	}
    		putIngredients();
        }
    }
    
    private synchronized void putIngredients()
    {
    	Random random = new Random();
    	ThreadExample1.missingIng = random.nextInt(3);
    	if(ThreadExample1.missingIng == 0) {
    		transcript.append("Jam and Jelly has been placed on the table \n");
    	}else if(ThreadExample1.missingIng == 1) {
    		transcript.append("Jam and Bread has been placed on the table \n");
    	}else {
    		transcript.append("Bread and Jelly has been placed on the table \n");
    	}
        
		ThreadExample1.empty = false;
    }
}

class Chefone implements Runnable
{
    /**
     * The text area where this thread's output will be displayed.
     */
    private JTextArea transcript;

    public Chefone(JTextArea transcript) {
        this.transcript = transcript;
    }

    public void run() {
        while(true) {
        	while(ThreadExample1.missingIng!=0)
        	{
                try {
                	Thread.sleep(500);
                } catch (InterruptedException e) {}
        	}
    		makeEat();
        }
    }
    
    private synchronized void makeEat()
    {
    	ThreadExample1.missingIng = 4;
        transcript.append("Bread was added to make the sandwich and was eaten \n");
		ThreadExample1.empty = true;
    }
}

class Cheftwo implements Runnable
{
    /**
     * The text area where this thread's output will be displayed.
     */
    private JTextArea transcript;

    public Cheftwo(JTextArea transcript) {
        this.transcript = transcript;
    }

    public void run() {
        while(true) {
        	while(ThreadExample1.missingIng!=1)
        	{
                try {
                	Thread.sleep(500);
                } catch (InterruptedException e) {}
        	}
    		makeEat();
        }
    }
    
    private synchronized void makeEat()
    {
    	ThreadExample1.missingIng = 4;
        transcript.append("Jelly was added to make the sandwich and was eaten \n");
		ThreadExample1.empty = true;
    }
}

class Chefthree implements Runnable
{
    /**
     * The text area where this thread's output will be displayed.
     */
    private JTextArea transcript;

    public Chefthree(JTextArea transcript) {
        this.transcript = transcript;
    }

    public void run() {
        while(true) {
        	while(ThreadExample1.missingIng!=2)
        	{
                try {
                	Thread.sleep(500);
                } catch (InterruptedException e) {}
        	}
    		makeEat();
        }
    }
    
    private synchronized void makeEat()
    {
    	ThreadExample1.missingIng = 4;
        transcript.append("Jam was added to make the sandwich and was eaten \n");
		ThreadExample1.empty = true;
    }
}