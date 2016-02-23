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
        while(quantum < 100.0) //stop after 99 quantum
        {			
            while(!this.processList.isEmpty() && quantum > this.processList.peek().getArrivalTime()) 
                //when the scheduler reaches the next arrival time of the processes
            {
                processQueue.add(this.processList.pop()); //put the next process into the queue
            }

            if(!processQueue.isEmpty()) //run the processes that have reached the cpu
            {
                executeProcess(processQueue.remove());                
            }
            else
            {
                timeChart.add(new ProcessSim());
                quantum += 1;
            }

        }
        
        // execute remaining processes
        while(!processQueue.isEmpty() && quantum < 100.0)
        {
            executeProcess(processQueue.remove());
        }
    }
    
    /**
     * Executes process and adds to times.
     * @param process process to execute
     */
    private void executeProcess(ProcessSim process)
    {
        process.setReadyState(true);
        totalWaitTime += (quantum - process.getArrivalTime()); 
        // run process until finished
        while (process.getRemainingRunTime() > 0)
        {
            timeChart.add(process);
            process.setRemainingRunTime(process.getRemainingRunTime() - 1);
            quantum += 1;
        }
        totalFinishedProcesses++;
        totalTurnaroundTime += (quantum - process.getArrivalTime());
        totalResponseTime += (quantum - process.getArrivalTime());
    }    
}
