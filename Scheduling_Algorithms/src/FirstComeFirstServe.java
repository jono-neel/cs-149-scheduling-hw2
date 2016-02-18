import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * First Come First Serve algorithm simply runs processes to completion as they reach the CPU
 * test comment
 *
 * @author Jonathan Neel
 *
 */
public class FirstComeFirstServe implements SchedulingAlgorithm{
	private float quantum;
	public FirstComeFirstServe()
	{
		quantum = 0;
	}
	@Override
	public void run(ArrayList<ProcessSim> list) {
		int processToAdd = 0;
		ArrayDeque<ProcessSim> processQueue = new ArrayDeque<ProcessSim>();
		int throughput = 0;
		while(quantum < 100) //stop after 99 quantum
		{
			
			while(quantum > list.get(processToAdd).getArrivalTime()) 
				//when the scheduler reaches the next arrival time of the processes
			{
				processQueue.add(list.get(processToAdd)); //put the next process into the queue
				processToAdd++; //iterate to the next index in the ArrayList
			}
			if(processQueue.isEmpty()) //idle if there are no more processes entering the queue
			{
				quantum += .1;
			}
			else
			{
				while(!processQueue.isEmpty()) //run the processes that have reached the cpu
				{
					ProcessSim currentProcess = processQueue.remove();
					quantum += currentProcess.getRunTime();
					throughput++;
					System.out.println("Quantum: " + (quantum - currentProcess.getRunTime()) + " Process: " + currentProcess.getName());
					if(quantum > 99) break;
				}
			}
		}System.out.println("Throughput:" + throughput);
		
	}

}
