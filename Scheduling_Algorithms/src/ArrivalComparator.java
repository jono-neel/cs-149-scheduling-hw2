import java.util.Comparator;
/**
 * Comparator for ordering processes by arrival time (ascending order)
 * @author jonathanneel
 *
 */
public class ArrivalComparator implements Comparator<ProcessSim>{

	@Override
	public int compare(ProcessSim o1, ProcessSim o2) {
		
		float difference = o1.getArrivalTime() - o2.getArrivalTime();
		if(difference > 0)
			return 1;
		else if (difference < 0)
			return -1;
		else return 0;
	}

}
