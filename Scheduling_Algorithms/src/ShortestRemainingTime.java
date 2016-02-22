
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
        // run for 100 time slices
        while (quantum < 100)
        {
            // add new processes
            while (!processList.isEmpty() && processList.peek().getArrivalTime() <= quantum)
            {
                processQueue.add(processList.pop());
            }
            
            // execute current process
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
        while (!processQueue.isEmpty())
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
        // check if process has been started before
        if (!process.getReadyState())
        {
            process.setReadyState(true);
            totalWaitTime += (quantum - process.getArrivalTime());
        }
        
        process.setRemainingRunTime(process.getRemainingRunTime() - 1);
        // check if process finished
        if(process.getRemainingRunTime() <= 0)
        {
            totalTurnaroundTime += (quantum - process.getArrivalTime());
            processQueue.poll();
        }
        else
        {
            // readd with new remaining time to sort
            processQueue.add(processQueue.poll());
        }
        
        quantum += 1;   
    }
}
