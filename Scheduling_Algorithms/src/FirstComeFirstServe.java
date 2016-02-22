import java.util.ArrayDeque;

/**
 * First Come First Serve algorithm simply runs processes to completion as they reach the CPU
 * test comment
 *
 * @author Jonathan Neel
 *
 */
public class FirstComeFirstServe extends SchedulingAlgorithm{
	public FirstComeFirstServe(ArrayDeque<ProcessSim> list)
	{
            super(list);
	}
	@Override
	public void run() {
		ArrayDeque<ProcessSim> processQueue = new ArrayDeque<ProcessSim>();
		int throughput = 0;
		while(quantum < 100) //stop after 99 quantum
		{
			
			while(!processList.isEmpty() && quantum > processList.peek().getArrivalTime()) 
				//when the scheduler reaches the next arrival time of the processes
			{
				processQueue.add(processList.pop()); //put the next process into the queue
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
