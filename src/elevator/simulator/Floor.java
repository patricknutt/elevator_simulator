/*
 * Floor.java 
 * Written By Patrick Nutt
 * Date: 1 Jul 2007
 */
 
/**
* Floor represents a floor component. This component monitors the activity of 
* passenger and elevator objects, and changes states accordingly.
*/
 
package elevator.simulator;

class Floor {

	private static final int NO_WAIT = 0;
	private static final int LOAD_UNLOAD = 1;
	private static final int LEAVE = 2;
	private static final int WAIT = 3;
	private int state = NO_WAIT;
	
	/** 
	 * Method called when a passenger wants to enter/exit an elevator car
	 */
	synchronized public void movePAX() {
		
		try {
			// Pre-Condition 
			while (!(state == LOAD_UNLOAD || state == LEAVE)) {
				wait();
			}			
			// Simulate elevator loading/unloading. Waits 1/2 second
			Thread.sleep(500);
			
			//Post-Condition	
	 		state = LEAVE;
			notifyAll();
			
		} catch (InterruptedException ie) {
		}  			
	}
	
	/**
	 * Method called when a passenger is waiting for an elevator
	 */
	synchronized public void waitPAX() {
		try {
			// Pre-Condition
			while (!(state == WAIT || state == NO_WAIT)) {
				wait();
			}

			// Post-Condition
				state = WAIT;
				notifyAll();
	
		} catch (InterruptedException ie) {
		}
		
	}
	
	/**
	 * Method called when an elevator car arrives
	 */
	synchronized public void recieveCar() {
			
		try{
			// Pre-Condition
			while (!(state == WAIT || state == NO_WAIT)) {
				wait();
			}
			
			// Post-Condition
			if (state == WAIT) {
				state = LOAD_UNLOAD;
				notifyAll();
			}
			else if (state == NO_WAIT) {
				state = LEAVE;
				notifyAll();
			}
			
		} catch (InterruptedException ie) {
		}
		
	}
	
	/** 
	 * Method called when an elevator car leaves
	 */
	synchronized public void sendCar() {
		
		try{
				// Pre-Condition
				while (!(state == LEAVE)) {
					wait();
				}

				// Post-Condition
				state = NO_WAIT;
				notifyAll();
				
		} catch (InterruptedException ie) {
		}			
	}
}
  	
  	
  	
  	
