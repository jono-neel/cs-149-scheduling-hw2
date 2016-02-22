import java.util.ArrayDeque;
import java.util.PriorityQueue;

/**
 * Highest priority first (preemptive)
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
        while (quantum < 100)
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
                    executeProcess(processQueue1.peek(), 1);
                }
                else if (!processQueue2.isEmpty())
                {
                    currentProcess = processQueue2.poll();
                    currentProcess.setXArrivalTime(quantum);
                    processQueue2.add(currentProcess);
                    executeProcess(processQueue2.peek(), 2);
                }
                else if (!processQueue3.isEmpty())
                {
                    currentProcess = processQueue3.poll();
                    currentProcess.setXArrivalTime(quantum);
                    processQueue3.add(currentProcess);
                    executeProcess(processQueue3.peek(), 3);
                }
                else // first 3 queues are empty
                {
                    currentProcess = processQueue4.poll();
                    currentProcess.setXArrivalTime(quantum);
                    processQueue4.add(currentProcess);
                    executeProcess(processQueue4.peek(), 4);
                }
            }
        }
        
        // execute remaining processes from all queues
        while (!processQueue1.isEmpty())
        {
            currentProcess = processQueue1.poll();
            currentProcess.setXArrivalTime(quantum);
            processQueue1.add(currentProcess);
            executeProcess(processQueue1.peek(), 1);
        }
        while (!processQueue2.isEmpty())
        {
            currentProcess = processQueue2.poll();
            currentProcess.setXArrivalTime(quantum);
            processQueue2.add(currentProcess);
            executeProcess(processQueue2.peek(), 2);
        }
        while (!processQueue3.isEmpty())
        {
            currentProcess = processQueue3.poll();
            currentProcess.setXArrivalTime(quantum);
            processQueue3.add(currentProcess);
            executeProcess(processQueue3.peek(), 3);
        }
        while (!processQueue4.isEmpty())
        {
            currentProcess = processQueue4.poll();
            currentProcess.setXArrivalTime(quantum);
            processQueue4.add(currentProcess);
            executeProcess(processQueue4.peek(), 4);
        }
    }

    private void executeProcess(ProcessSim process, int queueNum)
    {
        timeChart.add(process);
        // check if process has been started before
        boolean originalState = process.getReadyState();
        if (!process.getReadyState())
        {
            process.setReadyState(true);
            totalWaitTime += (quantum - process.getArrivalTime());
        }

        process.setRemainingRunTime(process.getRemainingRunTime() - 1);
        
        // first time executing and finishes in 1 quantum
        if(!originalState && process.getRemainingRunTime() <= 0)
        {
            totalResponseTime += (quantum - process.getArrivalTime());
        }
        // needs more than 1 quantum to finish
        else
        {
            totalResponseTime += (quantum + 1 - process.getArrivalTime());
        }
            
        // check if process finished
        if (process.getRemainingRunTime() <= 0)
        {
            totalTurnaroundTime += (quantum - process.getArrivalTime());
            // remove from process queue
            if (queueNum == 1)
            {
                processQueue1.poll();
            }
            else if (queueNum == 2)
            {
                processQueue2.poll();
            }
            else if (queueNum == 3)
            {
                processQueue3.poll();
            }
            else // queueNum == 4
            {
                processQueue4.poll();
            }
        }

        quantum += 1;
    }
}
