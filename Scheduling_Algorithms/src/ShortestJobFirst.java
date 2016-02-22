
import java.util.ArrayDeque;
import java.util.PriorityQueue;

/**
 * Shortest job first algorithm schedules by shortest run time (non-preemptive).
 * @author Katherine Soohoo
 */
public class ShortestJobFirst extends SchedulingAlgorithm
{
    private PriorityQueue<ProcessSim> inProgress;
    
    public ShortestJobFirst(ArrayDeque<ProcessSim> list)
    {
        super(list);
        inProgress = new PriorityQueue<ProcessSim>(new RunTimeComparator());
    }
    
    public void run()
    {
        ProcessSim currentProcess;
        
        System.out.print("During 100 quantum: ");
        while (quantum < 100)
        {
            // add new processes
            while (!processList.isEmpty() && processList.peek().getArrivalTime() <= quantum)
            {
                inProgress.add(processList.pop());
            }
            
            // execute current process until done
            if(!inProgress.isEmpty())
            {
                currentProcess = inProgress.peek();
                totalTurnaroundTime += (quantum - currentProcess.getArrivalTime() + currentProcess.getRunTime());
                totalWaitTime += (quantum - currentProcess.getArrivalTime());
                totalResponseTime += (quantum - currentProcess.getArrivalTime());
                while (currentProcess.getRemainingRunTime() > 0)
                {
                    currentProcess.setRemainingRunTime(currentProcess.getRemainingRunTime() - 1);
                    quantum += 1;
                }
                System.out.print("[Q:" + quantum + ", P:" + inProgress.poll().getName() + "] ");
            }
            // CPU idle, no process in progress
            else
            {
                quantum += 1;
            }
        }
        
        // execute remaining processes
        System.out.print("\nAfter 100 quantum: ");
        while (!inProgress.isEmpty())
        {
            currentProcess = inProgress.peek();
            totalTurnaroundTime += (quantum - currentProcess.getArrivalTime() + currentProcess.getRunTime());
            totalWaitTime += (quantum - currentProcess.getArrivalTime());
            while (currentProcess.getRemainingRunTime() > 0)
            {
                currentProcess.setRemainingRunTime(currentProcess.getRemainingRunTime() - 1);
                quantum += 1;
            }
            System.out.print("[Q:" + quantum + ", P:" + inProgress.poll().getName() + "] ");
        }
    }
}
