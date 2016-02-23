import java.util.ArrayDeque;

/**
 * Round robin schedules first come first serve with process switching (preemptive)
 * @author Matthew Montero
 */
public class RoundRobin extends SchedulingAlgorithm
{
    ArrayDeque<ProcessSim> processQueue = new ArrayDeque<ProcessSim>();

    public RoundRobin(ArrayDeque<ProcessSim> pl)
    {
        super(pl);
    }
    /**
     * In run, first check if the first item in the processList has arrived yet. If so, remove from processList
     * 	and add to processQueue. If processQueue is empty, add 1 to quantum, if not, execute process for 1.0 quantum.
     */
    public void run()
    {
        while(quantum < MAX_TIME_SLICES)
        {
            while(!processList.isEmpty() && processList.peek().getArrivalTime() <= quantum)
            {
                processQueue.add(processList.pop());
            }
            // cpu idle
            if(processQueue.isEmpty())
            {
                timeChart.add(new ProcessSim());
                quantum += 1;
            }
            // execute process
            else
            {			
                processQueue.add(processQueue.pop()); // move front process to end of queue
                executeProcess(processQueue.peek());
            }
        }
        
        // executing remaining processes
        while (!processQueue.isEmpty() && quantum < 100)
        {
            processQueue.add(processQueue.pop());
            executeProcess(processQueue.peek());
        }
    }
    
    /**
     * Executes process and adds to times.
     * @param process process to execute
     */
    private void executeProcess(ProcessSim process)
    {
        timeChart.add(process);
        // set arrived quantum for first visit
        if (!process.getReadyState())
        {
            process.setReadyState(true);
            process.setArrivedQuantum(quantum);
        }
        // subtract from remaining run time
        process.setRemainingRunTime(process.getRemainingRunTime() - 1);
        // check if process finished
        if(process.getRemainingRunTime() <= 0)
        {
            // add times
            if (process.getRunTime() <= 1)
            {
                totalResponseTime += process.getRunTime();
            }
            else if (process.getRunTime() > 1)
            {
                totalResponseTime += 1;
            }
            totalTurnaroundTime += (quantum - process.getArrivalTime());
            totalWaitTime += (process.getArrivedQuantum() - process.getArrivalTime());
            totalFinishedProcesses++;
            // remove from process queue
            processQueue.pop();
        }
        quantum += 1; 
    }
}
