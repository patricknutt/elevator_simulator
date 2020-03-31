/*
 * Elevator.java 
 * Written By Patrick Nutt
 * Date: 1 Jul 2007
 */
 
package elevator.simulator;

import java.util.Random;

import elevator.animator.Animator;
import elevator.animator.DrawEvent;
import elevator.animator.DrawListener;
import elevator.animator.StraightLinePath;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
/**
 * Elevator represents an elevator object. The elevator continuously moves from 
 * floor to floor. When it reaches the top floor, it reverses direction and
 * heads back down. It does the opposite when it reaches the ground floor.
 */
class Elevator implements Runnable, DrawListener {	

	private int myNumber;
	private Building myBuilding;
	private Animator animator;
	private StraightLinePath myPath = new StraightLinePath(1000,1000,1000,1000,20);
	private Color myColor = new Color (204, 204, 204);
	private int currFloor;
	private int reqFloor;
	
	 /**
	 * Constructor: Set the building
	 */
	 public Elevator(int number, Building building, Animator animator) {
	 	myNumber = number;
	 	myBuilding = building;
	 	this.animator = animator;
	 	this.currFloor = 0;
	 }
	 
	 public void call(int reqFloor) {
		 this.reqFloor = reqFloor;
		 this.run();
	 }
	 
	 /**
	 * Elevator thread that waits one second before arriving at and then leaving
	 * a floor. The Elevator is in continuous motion.
	 */
	 public void run() {
	 	
		int increment = 0;
		animator.addDrawListener(this);
		
		final int WAIT_TIME = 1999;	// < 2 seconds
	 	try {
	 		
	 		while (currFloor != reqFloor) {
	 			
				Random random = new Random(myNumber);
	  			int waitTime = random.nextInt(WAIT_TIME);
	  	
	  			// Wait for a random time up to 2 seconds before waiting on elevator to arrive	  	
	  			Thread.sleep(waitTime);
			  		 			
			  	myBuilding.elevatorArrives(currFloor, myNumber);
			  	// Wait for 1 second while elevator traverses to the next floor	  	
			  	Thread.sleep(500);			  	
	 			myBuilding.elevatorLeaves(currFloor, myNumber);
			  	
			  	// Move to next floor. Move up if at ground floor or down if at
			  	// top floor. Otherwise, continue in previous direction
			  	if (reqFloor > currFloor || currFloor == 0) {	
			  		increment = reqFloor - currFloor;
			  	}
			  	else if (reqFloor < currFloor || currFloor == myBuilding.getSize() - 1) { 
			  		increment = reqFloor - currFloor;
			  	}
			  	int lastFloor = currFloor;
			  	currFloor = currFloor + increment;
				move(120 + (myNumber * 340), (myBuilding.getFirstFloor() - (lastFloor * 50) ),
              	                     120 + (myNumber * 340), (myBuilding.getFirstFloor() - (currFloor * 50)), 20);
			}
		} catch (InterruptedException ie) {
		}
	}
	
	/**
	*   This function implements the procedural mechanism for
	*   moving this object.  It is synchronized, and so in the
	*   synchronized block it sets up the path, and then calls
	*   wait to stop the thread until the movement on the path
	*   has completed.
	*/
	private synchronized void move(int startX, int startY, int endX, int endY,int numberOfSteps) {
		try {
		    myPath = new StraightLinePath(startX, startY, endX, endY, numberOfSteps);
		    wait();
		} catch(InterruptedException e) {
		}
	}

	/**
	* draw method.  Get the Graphics object from the DrawEvent.  Then if the end of the
	* path has been reached, let the Elevator thread know.  Then draw the elevator moving
	* up/down to the next floor.
	*/
	public synchronized void draw(DrawEvent de) {
		// Get the Graphics to draw on.
		Graphics g = de.getGraphics();
		
		// Get where to draw.  If at end of path notify
		// the elevator.
//		Point p = myPath.nextPosition();
//		int x = (int)p.getX();
//		int y = (int)p.getY();
		
		if (! myPath.hasMoreSteps()) {
		        notify();
		}        
		
		Point position = myPath.nextPosition();
		
		Color C = g.getColor();
		g.setColor(myColor);
		
		g.fillRect(position.x, position.y, 40, 40);
		
		g.setColor(C);
	}
}
			  	
			  	