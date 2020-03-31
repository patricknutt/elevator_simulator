/*
 * Building.java 
 * Written By Patrick Nutt
 * Date: 1 Jul 2007
 */ 
 
package elevator.simulator;

import java.awt.Graphics;

import elevator.animator.Animator;
import elevator.animator.DrawEvent;
import elevator.animator.DrawListener;

import java.awt.Color;

/**
 * Building represents a building object that houses a specific number of elevators 
 * and floors. The Building creates five elevator threads and 50 passenger
 * threads and runs each. It also provides encapsulation of the methods of the 
 * Floor objects.
 */

public class Building implements DrawListener{
	
	private Color myColor = new Color(138, 168, 204); // Color to paint the building
	private Floor[][] floor; // Array of floors
	private int numFloors;
	
	public Building (int numFloors, int numCars, Animator animator) {
		this.numFloors = numFloors;
		floor = new Floor[numFloors][numCars];
		animator.addDrawListener(this);
								
		for (int i = 0; i < numFloors; i++) {
			for (int j = 0; j < numCars; j++) {
				floor[i][j] = new Floor();
			}
		}
	}
		
	public void elevatorArrives(int index, int side) {
		
		floor[index][side].recieveCar();
	}
	
	public void elevatorLeaves(int index, int side) {
		
		floor[index][side].sendCar();
	}
	
	public void waitForElevator(int index, int side) {
		
		floor[index][side].waitPAX();
	}
	
	public void getOnOffElevator(int index, int side) {
		
		floor[index][side].movePAX();
	}
	
	public int getSize() {
		return floor.length;
	}
	
	public int getFirstFloor() {
		return numFloors * 50;
	}	

	public void draw(DrawEvent de) {
		
		Graphics g = de.getGraphics();
		
		// Save old color and font     	
		Color C = g.getColor();
		        
		// Draw Building shell
		g.setColor(myColor);         
		g.fill3DRect (110, 50, 400, numFloors*50, true); 
		
		// Draw roof
		int x[] = new int[4];
		int y[] = new int[4];
		x[0] = 160;    y[0] = 50;
		x[1] = 210;    y[1] = 0;
		x[2] = 410;   y[2] = 0;
		x[3] = 460;   y[3] = 50;  		
		g.setColor(Color.black);
		g.fillPolygon (x, y, 4);

		//Draw lines to show each floor. Also draw exit doors on each floor        
		for (int i = 1; i <= numFloors; i++) {
			
			int yFloor = 50 + (i * 50);
			
			g.drawLine(160,yFloor,460,yFloor);
		 	
			g.setColor(Color.black);
			g.fillRect (300, yFloor - 40, 20, 40);
		
			g.setColor(Color.red);        	
			g.drawString ("EXIT", 320, yFloor - 30);
		
			g.setColor(C);
			
		}
        

    }
}