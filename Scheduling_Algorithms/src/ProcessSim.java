/**
 * Simulates a process that arrives to the cpu at a certain time, runs for a certain time
 * and has a certain priority level
 * 
 * @author Jonathan Neel
 *
 */
public class ProcessSim {
	private float arrivalTime;
	private float runTime;
	private float remainingRunTime;
	private String name;
	private int priority;
        
        /**
         * Creates empty process.
         */
	public ProcessSim()
	{
		arrivalTime = 0;
		runTime = 0;
		remainingRunTime = 0;
		priority = 0;
		name = "null";
	}
        
	/**
	 * constructor for ProcessSim takes 4 arguments to set its fields
	 * @param arrivalTime the time the process arrives at the CPU
	 * @param runTime the amount of quantum the process runs for
	 * @param priority the priority the process has in the priority queue
	 * @param name the name of the process (numbered)
	 */
	public ProcessSim(float arrivalTime, float runTime, int priority, String name)
	{
		this.arrivalTime = arrivalTime;
		this.runTime = runTime;
		remainingRunTime = runTime;
		this.priority = priority;
		this.name = name;
	}
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
        
        public String toString() {
            return "Process " + name + ", AT: " + arrivalTime
                    + ", ERT: " + runTime + ", Priority: " + priority;
        }
}
