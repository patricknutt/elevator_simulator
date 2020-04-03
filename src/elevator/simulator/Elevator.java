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
import elevator.animator.MoveController;
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

	private int car_number;
	private Building building;
	private Animator animator;
	private StraightLinePath elevator_path;
	private MoveController m_controller;
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
	 	this.car_number = number;
	 	this.building = building; 
	 	this.animator = animator;
	 	
	 	currFloor = building.getSize() - 1;
	 	start_y = building.getTopEdge() + ((currFloor) * building.getFloorHeight());
		end_y = building.getTopEdge() + ((currFloor  - 1) * building.getFloorHeight());
 		elevator_x = building.getElevatorLocation() + (car_number * 40 + 1);

 		elevator_path = new StraightLinePath(elevator_x, start_y, elevator_x, end_y, 10);
	 	this.m_controller = new MoveController(elevator_path);	
	 }
	 
	 /**
	  * Elevator thread that waits one second before arriving at and then leaving
	  * a floor. The Elevator is in continuous motion.
	  */
	 public void run() {			
				
		final int WAIT_TIME = 1000;	
		animator.addDrawListener(this);
		
 		while (true) {		
		 	try {
					Random random = new Random(car_number);
		  			int waitTime = random.nextInt(WAIT_TIME);		 			
		 			
		  			// Wait for a random time before waiting on elevator to arrive	  	
		  			Thread.sleep(waitTime);	
			  		
		  			// Determine path based on direction variable and where the elevator is in the building
			  		if(currFloor < building.getSize()) {
					  	building.elevatorArrives(currFloor, car_number);			  			
			 			building.elevatorLeaves(currFloor, car_number);
			  		
			 			if (direction == "up") {
			 				start_y = building.getTopEdge() + ((currFloor) * building.getFloorHeight());
			 				end_y = building.getTopEdge() + ((currFloor  - 1) * building.getFloorHeight());
			 				currFloor--;
			 			}
			 			else {
			 				start_y = building.getTopEdge() + ((currFloor) * building.getFloorHeight());
			 				end_y = building.getTopEdge() + ((currFloor + 1) * building.getFloorHeight());
			 				currFloor++;
			 			}
		 				elevator_path = new StraightLinePath(elevator_x, start_y, elevator_x, end_y, 10);
			  			m_controller.move(elevator_path);
			  		}				  		
			  		
			  		// Change direction of the elevator to keep it moving
			  		if(currFloor == (building.getSize() - 1) || currFloor == 0) {
			  			switchDirection();
			  		}
			} catch (InterruptedException ie) {
			}
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
		Point p = m_controller.nextPosition();
		int x = (int)p.x;
		int y = (int)p.y;
		
		graph_gen.setColor(elevator_color);
		
		graph_gen.fillRect(x, y, 39, building.getFloorHeight());
	}
    
	/**
	 * switchDirection
	 * 
	 * Changes the value of the direction variable from up to down or from down to up
	 */
	public void switchDirection() {
		if (direction == "up") {
			direction = "down";
		}
		else {
			direction = "up";
		}
	}
}			  	