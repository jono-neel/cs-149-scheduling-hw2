import java.util.ArrayDeque;

public class RoundRobin extends SchedulingAlgorithm
{
    ArrayDeque<ProcessSim> processQueue = new ArrayDeque<ProcessSim>();

    public RoundRobin(ArrayDeque<ProcessSim> pl)
    {
        super(pl);
    }
    /**
     * In run, first check if the first item in the processList has arrived yet. If so, remove from processList
     * 	and add to processQueue. If processQueue is empty, add 1 to quantum, if not, execute process for 1.0 quantum.
     */
    public void run()
    {
        //ProcessSim currentProcess;
        while(quantum < 100)
        {
            //System.out.println("processList.peek().getArrivalTime : " + processList.peek().getArrivalTime());
            if(!processList.isEmpty() && processList.peek().getArrivalTime() <= quantum)
            {
                processQueue.add(processList.pop());

            }
            if(processQueue.isEmpty())
            {
                timeChart.add(new ProcessSim());
                quantum += 1;
            }
            else
            {			
                processQueue.add(processQueue.pop()); // move front process to end of queue
                executeProcess(processQueue.peek());
                
            }
        }
        
        // executing remaining processes
        while (!processQueue.isEmpty() && quantum < 100)
        {
            processQueue.add(processQueue.pop());
            executeProcess(processQueue.peek());
        }
    }
    
    /**
     * Executes process and adds to times.
     * @param process process to execute
     */
    private void executeProcess(ProcessSim process)
    {
        timeChart.add(process);
        // check if process has been started before
        boolean originalState = process.getReadyState();
        if (!process.getReadyState())
        {
            process.setReadyState(true);
            totalWaitTime += (quantum - process.getArrivalTime());
            totalResponseTime += (quantum + 1 - process.getArrivalTime());
        }

        process.setRemainingRunTime(process.getRemainingRunTime() - 1);
        
                // first time executing and finishes in 1 quantum
        if(!originalState && process.getRemainingRunTime() <= 0)
        {
            totalResponseTime += (quantum - process.getArrivalTime());
        }
        // needs more than 1 quantum to finish
        else if(process.getRemainingRunTime() > 0 && !originalState)
        {
            totalResponseTime += (quantum + 1 - process.getArrivalTime());
        }
        
        // check if process finished
        if(process.getRemainingRunTime() <= 0)
        {
            totalTurnaroundTime += (quantum - process.getArrivalTime());
            processQueue.pop();
            totalFinishedProcesses++;
        }
        
        quantum += 1;
        
        }
}
