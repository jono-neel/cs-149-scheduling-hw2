import java.util.ArrayDeque;
import java.util.PriorityQueue;

public class HighestPriorityFirstPE extends SchedulingAlgorithm {

	private PriorityQueue<ProcessSim> processQueue1, processQueue2, processQueue3, processQueue4;

	public HighestPriorityFirst(ArrayDeque<ProcessSim> list) {
		super(list);
		processQueue1 = new PriorityQueue<>(new RunTimeComparator());
		processQueue2 = new PriorityQueue<>(new RunTimeComparator());
		processQueue3 = new PriorityQueue<>(new RunTimeComparator());
		processQueue4 = new PriorityQueue<>(new RunTimeComparator());
	}

	@Override
	public void run() {
		while (quantum < 100) {
			if (!processList.isEmpty() && processList.peek().getArrivalTime() < quantum) {
				if (processList.getFirst().getPriority() == 1) {
					processQueue1.add(processList.pop());
					processList.pop();
				}
				if (processList.getFirst().getPriority() == 2) {
					processQueue2.add(processList.pop());
					processList.pop();
				}
				if (processList.getFirst().getPriority() == 3) {
					processQueue3.add(processList.pop());
					processList.pop();
				}
				if (processList.getFirst().getPriority() == 4) {
					processQueue4.add(processList.pop());
					processList.pop();
				}

				if (processQueue1.isEmpty() || processQueue2.isEmpty() || processQueue3.isEmpty()
						|| processQueue4.isEmpty()) {
					timeChart.add(new ProcessSim());
					quantum += 1;
				}

				else {
					// processQueue1.add(processQueue1.remove());
					executeProcess(processQueue1.remove());

					// processQueue2.add(processQueue2.remove());
					executeProcess(processQueue2.remove());

					// processQueue3.add(processQueue3.remove());
					executeProcess(processQueue3.remove());

					// processQueue4.add(processQueue4.remove());
					executeProcess(processQueue4.remove());
				}
			}
		}
	}

	private void executeProcess(ProcessSim process) {
		timeChart.add(process);
		// check if process has been started before
		if (!process.getReadyState()) {
			process.setReadyState(true);
			totalWaitTime += (quantum - process.getArrivalTime());
		}

		process.setRemainingRunTime(process.getRemainingRunTime() - 1);
		// check if process finished
		if (process.getRemainingRunTime() <= 0) {
			totalTurnaroundTime += (quantum - process.getArrivalTime());
		}

		quantum += 1;
	}

}
