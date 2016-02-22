
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
        ProcessSim currentProcess;
        
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
        updateTimes(process);
        // run process until finished
        while (process.getRemainingRunTime() > 0)
        {
            process.setRemainingRunTime(process.getRemainingRunTime() - 1);
            quantum += 1;
            timeChart.add(process);
        }
        processQueue.poll();
    }
}
