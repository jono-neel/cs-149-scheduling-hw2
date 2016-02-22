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
        ShortestRemainingTime SRTTest = new ShortestRemainingTime(generateProcessQueue());
        RoundRobin RRTest = new RoundRobin(generateProcessQueue());
        HighestPriorityFirstPE HPFPETest = new HighestPriorityFirstPE(generateProcessQueue());
        //HighestPriorityFirstNPE HPFNPETest = new HighestPriorityFirstNPE(generateProcessQueue());
        
        // run scheduling algorithms
        FCFSTest.run();
        SJFTest.run();
        SRTTest.run();
        RRTest.run();
        HPFPETest.run();
        //HPFNPETest.run();
        
        // print results
        System.out.println("+==========Start First Come First Serve Algorithm Test Run 1==========+");
        FCFSTest.printRun();
        System.out.println("+======================End FCFS Test Run 1==========================+\n");
        System.out.println("+============Start Shortest Job First Algorithm Test Run 1============+");
        SJFTest.printRun();
        System.out.println("+======================End SJF Test Run 1===========================+\n");
        System.out.println("+=========Start Shortest Remaining Time Algorithm Test Run 1==========+");
        SRTTest.printRun();
        System.out.println("+======================End SRT Test Run 1===========================+\n");
        System.out.println("+===============Start Round Robin Algorithm Test Run 1================+");
        RRTest.printRun();
        System.out.println("+==================End Round Robin Test Run 1=======================+\n");
        System.out.println("+============Start Highest Priority First (PE) Test Run 1=============+");
        HPFPETest.printRun();
        System.out.println("+==============End Highest Priority First (PE) Test Run 1=============+");
        // HPFNPETest.printRun();

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
                    (rando.nextInt(MAX_PRIORITY)  + 1), 
                    Integer.toString(i));
            listByArrivals.add(p);
        }
        //Collections.sort(listByArrivals, new PriorityComparator());
        //Collections.sort(listByArrivals, new RunTimeComparator());
        Collections.sort(listByArrivals, new ArrivalComparator()); //put the list in order of arrival times
        for(ProcessSim p : listByArrivals)
        {
                //System.out.println(p);
                processQueue.add(p);
        }
        //System.out.println();
        return processQueue;
    }
}
