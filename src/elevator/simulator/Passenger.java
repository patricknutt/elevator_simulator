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
import elevator.animator.MoveController;
import elevator.animator.StraightLinePath;

/**
 * Passenger represents an elevator passenger. The passenger chooses a floor, 
 * waits for an elevator, then chooses a destination floor and gets off on that 
 * floor.
 * 
 * TODO: Animate the person walking (legs/arms moving)
 * TODO: Have the people stand next to each other instead of taking up the same space
 * TODO: Adjust face color based on the passenger's unique number
 */
class Passenger implements Runnable, DrawListener {
	private Building building;
	private Animator animator;
    private StraightLinePath to_elevator;
    private StraightLinePath from_elevator;
    private MoveController m_controller;
    private Color face_color = Color.ORANGE;
    private Color body_color = Color.BLUE;
    private int left_x;
    private int right_x;
    private int num_cars;
	
	/** 
	 * <b>Passenger</b><p>
	 * <i>public Passenger (Building building, Animator animator, int num_cars)</i>
	 * <p>
	 * Creates a new Passenger object. This constructor sets building parameters and define starting and ending x values
	 * @param building - Building object that will house this passenger
	 * @param animator - The Animator class used to animate this passenger
	 * @param num_cars - The number of cars included in the Building object
	 *  
	 */	 
	 public Passenger (Building building, Animator animator, int num_cars) {

	 	this.building = building;
	 	this.animator = animator;
	 	this.num_cars = num_cars;
	 	this.m_controller = new MoveController(to_elevator);
	 	left_x = building.getLeftEdge() + building.getDoorWidth();
	 	right_x = building.getElevatorLocation() - 15;	
	 }
	 
	 /**
	  * setPaxElevation
	  * @param floor - representation of the floor this passenger is on
	  * @return The y coordinate location corresponding to the passed in floor
	  */
	 private int setPaxElevation(int floor) {
		 return ((building.getTopEdge()) + (floor * building.getFloorHeight()) - 1);
	 }
	 
	 /** 
	  * Passenger thread that chooses a starting floor, waits for an elevator, 
	  *	chooses a ending floor and then exits the elevator.
	  */
	  public void run() {
	  	
	  	final int WAIT_TIME = 499;	
	  	while(true){
		  	try{
			  	Random random = new Random();
			  	int waitTime = random.nextInt(WAIT_TIME);
			  	
			  	// Wait for a random time up to WAIT_TIME before waiting on elevator to arrive	  	
			  	Thread.sleep(waitTime);
			  		
			  	// Randomly choose the starting  and destination floors & elevator
			  	int curr_floor = random.nextInt(building.getSize());
			  	int dest_floor = random.nextInt(building.getSize());
			  	int load_elevation = setPaxElevation(curr_floor);
			  	int unload_elevation = setPaxElevation(dest_floor);
			  	int elevator = random.nextInt(num_cars);
			  	
			  	// Create paths
			  	to_elevator = new StraightLinePath(left_x, load_elevation, right_x, load_elevation, 25);
			  	from_elevator = new StraightLinePath(right_x, unload_elevation, left_x, unload_elevation, 25);


		        // Exit the door and walk to the elevator
		        animator.addDrawListener(this);	 	
		        m_controller.move(to_elevator);
		        
			  	// Wait for elevator and load on the elevator when it arrives
			  	building.waitForElevator(curr_floor, elevator);
			  	building.getOnOffElevator(curr_floor, elevator);		  		
		    	animator.removeDrawListener(this);
			  				  	
		        // Wait for the elevator to arrive at the destination floor        
			  	building.waitForElevator(dest_floor, elevator);
			  	building.getOnOffElevator(dest_floor, elevator);
			  	
			  	// Exit the elevator and walk through the door
		        animator.addDrawListener(this);
		        m_controller.move(from_elevator);
			  	animator.removeDrawListener(this);	
			  	
		  	} catch (InterruptedException ie) {
		  	}
	  	}
	  }
	  
    /**
     *  Method to draw the person at a given position.
     */ 
    public synchronized void draw(DrawEvent d_event) {
        Graphics graph_gen = d_event.getGraphics();

        // Get where to draw.  If at end of path notify the passenger.
        Point p = m_controller.nextPosition();
        int x = (int)p.getX();
        int y = (int)p.getY();
        
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
}	 