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
    	float[] averagedMetrics = new float[4];
    	for(int i= 0; i < 5; i++){
    		FirstComeFirstServe FCFSTest = new FirstComeFirstServe(generateProcessQueue(i));
    		FCFSTest.run();
    		System.out.println("+==========Start First Come First Serve Algorithm Test Run " + (i + 1) + "==========+");
    		FCFSTest.printRun();
    		System.out.println("***********End FCFS Test Run " + (i + 1) + "***************************************\n");
    		averagedMetrics[0] += FCFSTest.averageTurnaroundTime;
    		averagedMetrics[1] += FCFSTest.averageResponseTime;
    		averagedMetrics[2] += FCFSTest.averageWaitTime;
    		averagedMetrics[3] += FCFSTest.throughput;
    	}
    	printAndResetAveragedMetrics(averagedMetrics);
    	for(int i= 0; i < 5; i++){
    		ShortestJobFirst SJFTest = new ShortestJobFirst(generateProcessQueue(i));
    		SJFTest.run();
    		System.out.println("+==========Start Shortest Job First Algorithm Test Run " + (i + 1) + "==============+");
            SJFTest.printRun();
            System.out.println("***********End SJF Test Run " + (i + 1) + "****************************************\n");
            averagedMetrics[0] += SJFTest.averageTurnaroundTime;
    		averagedMetrics[1] += SJFTest.averageResponseTime;
    		averagedMetrics[2] += SJFTest.averageWaitTime;
    		averagedMetrics[3] += SJFTest.throughput;
    	}
    	printAndResetAveragedMetrics(averagedMetrics);
    	for(int i = 0; i < 5; i++){
    		ShortestRemainingTime SRTTest = new ShortestRemainingTime(generateProcessQueue(i));
    		SRTTest.run();
    		System.out.println("+==========Start Shortest Remaining Time Algorithm Test Run " + (i + 1) + "=========+");
    		SRTTest.printRun();
    		System.out.println("***********End SRT Test Run " + (i + 1) + "****************************************\n");
    		averagedMetrics[0] += SRTTest.averageTurnaroundTime;
    		averagedMetrics[1] += SRTTest.averageResponseTime;
    		averagedMetrics[2] += SRTTest.averageWaitTime;
    		averagedMetrics[3] += SRTTest.throughput;
    	}
    	printAndResetAveragedMetrics(averagedMetrics);
    	for(int i= 0; i < 5; i++){
            RoundRobin RRTest = new RoundRobin(generateProcessQueue(i));
            RRTest.run();
            System.out.println("+==========Start Round Robin Algorithm Test Run " + (i + 1) + "=====================+");
            RRTest.printRun();
            System.out.println("***********End Round Robin Test Run " + (i + 1) + "********************************\n");
    		averagedMetrics[0] += RRTest.averageTurnaroundTime;
    		averagedMetrics[1] += RRTest.averageResponseTime;
    		averagedMetrics[2] += RRTest.averageWaitTime;
    		averagedMetrics[3] += RRTest.throughput;
    	}
    	printAndResetAveragedMetrics(averagedMetrics);
    	for(int i= 0; i < 5; i++){
            HighestPriorityFirstPE HPFPETest = new HighestPriorityFirstPE(generateProcessQueue(i));
            HPFPETest.run();
            System.out.println("+==========Start Highest Priority First (PE) Test Run " + (i + 1) + "===============+");
            HPFPETest.printRun();
            System.out.println("***********End Highest Priority First (PE) Test Run " + (i + 1) + "****************\n");
    		averagedMetrics[0] += HPFPETest.averageTurnaroundTime;
    		averagedMetrics[1] += HPFPETest.averageResponseTime;
    		averagedMetrics[2] += HPFPETest.averageWaitTime;
    		averagedMetrics[3] += HPFPETest.throughput;
    	}
    	printAndResetAveragedMetrics(averagedMetrics);
    	for(int i= 0; i < 5; i++){
        HighestPriorityFirstNPE HPFNPETest = new HighestPriorityFirstNPE(generateProcessQueue(i));
        HPFNPETest.run();
    	System.out.println("+==========Start Highest Priority First (NPE) Test Run " + (i + 1) + "==============+");
        HPFNPETest.printRun();
    	System.out.println("***********End Highest Priority First (NPE) Test Run " + (i + 1) + "*****************");
    	averagedMetrics[0] += HPFNPETest.averageTurnaroundTime;
		averagedMetrics[1] += HPFNPETest.averageResponseTime;
		averagedMetrics[2] += HPFNPETest.averageWaitTime;
		averagedMetrics[3] += HPFNPETest.throughput;
    	}
    	printAndResetAveragedMetrics(averagedMetrics);

        // extra credit, uncomment to run
