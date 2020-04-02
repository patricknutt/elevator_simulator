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
 * 
 * TODO: Animate the person walking
 * TODO: Have the people stand next to each other instead of taking up the same space
 */
class Passenger implements Runnable, DrawListener {
	
	private int pax_number; 	// Number of this passenger
	private Building building;
	private Animator animator;
    private Path path;
    private Color face_color = Color.ORANGE;
    private Color body_color = Color.BLUE;
    private int left_x;
    private int right_x;
    private int elevator = 0; 
	
	/** 
	 * Constructor: Set the passenger number and building
	 */	 
	 public Passenger (int pax_number, Building building, Animator animator) {
	 	
	 	this.pax_number = pax_number;
	 	this.building = building;
	 	this.animator = animator;
	 	left_x = building.getLeftEdge();
	 	right_x = building.getElevatorLocation() - 15;
	 	
	 }
	 
	 private int setPaxElevation(int floor) {
		 return ((building.getTopEdge()) + (floor * building.getFloorHeight()) - 1);
	 }
	 
	 /** 
	  * Passenger thread that chooses a starting floor, waits for an elevator, 
	  *	chooses a ending floor and then exits the elevator.
	  */
	  public void run() {
	  	
	  	final int WAIT_TIME = 499;	// < 2 minutes
	  	try{
		  	Random random = new Random();
		  	int waitTime = random.nextInt(WAIT_TIME);
		  	// Wait for a random time up to 2 minutes before waiting on elevator to arrive	  	
		  	Thread.sleep(waitTime);
		  		
		  	// Randomly choose the starting floor
		  	int curr_floor = random.nextInt(building.getSize());
		  	int pax_elevation = setPaxElevation(curr_floor);
		  	
		  	// Randomly choose elevator
		  	elevator = random.nextInt(2);

	        animator.addDrawListener(this);
	        // Move from left to right
	        move(left_x, pax_elevation, right_x, pax_elevation, 25);	        

		  	building.waitForElevator(curr_floor, elevator);
		  	building.getOnOffElevator(curr_floor, elevator); 
		  	
		  	// Wait for elevator. When elevator arrives, passenger gets on.		  		
	    	animator.removeDrawListener(this);
	    	Thread.sleep(waitTime);
	    	
		  	// Randomly choose the ending floor
		  	int dest_floor = random.nextInt(building.getSize()); // 0 to 9
		  	pax_elevation = setPaxElevation(dest_floor);
		  	
	        // Code to move from elevator.         
		  	building.waitForElevator(dest_floor, elevator);
		  	building.getOnOffElevator(dest_floor, elevator);
		  	
		  	// Exit elevator and walk through the door
	        animator.addDrawListener(this);
	        move(right_x, pax_elevation, left_x, pax_elevation, 25);
		  	animator.removeDrawListener(this);
		  	
	    	Thread.sleep(waitTime);
	  		
	  	} catch (InterruptedException ie) {
	  	}
	  }
	  
    /**
     *  Method to draw the person at a given position.
     */ 
    public synchronized void draw(DrawEvent d_event) {
        Graphics graph_gen = d_event.getGraphics();

        // Get where to draw.  If at end of path notify the passenger.
        Point p = path.nextPosition();
        int x = (int)p.getX();
        int y = (int)p.getY();

        // Notify if done drawing
        if (! path.hasMoreSteps()) {
                notify();
        }        

        //Draw Stick Figure
	    graph_gen.setColor(face_color);  
	    graph_gen.fillOval(x + 5, y + 10, 10, 10); // face
	    graph_gen.setColor(body_color);
	    graph_gen.drawOval(x + 5, y + 10, 10, 10); // head
	    graph_gen.drawLine(x + 8, y + 20, x + 8, y + 40); // body
	    graph_gen.drawLine(x + 0, y + 25, x + 15, y + 25); // arms 
	    graph_gen.drawLine(x, y + 50, x + 8, y + 40); // left leg
	    graph_gen.drawLine(x + 15, y + 50, x + 8, y + 40); // right leg
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
            path = new StraightLinePath(startX, startY, endX, endY, numberOfSteps);
            wait();
        } catch(InterruptedException e) {
        }
    }	  
}
	  	

	  						
	  	
	  
	 
	 