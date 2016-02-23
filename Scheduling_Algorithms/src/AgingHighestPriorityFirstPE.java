import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Aging highest priority first (preemptive)
 * @author Tin Che
 */
public class AgingHighestPriorityFirstPE extends SchedulingAlgorithm {

    private int MAX_PRIORITY = 4;
    private PriorityQueue<ProcessSim> processQueue1, processQueue2, processQueue3, processQueue4;
    private ArrayList<PriorityQueue<ProcessSim>> queueList;

    public AgingHighestPriorityFirstPE(ArrayDeque<ProcessSim> list) 
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
                increasePriority();
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
            increaseWaitedQuantum();
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
