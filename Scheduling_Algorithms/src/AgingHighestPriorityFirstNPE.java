import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Aging highest priority first (non-preemptive)
 * @author Tin Che
 */
public class AgingHighestPriorityFirstNPE extends SchedulingAlgorithm {

    private int MAX_PRIORITY = 4;
    private PriorityQueue<ProcessSim> processQueue1, processQueue2, processQueue3, processQueue4;
    private ArrayList<PriorityQueue<ProcessSim>> queueList;

    public AgingHighestPriorityFirstNPE(ArrayDeque<ProcessSim> list) 
    {
            super(list);
            processQueue1 = new PriorityQueue<>(new XArrivalComparator());
            processQueue2 = new PriorityQueue<>(new XArrivalComparator());
            processQueue3 = new PriorityQueue<>(new XArrivalComparator());
            processQueue4 = new PriorityQueue<>(new XArrivalComparator());
            queueList = new ArrayList<>();
            queueList.add(processQueue1);
            queueList.add(processQueue2);
            queueList.add(processQueue3);
            queueList.add(processQueue4); 
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
                increasePriority();
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
            increaseWaitedQuantum();
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
    
    private void increasePriority()
    {
        //System.out.println("START INC PRI");
        //Increase priority if a process waited for 5 quantum.
        for(int i = 0; i < 0; i++){
            for(ProcessSim ps : queueList.get(i + 1))
            {
                if(ps.getWaitedQuantum() >= 5){
                    ps.increasePriority();
                    queueList.get(i + 1).remove(ps);
                    queueList.get(i).add(ps);
                }
            }
        }
    }
    
    private void increaseWaitedQuantum()
    {
        //System.out.println("START INC WAITEDQUANTUM");
        for(int i = 0; i < 0; i++){
            for(ProcessSim ps : queueList.get(i + 1))
            {
                ps.increaseWaitedQuantum();
            }
        }
    
    }
}