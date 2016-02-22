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
                //System.out.println(quantum);
            }
            else
            {			
                processQueue.add(processQueue.pop()); // move front process to end of queue
                executeProcess(processQueue.peek());
                /*System.out.println("Process: " + processQueue.peek().getName());
                System.out.println("	Starting Quantum: " + quantum);
                System.out.println(" 	Arrival Time    : " + processQueue.peek().getArrivalTime());
                System.out.println("	Estimated Time  : " + processQueue.peek().getRunTime());
                System.out.println("	Remaining Time before quantum: " + processQueue.peek().getRemainingRunTime());
                // move process to end of queue
                processQueue.add(processQueue.pop());
                
                currentProcess = processQueue.peek();
                timeChart.add(currentProcess);
                if (!currentProcess.getReadyState())
                {
                    currentProcess.setReadyState(true);
                    currentProcess.setExecutionStartTime(quantum);
                    totalWaitTime += (quantum - currentProcess.getArrivalTime());
                }

                currentProcess.setRemainingRunTime(currentProcess.getRemainingRunTime() - 1);
                //System.out.println("	Remaining Time after quantum : " + processQueue.peek().getRemainingRunTime());

                if(currentProcess.getRemainingRunTime() <= 0)
                {
                    totalTurnaroundTime += (quantum - currentProcess.getArrivalTime());
                    processQueue.pop();
                    //System.out.println("				Process: " + processQueue.peek().getName() + " complete...");
                    //quantum += processQueue.pop().getRemainingRunTime();
                }
                else
                {
                    processQueue.add(processQueue.pop());
                }
                quantum += 1;

                //System.out.println("	Ending Quantum  : " + quantum);*/

            }
        }
        
        // executing remaining processes
        while (!processQueue.isEmpty())
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
        process = processQueue.peek();
        timeChart.add(process);
        // check if process has been started before
        if (!process.getReadyState())
        {
            process.setReadyState(true);
            totalWaitTime += (quantum - process.getArrivalTime());
        }

        process.setRemainingRunTime(process.getRemainingRunTime() - 1);
        // check if process finished
        if(process.getRemainingRunTime() <= 0)
        {
            totalTurnaroundTime += (quantum - process.getArrivalTime());
            processQueue.pop();
        }
        
        quantum += 1;    
    }
}
