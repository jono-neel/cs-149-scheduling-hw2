import java.util.ArrayDeque;

/**
 * First Come First Serve algorithm simply runs processes to completion as they reach the CPU
 * test comment
 *
 * @author Jonathan Neel
 *
 */
public class FirstComeFirstServe extends SchedulingAlgorithm
{
    public FirstComeFirstServe(ArrayDeque<ProcessSim> list)
    {
        super(list);
    }
    
    @Override
    public void run()
    {
        ArrayDeque<ProcessSim> processQueue = new ArrayDeque<ProcessSim>();
        ProcessSim currentProcess;
        while(quantum < 100) //stop after 99 quantum
        {			
            while(!this.processList.isEmpty() && quantum > this.processList.peek().getArrivalTime()) 
                //when the scheduler reaches the next arrival time of the processes
            {
                processQueue.add(this.processList.pop()); //put the next process into the queue
            }

            if(!processQueue.isEmpty()) //run the processes that have reached the cpu
            {
                currentProcess = processQueue.remove();
                updateTimes(currentProcess);
                while(currentProcess.getRemainingRunTime() > 0)
                {
                    currentProcess.setRemainingRunTime(currentProcess.getRemainingRunTime() - 1);
                    quantum += 1;
                    timeChart.add(currentProcess);
                }
                //quantum += currentProcess.getRunTime();
                //throughput++;
                //System.out.println("Quantum: " + (quantum - currentProcess.getRunTime()) + " Process: " + currentProcess.getName());
                //if(quantum > 99) break;
            }
            else
            {
                quantum += 1;
                timeChart.add(new ProcessSim());
            }

        }
        
        // execute remaining processes
        while(!processQueue.isEmpty())
        {
            currentProcess = processQueue.remove();
            updateTimes(currentProcess);
            while(currentProcess.getRemainingRunTime() > 0)
            {
                currentProcess.setRemainingRunTime(currentProcess.getRemainingRunTime() - 1);
                quantum += 1;
                timeChart.add(currentProcess);
            }
        }
    }
}