//        for(int i= 0; i < 5; i++){
//            AgingHighestPriorityFirstPE AHPFPETest = new AgingHighestPriorityFirstPE(generateProcessQueue(i));
//            AHPFPETest.run();
//            System.out.println("+==========Start Aging Highest Priority First (PE) Test Run " + (i + 1) + "==========+");
//            AHPFPETest.printRun();
//            System.out.println("***********End Aging Highest Priority First (PE) Test Run " + (i + 1) + "***********\n");
//    		averagedMetrics[0] += AHPFPETest.averageTurnaroundTime;
//    		averagedMetrics[1] += AHPFPETest.averageResponseTime;
//    		averagedMetrics[2] += AHPFPETest.averageWaitTime;
//    		averagedMetrics[3] += AHPFPETest.throughput;
//    	}
//    	printAndResetAveragedMetrics(averagedMetrics);
//        for(int i= 0; i < 5; i++){
//        AgingHighestPriorityFirstNPE AHPFNPETest = new AgingHighestPriorityFirstNPE(generateProcessQueue(i));
//        AHPFNPETest.run();
//    	System.out.println("+==========Start Aging Highest Priority First (NPE) Test Run " + (i + 1) + "========+");
//        AHPFNPETest.printRun();
//    	System.out.println("***********End Aging Highest Priority First (NPE) Test Run " + (i + 1) + "***********");
//    	averagedMetrics[0] += AHPFNPETest.averageTurnaroundTime;
//		averagedMetrics[1] += AHPFNPETest.averageResponseTime;
//		averagedMetrics[2] += AHPFNPETest.averageWaitTime;
//		averagedMetrics[3] += AHPFNPETest.throughput;
//    	}
//    	printAndResetAveragedMetrics(averagedMetrics);
    }
    
    /**
     * Prints total average and resets array.
     * @param averagedMetrics array of metrics
     */
    public static void printAndResetAveragedMetrics(float[] averagedMetrics)
    {
    	for(int i = 0; i < 4; i++)
    	{
    		averagedMetrics[i] = averagedMetrics[i] / 5;
    	}
    	System.out.println("|||||||||||||||| Average of 5 runs |||||||||||||||\nAverage Turnaround Time: " 
    			+ averagedMetrics[0] +"\nAverage Wait Time: " 
    			+ averagedMetrics[2] +"\nAverage Response Time: " 
    			+ averagedMetrics[1] +"\nAverage Throughput: " 
    			+ averagedMetrics[3] + "\n||||||||||||||||||||||||||||||||||||||||||||||||||\n");
    	for(int i = 0; i < 4; i++)
    	{
    		averagedMetrics[i] = 0;
    	}
    }
    
    /**
     * Generates processes with random values.
     * Currently uses same seed for debugging purposes.
     * @return process queue sorted by arrival time
     */
    public static ArrayDeque<ProcessSim> generateProcessQueue(int runNum)
    {
        ArrayList<ProcessSim> listByArrivals = new ArrayList<>();
        ArrayDeque<ProcessSim> processQueue = new ArrayDeque<>();
        
        Random rando = new Random(runNum + 4);
        for(int i = 0; i < NUM_OF_PROCESSES; i++) //populate the list with randomly valued proceses
        {
            ProcessSim p = new ProcessSim(
                    rando.nextFloat() * MAX_ARRIVAL_TIME,
                    rando.nextFloat() * MAX_RUN_TIME, 
                    (rando.nextInt(MAX_PRIORITY)  + 1), 
                    Integer.toString(i));
            listByArrivals.add(p);
        }
        Collections.sort(listByArrivals, new ArrivalComparator()); //put the list in order of arrival times
        for(ProcessSim p : listByArrivals)
        {
                processQueue.add(p);
        }
        return processQueue;
    }
}
