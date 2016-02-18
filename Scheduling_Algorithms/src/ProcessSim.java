/**
 * Simulates a process that arrives to the cpu at a certain time, runs for a certain time
 * and has a certain priority level
 * 
 * test comment 2956
 * @author Jonathan Neel
 *
 */
public class ProcessSim {
	private float arrivalTime;
	private float runTime;
	private float remainingRunTime;
	private int name;
	private int priority;
	
	public float getRemainingRunTime() {
		return remainingRunTime;
	}

	public void setRemainingRunTime(float remainingRunTime) {
		this.remainingRunTime = remainingRunTime;
	}
	public float getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(float arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public float getRunTime() {
		return runTime;
	}

	public void setRunTime(float runTime) {
		this.runTime = runTime;
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * constructor for ProcessSim takes 4 arguments to set its fields
	 * @param arrivalTime the time the process arrives at the CPU
	 * @param runTime the amount of quantum the process runs for
	 * @param priority the priority the process has in the piroity queue
	 * @param name the name of the process (numbered)
	 */
	public ProcessSim(float arrivalTime, float runTime, int priority, int name)
	{
		this.arrivalTime = arrivalTime;
		this.runTime = runTime;
		remainingRunTime = runTime;
		this.priority = priority;
		this.name = name;
	}
	
	
}
