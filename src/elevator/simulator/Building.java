/*
 * Building.java 
 * Written By Patrick Nutt
 * Date: 1 Jul 2007
 */ 
 
package elevator.simulator;

import java.awt.Font;
import java.awt.Graphics;

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
	private Color building_color = Color.LIGHT_GRAY; 
	private Color roof_color = Color.BLACK;
	private Color door_color = Color.BLACK;
	private Color floor_color = Color.BLACK;
	private Color text_color = Color.RED;
	private Floor[][] floor; 
	private int num_floors;
	private int num_cars;
	private int building_left = 110;
	private int building_top = 200;
	private int building_right = 510;
	private int building_width = building_right - building_left;
	private int building_height;
	private int floor_height = 50;
	private int door_height = 40;
	private int door_width = 20;
	private Roof roof;
	
	public Building (int num_floors, int num_cars, Animator animator) {
		this.num_floors = num_floors;
		this.num_cars = num_cars;
		this.building_height = this.num_floors * floor_height;
		
		floor = new Floor[this.num_floors][this.num_cars];
		animator.addDrawListener(this);
		
		// Create floors. 
		// TODO: Is this necessary?
		for (int i = 0; i < num_floors; i++) {
			for (int j = 0; j < num_cars; j++) {
				floor[i][j] = new Floor();
			}
		}
	}
	
	static class Roof {
		private int top_left;
		private int top_right;
		private int bottom_left;
		private int bottom_right;
		private int top;
		private int bottom;
		
		public Roof(int top_left, int top_right, int bottom_right, int bottom_left, 
				int top, int bottom) {
			this.top_left = top_left;
			this.top_right = top_right;
			this.bottom_left = bottom_left;
			this.bottom_right = bottom_right;
			this.top = top;
			this.bottom = bottom;			
		}
		
		public int[] getX() {
			int coords[] = new int[4];
			coords[0] = bottom_left;
			coords[1] = top_left;
			coords[2] = top_right;
			coords[3] = bottom_right;
			return coords;
		}
		
		public int[] getY() {
			int coords[] = new int[4];
			coords[0] = coords[3] = bottom;
			coords[1] = coords[2] = top;
			return coords;
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
	
	public int getFloorHeight() {
		return floor_height;
	}
	
	public int getDoorWidth() {
		return door_width;
	}
	
	public int getSize() {
		return floor.length;
	}
	
	public int getLeftEdge() {
		return building_left;
	}
	
	public int getRightEdge() {
		return building_right;
	}
	
	public int getBottomEdge() {
		return building_height + building_top - 1;
	}
	
	public int getTopEdge() {
		return building_top;
	}
	
	public int getElevatorLocation() {
		return building_right - (40 * num_cars);
	}
	
	private void drawFloor(Graphics graph_gen) {
		for (int i = num_floors; i >= 1; i--) {
			int this_floor = getBottomEdge() - (floor_height * (i - 1) + 1);	
			
			graph_gen.setColor(door_color);
			graph_gen.fillRect (building_left + 1, this_floor - 40, door_width, door_height);
			
			graph_gen.setColor(floor_color);
			graph_gen.drawLine(building_left,  this_floor,  getElevatorLocation(),  this_floor);
		
			graph_gen.setFont(new Font("Times New Roman", Font.PLAIN, 10));
			graph_gen.setColor(text_color);        	
			graph_gen.drawString ("" + i, building_left + 7, this_floor - 20);
		}
	}
	
	private void drawRoof(Graphics graph_gen) {
		roof = new Roof(building_left + 100, building_right - 100, building_right, building_left, building_top - floor_height, building_top);		
		graph_gen.setColor(roof_color);
		graph_gen.fillPolygon (roof.getX(), roof.getY(), 4);
	}
	
	public void draw(DrawEvent d_event) {
		
		Graphics graph_gen = d_event.getGraphics();
		        
		// Draw Building shell
		graph_gen.setColor(building_color);         
		graph_gen.fill3DRect (building_left, building_top, building_width, building_height, true); 
		
		// Draw roof
		drawRoof(graph_gen);

		//Draw lines, draw a door on the left and leave space for elevator(s) to the right and show the floor number for each floor       
		drawFloor(graph_gen);     
    }
}