/*
 * Passenger.java 
 * Written By Patrick Nutt
 * Date: 1 Jul 2007
 */
 
package elevator.simulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import elevator.animator.Animator;
import elevator.animator.DrawEvent;
import elevator.animator.DrawListener;
import elevator.animator.Path;
import elevator.animator.StraightLinePath;

/**
 * Passenger represents an elevator passenger. The passenger chooses a floor, 
 * waits for an elevator, then chooses a destination floor and gets off on that 
 * floor.
 */
class Passenger implements Runnable, DrawListener {
	
	private int myNumber; 	// Number of this passenger
	private Building myBuilding;
	private Animator animator;
    private Path myPath;
    private Color faceColor = new Color(255, 153, 80);
    private Color bodyColor = new Color(204, 80, 0); 
    private int side;
	
	/** 
	 * Constructor: Set the passenger number and building
	 */	 
	 public Passenger (int number, Building building, Animator animator) {
	 	
	 	myNumber = number;
	 	myBuilding = building;
	 	this.animator = animator;
	 	
	 }
	 
	 /** 
	  * Passenger thread that chooses a starting floor, waits for an elevator, 
	  *	chooses a ending floor and then exits the elevator.
	  */
	  public void run() {
	  	
	  	final int WAIT_TIME = 119999;	// < 2 minutes
	  	try{
		  	Random random = new Random();
		  	int waitTime = random.nextInt(WAIT_TIME);
		  	// Wait for a random time up to 2 minutes before waiting on elevator to arrive	  	
		  	Thread.sleep(waitTime);
		  		
		  	// Randomly choose the starting floor
		  	int currFloor = random.nextInt(myBuilding.getSize());
		  	side = random.nextInt(2);
		  		
		  	// Wait for elevator. When elevator arrives, passenger gets on.
	        // Move to elevator
	        animator.addDrawListener(this);
	        
	        move( 300, ((myBuilding.getTopFloor()) - currFloor * 50), 160 + (side * 285), ((myBuilding.getTopFloor()) - currFloor * 50), 25);
	        
	        //myBuilding.callElevator(currFloor, side);
		  	myBuilding.waitForElevator(currFloor, side);
		  	myBuilding.getOnOffElevator(currFloor, side); 
		  		
	    	animator.removeDrawListener(this);
	    	
		  	// Randomly choose the ending floor
		  	int destFloor = ((currFloor + (random.nextInt(myBuilding.getSize() - 1)))
		  					 % myBuilding.getSize());
		  		  	
	        // Code to move from elevator.         
		  	myBuilding.waitForElevator(destFloor, side);
		  	myBuilding.getOnOffElevator(destFloor, side);
		  	
	        animator.addDrawListener(this);
	        move( 160 + (side * 285), ((myBuilding.getTopFloor()) - destFloor * 50), 300, ((myBuilding.getTopFloor()) - destFloor * 50), 25);  
		  	animator.removeDrawListener(this);
	  		
	  	} catch (InterruptedException ie) {
	  	}
	  }
	  
    /**
     *  Method to draw the person at a given position.
     */ 
    public synchronized void draw(DrawEvent de) {
        Graphics g = de.getGraphics();
        Color C = g.getColor();        // Save old color

        // Get where to draw.  If at end of path notify
        // the passenger.
        Point p = myPath.nextPosition();
        int x = (int)p.getX();
        int y = (int)p.getY();

        // Notify if done drawing
        if (! myPath.hasMoreSteps()) {
                notify();
        }        

        //Draw Stick Figure
    g.setColor(faceColor);    
	g.fillOval(x + 5, y+10, 10, 10); // face
	g.setColor(bodyColor);
	g.drawOval(x+5, y+10, 10, 10); // head
    g.drawString("" + myNumber, x + 15 - (side * 30), y+25); // number
	g.drawLine(x+8, y+20, x+8, y+40); // body
	g.drawLine(x+0, y+25, x+15, y+25); // arms 
	g.drawLine(x, y+50, x +8, y+40); // left leg
	g.drawLine(x+15, y+50, x+8, y+40); // right leg

        g.setColor(C);                 // Restore old color
    }

     /**
      *   This function implements the procedural mechanism for
      *   moving this object.  It is synchronized, and so in the
      *   synchronized block it sets up the path, and then calls
      *   wait to stop the thread until the movement on the path
      *   has completed.
      */
    private synchronized void move(int startX, int startY, int endX, 
        int endY,int numberOfSteps) {
        try {
            myPath = new StraightLinePath(startX, startY, endX, endY, numberOfSteps);
            wait();
        } catch(InterruptedException e) {
        }
    }	  
}
	  	

	  						
	  	
	  
	 
	 