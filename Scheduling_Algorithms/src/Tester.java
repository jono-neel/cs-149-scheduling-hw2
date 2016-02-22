import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Tester for the scheduling algorithms.
 * @author Jonathan Neel
 *
 */
public class Tester {
    private static ArrayDeque<ProcessSim> processQueue = new ArrayDeque<ProcessSim>();
    private static final int NUM_OF_PROCESSES = 50;
    private static final int MAX_ARRIVAL_TIME = 99;
    private static final int MAX_RUN_TIME = 10;
    private static final int MAX_PRIORITY = 4;
    
    public static void main(String[] args)
    {
        initializeProcesses();
        
        // instantiate scheduling algorithms
        FirstComeFirstServe FCFSTest = new FirstComeFirstServe(processQueue.clone());
        ShortestJobFirst SJFTest = new ShortestJobFirst(processQueue.clone());
        //ShortestRemainingTime SRTTest = new ShortestRemainingTime(processQueue.clone());
        RoundRobin RRTest = new RoundRobin(processQueue.clone());
        // HighestPriorityFirst HPFTest = new HighestPriorityFirst(processQueue.clone());
        
        // run scheduling algorithms
        FCFSTest.run();
        SJFTest.run();
        //SRTTest.run()
        RRTest.run();
        //HPFTest.run
        
        // print results
        //FCFSTest.printRun();
        //SJFTest.printRun()
        //SJFTest.printRun();
        //RRTest.printRun();
        //HPFTest.printRun();
    }
    
    /**
     * Generates processes with random values.
     */
    public static void initializeProcesses()
    {
        ArrayList<ProcessSim> listByArrivals = new ArrayList<>();
        
        Random rando = new Random(10);
        for(int i = 0; i < NUM_OF_PROCESSES; i++) //populate the list with randomly valued proceses
        {
            ProcessSim p = new ProcessSim(
                    rando.nextFloat() * MAX_ARRIVAL_TIME,
                    rando.nextFloat() * MAX_RUN_TIME, 
                    (rando.nextInt(MAX_PRIORITY)  + 1), i);
            listByArrivals.add(p);
        }
        //Collections.sort(listByArrivals, new PriorityComparator());
        //Collections.sort(listByArrivals, new RunTimeComparator());
        Collections.sort(listByArrivals, new ArrivalComparator()); //put the list in order of arrival times
        for(ProcessSim p : listByArrivals)
        {
                System.out.println("Process " + p.getName());
                System.out.println("Arrival time: " + p.getArrivalTime());
                System.out.println("Runtime: " + p.getRunTime());
                System.out.println("Priority: " + p.getPriority() + "\n");
                processQueue.add(p);
        }
    }
}
