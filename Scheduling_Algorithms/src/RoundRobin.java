import java.util.ArrayDeque;


public class RoundRobin 
{
	ArrayDeque<ProcessSim> inProgress = new ArrayDeque<ProcessSim>();
	ArrayDeque<ProcessSim> processList = new ArrayDeque<ProcessSim>();
	float quantum = 0;
	public RoundRobin(ArrayDeque<ProcessSim> pl)
	{
		processList = pl;
	}
	/**
	 * In run, first check if the first item in the processList has arrived yet. If so, remove from processList
	 * 	and add to inProgress. If inProgress is empty, add .01 to quantum, if not, execute process for 1.0 quantum.
	 */
	public void run()
	{
		while(quantum < 100)
		{
			//System.out.println("processList.peek().getArrivalTime : " + processList.peek().getArrivalTime());
			if(!processList.isEmpty() && processList.peek().getArrivalTime() <= quantum)
			{
				inProgress.add(processList.pop());
			}
			if(inProgress.isEmpty())
			{
				quantum+=0.01;
				//System.out.println(quantum);
			}
			else
			{				
				System.out.println("Process: " + inProgress.peek().getName());
				System.out.println("	Starting Quantum: " + quantum);
				System.out.println(" 	Arrival Time    : " + inProgress.peek().getArrivalTime());
				System.out.println("	Estimated Time  : " + inProgress.peek().getRunTime());
				System.out.println("	Remaining Time before quantum: " + inProgress.peek().getRemainingRunTime());
				
				inProgress.peek().setRemainingRunTime(inProgress.peek().getRemainingRunTime() - 1);
				
				System.out.println("	Remaining Time after quantum : " + inProgress.peek().getRemainingRunTime());
				
				if(inProgress.peek().getRemainingRunTime() <= 0)
				{
					System.out.println("				Process: " + inProgress.peek().getName() + " complete...");
					quantum += inProgress.pop().getRemainingRunTime();
				}
				else
				{
					inProgress.add(inProgress.pop());
				}
				quantum += 1;
				
				System.out.println("	Ending Quantum  : " + quantum);
				
			}
		}
	}
}
