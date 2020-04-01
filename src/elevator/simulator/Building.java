/*
 * Building.java 
 * Written By Patrick Nutt
 * Date: 1 Jul 2007
 */ 
 
package elevator.simulator;

import java.awt.Graphics;
import java.awt.Font;

import elevator.animator.Animator;
import elevator.animator.DrawEvent;
import elevator.animator.DrawListener;

import java.awt.Color;

/**
 * Building represents a building object that houses a specific number of passengers, 
 * elevators and floors. The Building object takes an input for the number of elevators and floors and draws each. 
 * The Building object acts as a state machine controlling the movements of elevators and passengers
 */

public class Building implements DrawListener{
	
	// Initialize building parameters
	private Color myColor = Color.LIGHT_GRAY; 
	private Floor[][] floor; 
	private int numFloors;
	private int left = 110;
	private int top = 50;
	private int right = 510;
	private int building_width = right - left;
	private int building_height;
	private int floor_height = 50;
	
	public Building (int numFloors, int numCars, Animator animator) {
		this.numFloors = numFloors;
		this.building_height = numFloors * floor_height;
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
	
	public void callElevator(int index, int side) {
		floor[index][side].callCar();
	}
	
	public int getFloorHeight() {
		return floor_height;
	}
	
	public int getSize() {
		return floor.length;
	}
	
	public int getLeftEdge() {
		return left;
	}
	
	public int getRightEdge() {
		return right;
	}
	
	public int getBottomEdge() {
		return building_height + top - 1;
	}
	
	public int getTopEdge() {
		return top;
	}
	
	public void draw(DrawEvent d_event) {
		
		Graphics graph_gen = d_event.getGraphics();
		
		// Save old color and font     	
		Color original_color = graph_gen.getColor();
		        
		// Draw Building shell
		graph_gen.setColor(myColor);         
		graph_gen.fill3DRect (left, top, building_width, building_height, true); 
		
		// Draw roof
		int x_coords[] = new int[4];
		int y_coords[] = new int[4];
		x_coords[0] = left;    
		x_coords[1] = left + 100;    
		x_coords[2] = right - 100;  
		x_coords[3] = right;     
		y_coords[0] = y_coords[3] = top;
		y_coords[1] = y_coords[2] = top - floor_height;
		
		graph_gen.setColor(Color.BLACK);
		graph_gen.fillPolygon (x_coords, y_coords, 4);

		//Draw lines, draw a door and show the floor number for each floor       
		for (int i = numFloors; i >= 1; i--) {
			
			int yFloor = getBottomEdge() - (floor_height * (i - 1)); 

			graph_gen.drawLine(160,yFloor,460,yFloor);
		 	
			graph_gen.setColor(Color.BLACK);
			graph_gen.fillRect (300, yFloor - 40, 20, 40);
		
			graph_gen.setFont(new Font("Times New Roman", Font.PLAIN, 10));
			graph_gen.setColor(Color.RED);        	
			graph_gen.drawString ("" + i, 306, yFloor - 20);
		
			graph_gen.setColor(original_color);			
		}      
    }
}