
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
        // run for 100 time slices
        while (quantum < 100)
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
        boolean originalState = process.getReadyState();
        process.setReadyState(true);
        totalWaitTime += (quantum - process.getArrivalTime()); 
        // run process until finished
        while (process.getRemainingRunTime() > 0)
        {
            timeChart.add(process);
            process.setRemainingRunTime(process.getRemainingRunTime() - 1);
            quantum += 1;
            // first time executing and finishes in 1 quantum
            if(!originalState && process.getRemainingRunTime() <= 0)
            {
               totalResponseTime += process.getRunTime();
               originalState = true;
            }
           // needs more than 1 quantum to finish
            else if (!originalState && process.getRemainingRunTime() > 0)
            {
                totalResponseTime += 1;
                originalState = true;
            }
        }
        totalTurnaroundTime += (quantum - process.getArrivalTime());
        totalFinishedProcesses++;
    }
}
