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
 * 
 * TODO: Animate the elevator doors opening and closing
 */
class Elevator implements Runnable, DrawListener {	

	private int myNumber;
	private Building myBuilding;
	private Animator animator;
	private StraightLinePath myPath;
	private Color elevator_color = Color.DARK_GRAY;
	private int elevator_x;
	private int currFloor;
	private String direction = "up"; 
	int start_y;
	int end_y;
	
	 /**
	  * Constructor: Set the building
	  * @param number
	  * @param building
	  * @param animator
	  */
	 public Elevator(int number, Building building, Animator animator) {
	 	myNumber = number;
	 	myBuilding = building; 
	 	currFloor = myBuilding.getSize() - 1;
	 	start_y = myBuilding.getTopEdge() + ((currFloor) * myBuilding.getFloorHeight());
		end_y = myBuilding.getTopEdge() + ((currFloor  - 1) * myBuilding.getFloorHeight());
	 	this.animator = animator;
	 	
	 	if (myNumber == 0) {
	 		elevator_x = myBuilding.getElevatorLocation() + 1; 		
	 	}
	 	else {
	 		elevator_x = myBuilding.getElevatorLocation() + 41;	 		
	 	}

	 	myPath = new StraightLinePath(elevator_x, start_y, elevator_x, end_y, 10);
	 }
	 
	 /**
	  * Elevator thread that waits one second before arriving at and then leaving
	  * a floor. The Elevator is in continuous motion.
	  */
	 public void run() {
		animator.addDrawListener(this);
		
		final int WAIT_TIME = 1000;	
	 	try {	 		
	 		while (true) {
				Random random = new Random(myNumber);
	  			int waitTime = random.nextInt(WAIT_TIME);
	  				  	
	  			// Wait for a random time before waiting on elevator to arrive	  	
	  			Thread.sleep(waitTime);	
		  		
		  		if(currFloor < myBuilding.getSize()) {
				  	myBuilding.elevatorArrives(currFloor, myNumber);				  	  			  	
				  	Thread.sleep(waitTime);
		  			
		 			myBuilding.elevatorLeaves(currFloor, myNumber);
		  		
		 			if (direction == "up") {
		 				start_y = myBuilding.getTopEdge() + ((currFloor) * myBuilding.getFloorHeight());
		 				end_y = myBuilding.getTopEdge() + ((currFloor  - 1) * myBuilding.getFloorHeight());
		 				currFloor--;
		 			}
		 			else {
		 				start_y = myBuilding.getTopEdge() + ((currFloor) * myBuilding.getFloorHeight());
		 				end_y = myBuilding.getTopEdge() + ((currFloor + 1) * myBuilding.getFloorHeight());
		 				currFloor++;
		 			}

		  			move(elevator_x, start_y, elevator_x, end_y, 10);
		  		}	
		  		
		  		
		  		if(currFloor == (myBuilding.getSize() - 1) || currFloor == 0) {
		  			switchDirection();
		  		}
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
    private synchronized void move(int startX, int startY, int endX, 
            int endY,int numberOfSteps) {
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
	public synchronized void draw(DrawEvent d_event) {
		// Get the Graphics to draw on.
		Graphics graph_gen = d_event.getGraphics();
		
		// Get where to draw.  If at end of path notify
		// the elevator.
		Point p = myPath.nextPosition();
		int x = (int)p.x;
		int y = (int)p.y;
		
		
		if (! myPath.hasMoreSteps()) {
			notify();
		} 
		
		graph_gen.setColor(elevator_color);
		
		graph_gen.fillRect(x, y, 39, myBuilding.getFloorHeight());
	}
	
	public void switchDirection() {
		if (direction == "up") {
			direction = "down";
		}
		else {
			direction = "up";
		}
	}
}
			  	
			  	