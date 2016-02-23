import java.util.ArrayDeque;

/**
 * First Come First Serve algorithm simply runs processes to completion as they reach the CPU
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
        while(quantum < MAX_TIME_SLICES)
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
        process.setArrivedQuantum(quantum);
        // run process until finished
        while (process.getRemainingRunTime() > 0)
        {
            timeChart.add(process);
            process.setRemainingRunTime(process.getRemainingRunTime() - 1);
            quantum += 1;
        }
        // add times after process finish executing
        if (process.getRunTime() <= 1)
        {
            totalResponseTime += process.getRunTime();
        }
        else if (process.getRunTime() > 1)
        {
            totalResponseTime += 1;
        }
        totalTurnaroundTime += (quantum - process.getArrivalTime());
        totalWaitTime += (process.getArrivedQuantum() - process.getArrivalTime());
        totalFinishedProcesses++;
    }    
}
