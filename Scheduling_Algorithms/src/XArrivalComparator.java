import java.util.Comparator;
/**
 * Comparator for ordering processes by x arrival time (ascending order)
 * @author Katherine Soohoo
 *
 */
public class XArrivalComparator implements Comparator<ProcessSim>{

	@Override
	public int compare(ProcessSim o1, ProcessSim o2) {
		
		float difference = o1.getXArrivalTime() - o2.getXArrivalTime();
		if(difference > 0)
			return 1;
		else if (difference < 0)
			return -1;
		else return 0;
	}

}
