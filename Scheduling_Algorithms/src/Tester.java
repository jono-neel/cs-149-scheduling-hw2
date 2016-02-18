import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * tester for the scheduling algorithms
 * @author Jonathan Neel
 *
 */
public class Tester {
	public static void main(String[] args)
	{
		ArrayList<ProcessSim> listByArrivals = new ArrayList<ProcessSim>();
		Random rando = new Random(10);
		for(int i = 0; i < 50; i++) //populate the list with randomly valued proceses
		{
			ProcessSim p = new ProcessSim(rando.nextFloat() * 99, rando.nextFloat() * 10, (rando.nextInt(4)  + 1), i);
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
	
		}
		FirstComeFirstServe FCFSTest = new FirstComeFirstServe();
		FCFSTest.run(listByArrivals); //run the FCFS algorithm
		
	}
}
