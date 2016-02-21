
import java.util.ArrayDeque;
import java.util.PriorityQueue;

/**
 * Shortest job first algorithm schedules by shortest run time.
 * @author Katherine Soohoo
 */
public class ShortestJobFirst
{
    private float quantum;
    ArrayDeque<ProcessSim> processList;
    PriorityQueue<ProcessSim> inProgress;
    
    public ShortestJobFirst(ArrayDeque<ProcessSim> list)
    {
        quantum = 0;
        processList = list;
        inProgress = new PriorityQueue<ProcessSim>(new RunTimeComparator());
    }
    
    public void run()
    {
        ProcessSim currentProcess;
        
        while (quantum < 100)
        {
            // add new processes
            while (!processList.isEmpty() && processList.peek().getArrivalTime() <= quantum)
            {
                inProgress.add(processList.pop());
            }
            
            // execute current process
            if(!inProgress.isEmpty())
            {
                currentProcess = inProgress.peek();
                currentProcess.setRemainingRunTime(currentProcess.getRemainingRunTime() - 1);
                if (currentProcess.getRemainingRunTime() <= 0)
                {
                    System.out.print(inProgress.poll().getName() + " ");
                }
            }
            
            // next time slice
            quantum += 1;
        }
        
        // execute remaining processes
        System.out.print("\nAfter 100 quantum: ");
        while (!inProgress.isEmpty())
        {
            System.out.print(inProgress.poll().getName() + " ");
        }
    }
}
