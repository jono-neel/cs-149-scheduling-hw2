import java.util.ArrayList;

/**
 * An interface that all scheduling algorithms should follow. May need to add more
 * or may not be needed at all, I'm not sure.
 * @author Jonathan Neel
 *
 */
public interface SchedulingAlgorithm {
	/**
	 * Runs the algorithm, given a list of processes in order of arrival time.
	 * @param list the list of processes to be run
	 */
	public void run(ArrayList<ProcessSim> list);
}
