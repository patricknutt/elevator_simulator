package elevator.simulator;

import elevator.animator.Animator;

public class ElevatorSim {
	static final int TOTAL_PAX = 20;	// Number of passengers
	static final int TOTAL_CARS = 3;	// Number of elevator cars
	static final int TOTAL_FLOORS = 15;  // Number of floors (Max is 10)
	
	public static void main(String[] args) {

		// Create Passenger and Elevator Threads
		Thread[] paxThreads = new Thread[TOTAL_PAX];
		Thread[] elevThreads = new Thread[TOTAL_CARS];
				
		// Create building
		Animator animator = new Animator();
		animator.setVisible(true);
        
		Building building = new Building(TOTAL_FLOORS, TOTAL_CARS, animator);
		
		// Start all elevator threads
		for (int i = 0; i < TOTAL_CARS; i++) {
			Elevator elevator = new Elevator(i, building, animator);
			(elevThreads[i] = new Thread(elevator)).start();
		}
		// Start all passenger threads
		for (int i = 0; i < TOTAL_PAX; i++) {
			Passenger passenger = new Passenger(i, building, animator);
			(paxThreads[i] = new Thread(passenger)).start();
		}
	}

}
