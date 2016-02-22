/**
 * Simulates a process that arrives to the cpu at a certain time, runs for a certain time
 * and has a certain priority level
 * 
 * @author Jonathan Neel
 *
 */
public class ProcessSim {
	private float arrivalTime;
	private float expectedRunTime;
	private float remainingRunTime;
	private String name;
	private int priority;
        private boolean readyState;
        private float xArrivalTime;
        
        /**
         * Creates empty process.
         */
	public ProcessSim()
	{
		arrivalTime = 0;
		expectedRunTime = 0;
		remainingRunTime = 0;
                xArrivalTime = arrivalTime;
		priority = 0;
		name = "null";
                readyState = false;
	}
        
	/**
	 * constructor for ProcessSim takes 4 arguments to set its fields
	 * @param arrivalTime the time the process arrives at the CPU
	 * @param expectedRunTime the amount of quantum the process runs for
	 * @param priority the priority the process has in the priority queue
	 * @param name the name of the process (numbered)
	 */
	public ProcessSim(float arrivalTime, float runTime, int priority, String name)
	{
		this.arrivalTime = arrivalTime;
		expectedRunTime = runTime;
		remainingRunTime = runTime;
                xArrivalTime = arrivalTime;
		this.priority = priority;
		this.name = name;
                readyState = false;
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
		return expectedRunTime;
	}

	public void setRunTime(float runTime) {
		this.expectedRunTime = runTime;
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

        public boolean getReadyState() {
            return readyState;
        }
        
        public void setReadyState(boolean readyState) {
            this.readyState = readyState;
        }
        
        public float getXArrivalTime() {
                return xArrivalTime;
        }

        public void setXArrivalTime(float xArrivalTime) {
                this.xArrivalTime = xArrivalTime;
        }
        
        public String toString() {
            return "Process " + name + ", AT: " + arrivalTime
                    + ", ERT: " + expectedRunTime + ", Priority: " + priority;
        }
}
