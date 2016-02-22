
import java.util.ArrayDeque;

/**
 * An abstract class that all scheduling algorithms should inherit. May need to add more
 * or may not be needed at all, I'm not sure.
 * @author Jonathan Neel, Katherine Soohoo
 *
 */
public abstract class SchedulingAlgorithm
{
    protected float quantum = 0;
    protected float totalTurnaroundTime = 0;
    protected float totalWaitTime = 0;
    protected float totalResponseTime = 0;
    protected ArrayDeque<ProcessSim> processList;
    protected int processListSize;
    
    /**
     * Creates a scheduling algorithm with list of simulated processes.
     * @param list list of simulated processes
     */
    public SchedulingAlgorithm(ArrayDeque<ProcessSim> list)
    {
        processList = list;
        processListSize = list.size();
    }
    
    /**
     * Runs scheduler.
     */
    public abstract void run();
    
    /**
     * Prints algorithm run statistics
     * Turnaround Time = wait time + run time
     * Wait Time = arrival time + time when execution begins
     * Response Time = ?
     * Throughput = number of jobs / total time
     */
    protected void printRun()
    {
        System.out.println("Average Turnaround Time: "
                + totalTurnaroundTime / processListSize);
        System.out.println("Average Wait Time: "
                + totalWaitTime / processListSize);
        System.out.println("Average Response Time: "
                + totalResponseTime / processListSize);
        System.out.println("Throuhput: "
                + quantum / processListSize);
    }
}
