
import java.util.ArrayDeque;
import java.util.PriorityQueue;

/**
 * Shortest job first algorithm schedules by shortest run time (non-preemptive).
 * @author Katherine Soohoo
 */
public class ShortestJobFirst extends SchedulingAlgorithm
{
    private PriorityQueue<ProcessSim> processQueue;
    
    public ShortestJobFirst(ArrayDeque<ProcessSim> list)
    {
        super(list);
        processQueue = new PriorityQueue<ProcessSim>(new RunTimeComparator());
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
                executeProcess(processQueue.poll());
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
            executeProcess(processQueue.poll());
        }
    }
    
    /**
     * Executes process and adds to times.
     * @param process process to execute
     */
    private void executeProcess(ProcessSim process)
    {
        process.setReadyState(true);
        process.setArrivedQuantum(quantum);
        // run process until finished
        while (process.getRemainingRunTime() > 0)
        {
            timeChart.add(process);
            process.setRemainingRunTime(process.getRemainingRunTime() - 1);
            quantum += 1;
        }
        // add times after process finish executing
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
    }
}
