import java.util.Comparator;
/**
 * Comparator for ordering processes by Priority. in order from highest to low, 
 * 1 is highest priority
 * @author Jonathan Neel
 *
 */
public class PriorityComparator implements Comparator<ProcessSim>{

	@Override
	public int compare(ProcessSim o1, ProcessSim o2) {
		
		return o1.getPriority() - o2.getPriority();
	}

}
