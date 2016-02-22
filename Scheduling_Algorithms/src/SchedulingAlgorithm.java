
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
    protected ArrayDeque<ProcessSim> processQueue;
    protected ArrayDeque<ProcessSim> processList;
    protected ArrayList<ProcessSim> timeChart;
    
    /**
     * Creates a scheduling algorithm with list of simulated processes.
     * @param list list of simulated processes
     */
    public SchedulingAlgorithm(ArrayDeque<ProcessSim> list)
    {
        processQueue = list;
        processList = list.clone();
        timeChart = new ArrayList<>();
    }
    
    /**
     * Runs scheduler.
     */
    public abstract void run();
    
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
        for (ProcessSim ps : processList)
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
                + totalTurnaroundTime / processList.size());
        System.out.println("Average Wait Time: "
                + totalWaitTime / processList.size());
        System.out.println("Average Response Time: "
                + totalResponseTime / processList.size());
        System.out.println("Throuhput: "
                + quantum / processList.size());
    }
}
