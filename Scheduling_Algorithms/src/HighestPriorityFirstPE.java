import java.util.ArrayDeque;
import java.util.PriorityQueue;

/**
 * Highest priority first schedules by highest priority (preemptive).
 */
public class HighestPriorityFirstPE extends SchedulingAlgorithm {

    private PriorityQueue<ProcessSim> processQueue1, processQueue2, processQueue3, processQueue4;

    public HighestPriorityFirstPE(ArrayDeque<ProcessSim> list) 
    {
            super(list);
            processQueue1 = new PriorityQueue<>(new XArrivalComparator());
            processQueue2 = new PriorityQueue<>(new XArrivalComparator());
            processQueue3 = new PriorityQueue<>(new XArrivalComparator());
            processQueue4 = new PriorityQueue<>(new XArrivalComparator());
    }

    @Override
    public void run()
    {
        ProcessSim currentProcess;
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
            // execute process
            else
            {
                if (!processQueue1.isEmpty())
                {
                    currentProcess = processQueue1.poll();
                    currentProcess.setXArrivalTime(quantum);
                    processQueue1.add(currentProcess);
                    executeProcess(processQueue1.peek());
                }
                else if (!processQueue2.isEmpty())
                {
                    currentProcess = processQueue2.poll();
                    currentProcess.setXArrivalTime(quantum);
                    processQueue2.add(currentProcess);
                    executeProcess(processQueue2.peek());
                }
                else if (!processQueue3.isEmpty())
                {
                    currentProcess = processQueue3.poll();
                    currentProcess.setXArrivalTime(quantum);
                    processQueue3.add(currentProcess);
                    executeProcess(processQueue3.peek());
                }
                else // first 3 queues are empty
                {
                    currentProcess = processQueue4.poll();
                    currentProcess.setXArrivalTime(quantum);
                    processQueue4.add(currentProcess);
                    executeProcess(processQueue4.peek());
                }
            }
        }
        
        // execute remaining processes from all queues
        while (!processQueue1.isEmpty() && quantum < 100)
        {
            currentProcess = processQueue1.poll();
            currentProcess.setXArrivalTime(quantum);
            processQueue1.add(currentProcess);
            executeProcess(processQueue1.peek());
        }
        while (!processQueue2.isEmpty() && quantum < 100)
        {
            currentProcess = processQueue2.poll();
            currentProcess.setXArrivalTime(quantum);
            processQueue2.add(currentProcess);
            executeProcess(processQueue2.peek());
        }
        while (!processQueue3.isEmpty() && quantum < 100)
        {
            currentProcess = processQueue3.poll();
            currentProcess.setXArrivalTime(quantum);
            processQueue3.add(currentProcess);
            executeProcess(processQueue3.peek());
        }
        while (!processQueue4.isEmpty() && quantum < 100)
        {
            currentProcess = processQueue4.poll();
            currentProcess.setXArrivalTime(quantum);
            processQueue4.add(currentProcess);
            executeProcess(processQueue4.peek());
        }
    }
    
    /**
     * Executes process and adds to times.
     * @param process process to execute
     */
    private void executeProcess(ProcessSim process)
    {
        timeChart.add(process);
        // set arrived quantum for first visit
        if (!process.getReadyState())
        {
            process.setReadyState(true);
            process.setArrivedQuantum(quantum);
        }
        // subtract from remaining run time
        process.setRemainingRunTime(process.getRemainingRunTime() - 1);
        // check if process finished
        if(process.getRemainingRunTime() <= 0)
        {
            // add times
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
           // remove from process queue
            if (process.getPriority() == 1)
            {
                processQueue1.poll();
            }
            else if (process.getPriority() == 2)
            {
                processQueue2.poll();
            }
            else if (process.getPriority() == 3)
            {
                processQueue3.poll();
            }
            else // priority is 4
            {
                processQueue4.poll();
            }
        }
        quantum += 1;
    }
}
