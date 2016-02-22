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
            processQueue1 = new PriorityQueue<>(new ArrivalComparator());
            processQueue2 = new PriorityQueue<>(new ArrivalComparator());
            processQueue3 = new PriorityQueue<>(new ArrivalComparator());
            processQueue4 = new PriorityQueue<>(new ArrivalComparator());
    }

    @Override
    public void run()
    {
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
                    executeProcess(processQueue1.peek(), 1);
                }
                else if (!processQueue2.isEmpty())
                {
                    executeProcess(processQueue2.peek(), 2);
                }
                else if (!processQueue3.isEmpty())
                {
                    executeProcess(processQueue3.peek(), 3);
                }
                else // first 3 queues are empty
                {
                    executeProcess(processQueue4.peek(), 4);
                }
            }
        }
        
        // execute remaining processes from all queues
        while (!processQueue1.isEmpty())
        {
            executeProcess(processQueue1.peek(), 1);
        }
        while (!processQueue2.isEmpty())
        {
            executeProcess(processQueue2.peek(), 2);
        }
        while (!processQueue3.isEmpty())
        {
            executeProcess(processQueue3.peek(), 3);
        }
        while (!processQueue4.isEmpty())
        {
            executeProcess(processQueue4.peek(), 4);
        }
    }

    private void executeProcess(ProcessSim process, int queueNum)
    {
        timeChart.add(process);
        // check if process has been started before
        if (!process.getReadyState())
        {
            process.setReadyState(true);
            totalWaitTime += (quantum - process.getArrivalTime());
        }

        process.setRemainingRunTime(process.getRemainingRunTime() - 1);
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
