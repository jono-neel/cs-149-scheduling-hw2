import java.util.ArrayDeque;
import java.util.PriorityQueue;

/**
 * Highest priority first schedules by highest priority (non-preemptive).
 */
public class HighestPriorityFirstNPE extends SchedulingAlgorithm {

    private PriorityQueue<ProcessSim> processQueue1, processQueue2, processQueue3, processQueue4;

    public HighestPriorityFirstNPE(ArrayDeque<ProcessSim> list) 
    {
        super(list);
        processQueue1 = new PriorityQueue<>(new ArrivalComparator());
        processQueue2 = new PriorityQueue<>(new ArrivalComparator());
        processQueue3 = new PriorityQueue<>(new ArrivalComparator());
        processQueue4 = new PriorityQueue<>(new ArrivalComparator());
    }

    @Override
    public void run()
    {
        while (quantum < MAX_TIME_SLICES)
        {
            while (!processList.isEmpty() && processList.peek().getArrivalTime() <= quantum)
            {
                // add to proper queue based on priority
                if (processList.peek().getPriority() == 1) 
                {
                    processQueue1.add(processList.pop());
                }
                else if (processList.peek().getPriority() == 2)
                {
                    processQueue2.add(processList.pop());
                }
                else if (processList.peek().getPriority() == 3)
                {
                    processQueue3.add(processList.pop());
                }
                else // priority = 4
                { 
                    processQueue4.add(processList.pop());
                }
            }

            // cpu idle, all queues are empty
            if (processQueue1.isEmpty() && processQueue2.isEmpty() && processQueue3.isEmpty()
                    && processQueue4.isEmpty())
            {
                timeChart.add(new ProcessSim());
                quantum += 1;
            }
            // execute process until finished
            else
            {
                if (!processQueue1.isEmpty())
                {
                    executeProcess(processQueue1.poll());
                }
                else if (!processQueue2.isEmpty())
                {
                    executeProcess(processQueue2.poll());
                }
                else if (!processQueue3.isEmpty())
                {
                    executeProcess(processQueue3.poll());
                }
                else // first 3 queues are empty
                {
                    executeProcess(processQueue4.poll());
                }
            }
        }
        
        // execute remaining processes from all queues
        while (!processQueue1.isEmpty()  && quantum < 100)
        {
            executeProcess(processQueue1.poll());
        }
        while (!processQueue2.isEmpty() && quantum < 100)
        {
            executeProcess(processQueue2.poll());
        }
        while (!processQueue3.isEmpty() && quantum < 100)
        {
            executeProcess(processQueue3.poll());
        }
        while (!processQueue4.isEmpty() && quantum < 100)
        {
            executeProcess(processQueue4.poll());
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
        float enteredQuanta = quantum;
        System.out.println("Quantum: " + quantum  + " Arr time: " + process.getArrivalTime());
        // run process until finished
        while (process.getRemainingRunTime() > 0)
        {
            timeChart.add(process);
            process.setRemainingRunTime(process.getRemainingRunTime() - 1);
            quantum += 1;
            if(quantum > 99)
                break;
        }
        // add times after process finish executing
        float resTime = 0.f;    
        if (process.getRunTime() <= 1)
        {
            resTime = enteredQuanta - process.getArrivalTime() + process.getRunTime();
        }
        else
        {
            resTime = enteredQuanta - process.getArrivalTime() + 1;
        }
        totalResponseTime += resTime;
        process.setResponseTime(resTime);
        
        totalTurnaroundTime += (quantum - process.getArrivalTime());
        totalWaitTime += (process.getArrivedQuantum() - process.getArrivalTime());
        totalFinishedProcesses++;
    }
}
