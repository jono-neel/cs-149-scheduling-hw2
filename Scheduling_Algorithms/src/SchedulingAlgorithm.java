
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * An abstract class that all scheduling algorithms should inherit.
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
    protected ArrayDeque<ProcessSim> processListClone;
    protected ArrayList<ProcessSim> timeChart;
    
    /**
     * Creates a scheduling algorithm with list of simulated processes.
     * @param list list of simulated processes
     */
    public SchedulingAlgorithm(ArrayDeque<ProcessSim> list)
    {
        processList = list;
        processListClone = list.clone();
        timeChart = new ArrayList<>();
    }
    
    /**
     * Runs scheduler.
     */
    public abstract void run();
    
    /**
     * Updates time when process executes.
     * @param process process executing
     */
    protected void updateTimes(ProcessSim process)
    {
        totalTurnaroundTime += (quantum - process.getArrivalTime() + process.getRunTime());
        totalWaitTime += (quantum - process.getArrivalTime());   
    }
    
    /**
     * Prints algorithm run outputs.
     * Turnaround Time = wait time + run time
     * Wait Time = arrival time + time when execution begins
     * Response Time = ?
     * Throughput = number of jobs / total time
     */
    protected void printRun()
    {
        // processes created
        System.out.println("Processes Created:" );
        for (ProcessSim ps : processListClone)
        {
            System.out.println(ps);
        }
        
        // time chart
        System.out.print("Time Chart: [");
        for (int i = 0; i < timeChart.size(); i++)
        {
            System.out.print(timeChart.get(i).getName());
            if (i != timeChart.size() - 1)
            {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        
        // statistics
        System.out.println("Average Turnaround Time: "
                + totalTurnaroundTime / processListClone.size());
        System.out.println("Average Wait Time: "
                + totalWaitTime / processListClone.size());
        System.out.println("Average Response Time: "
                + totalResponseTime / processListClone.size());
        System.out.println("Throuhput: "
                + processListClone.size() / quantum);
    }
}
