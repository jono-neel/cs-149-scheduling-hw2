
import java.util.ArrayDeque;
import java.util.PriorityQueue;

/**
 * Shortest remaining time algorithm schedules by shortest remaining run time (preemptive).
 * @author Katherine Soohoo
 */
public class ShortestRemainingTime extends SchedulingAlgorithm
{
    private PriorityQueue<ProcessSim> processQueue;
    
    public ShortestRemainingTime(ArrayDeque<ProcessSim> list)
    {
        super(list);
        processQueue = new PriorityQueue<ProcessSim>(new RemainingRunTimeComparator());
    }
    
    public void run()
    {        
        while (quantum < MAX_TIME_SLICES)
        {
            // add new processes
            while (!processList.isEmpty() && processList.peek().getArrivalTime() <= quantum)
            {
                processQueue.add(processList.pop());
            }
            
            // execute current process until finished
            if(!processQueue.isEmpty())
            {
                executeProcess(processQueue.peek());
            }
            // CPU idle, no process in executing
            else
            {
                timeChart.add(new ProcessSim());
                quantum += 1;
            }
        }
        
        // execute remaining processes
        while (!processQueue.isEmpty() && quantum < 100)
        {
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
        boolean originalState = process.getReadyState();
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
            processQueue.poll();
        }
        else // readd to process queue with new remaining time to sort
        {
            processQueue.add(processQueue.poll());
        }
        quantum += 1;   
    }
}
