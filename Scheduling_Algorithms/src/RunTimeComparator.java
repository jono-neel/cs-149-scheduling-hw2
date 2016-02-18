import java.util.Comparator;

/**
 * Sorts processes by run time - low to high.
 * @author Jonathan Neel
 */

public class RunTimeComparator implements Comparator<ProcessSim>{


	@Override
	public int compare(ProcessSim o1, ProcessSim o2) {
	
		float difference = o1.getRunTime() - o2.getRunTime();
		if(difference > 0)
			return 1;
		else if (difference < 0)
			return -1;
		else return 0;
		
	}

}
