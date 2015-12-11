


/**
 * Class for general smart service. Any concrete service should extend it.
 *
 * @author masa-n
 */
public abstract class SmartService {

	/** wait interval in msec */
	private int waitTime;
	/** flag for the service status */
	private boolean running = false;


	public int getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * Start service. Implemented as Template Method for any smart services.
	 * @throws InterruptedException
	 */
	public void startService() throws InterruptedException {
		running = true;

		//Repeat until explicitly stopped.
		while (running) {
			// (1) State Observation
			System.out.println("(Step 1) Observing State...");
		
			// (2) State Interpretation
			System.out.println("(Step 2) Interpreting State...");
		
			// If the situation is not acceptable
			
			// Sleep for a while
			Thread.sleep(waitTime);
		}

		System.out.println("Terminating service.");

	}

	/**
	 * Stop service.
	 *
	 */
	public void stopService() {
		running = false;
	}

	//public abstract void pause();
	//public abstract void resume();


}
