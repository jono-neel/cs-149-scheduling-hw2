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
    private static final int NUM_OF_PROCESSES = 50;
    private static final int MAX_ARRIVAL_TIME = 99;
    private static final int MAX_RUN_TIME = 10;
    private static final int MAX_PRIORITY = 4;
    
    public static void main(String[] args)
    {        
        // instantiate scheduling algorithms
        FirstComeFirstServe FCFSTest = new FirstComeFirstServe(generateProcessQueue());
        ShortestJobFirst SJFTest = new ShortestJobFirst(generateProcessQueue());
        //ShortestRemainingTime SRTTest = new ShortestRemainingTime(generateProcessQueue());
        RoundRobin RRTest = new RoundRobin(generateProcessQueue());
        // HighestPriorityFirst HPFTest = new HighestPriorityFirst(generateProcessQueue());
        
        // run scheduling algorithms
        FCFSTest.run();
        SJFTest.run();
        //SRTTest.run()
        RRTest.run();
        //HPFTest.run
        
        // print results
        FCFSTest.printRun();
        SJFTest.printRun()
        //SJFTest.printRun();
        RRTest.printRun();
        //HPFTest.printRun();
    }
    
    /**
     * Generates processes with random values.
     * Currently uses same seed for debugging purposes.
     * @return process queue sorted by arrival time
     */
    public static ArrayDeque<ProcessSim> generateProcessQueue()
    {
        ArrayList<ProcessSim> listByArrivals = new ArrayList<>();
        ArrayDeque<ProcessSim> processQueue = new ArrayDeque<>();
        
        Random rando = new Random(10);
        for(int i = 0; i < NUM_OF_PROCESSES; i++) //populate the list with randomly valued proceses
        {
            ProcessSim p = new ProcessSim(
                    rando.nextFloat() * MAX_ARRIVAL_TIME,
                    rando.nextFloat() * MAX_RUN_TIME, 
                    (rando.nextInt(MAX_PRIORITY)  + 1), "Process " + i);
            listByArrivals.add(p);
        }
        //Collections.sort(listByArrivals, new PriorityComparator());
        //Collections.sort(listByArrivals, new RunTimeComparator());
        Collections.sort(listByArrivals, new ArrivalComparator()); //put the list in order of arrival times
        for(ProcessSim p : listByArrivals)
        {
                System.out.println(p.getName());
                System.out.println("Arrival time: " + p.getArrivalTime());
                System.out.println("Runtime: " + p.getRunTime());
                System.out.println("Priority: " + p.getPriority() + "\n");
                processQueue.add(p);
        }
        return processQueue;
    }
}
