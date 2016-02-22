import java.util.Comparator;

/**
 * Sorts processes by remaining run time from lowest to highest.
 * @author Katherine Soohoo
 */

public class RemainingRunTimeComparator implements Comparator<ProcessSim>
{
    @Override
    public int compare(ProcessSim o1, ProcessSim o2)
    {

        float difference = o1.getRemainingRunTime() - o2.getRemainingRunTime();
        if(difference > 0)
                return 1;
        else if (difference < 0)
                return -1;
        else return 0;

    }
}
