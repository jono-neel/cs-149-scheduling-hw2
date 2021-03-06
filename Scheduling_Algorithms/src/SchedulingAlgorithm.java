
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
    protected int totalFinishedProcesses = 0;
    protected float averageTurnaroundTime = 0;
    protected float averageResponseTime = 0;
    protected float averageWaitTime = 0;
    protected float throughput = 0;
    protected ArrayDeque<ProcessSim> processList;
    protected ArrayDeque<ProcessSim> processListClone;
    protected ArrayList<ProcessSim> timeChart;
    protected static final int MAX_TIME_SLICES = 100;
    
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
     * Runs scheduler for set amount of time slices.
     */
    public abstract void run();
    
    /**
     * Prints algorithm run outputs.
     * Turnaround Time = execution end time - arrival time
     * Wait Time = execution begin time - arrival time
     * Response Time = first response - arrival time
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
        averageTurnaroundTime = totalTurnaroundTime / totalFinishedProcesses;
        averageResponseTime = totalResponseTime / totalFinishedProcesses;
        averageWaitTime = totalWaitTime / totalFinishedProcesses;
        throughput = totalFinishedProcesses / quantum;
        System.out.println("Quantums: " + timeChart.size());
        System.out.println("Processes Completed: " + totalFinishedProcesses);
        System.out.println("Average Turnaround Time: "
                + averageTurnaroundTime);
        System.out.println("Average Wait Time: "
                + averageWaitTime);
        System.out.println("Average Response Time: "
                + averageResponseTime);
        System.out.println("Throuhput: "
                + throughput);
        System.out.println();
    }
}
