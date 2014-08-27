package dk.andreas.tabvejrny;

import dk.andreas.tabvejrny.dataklasser.GPSBy;
import java.util.Comparator;

public class SortByDistance implements Comparator {
    public int compare(Object gps1, Object gps2) {
        GPSBy g1 = (GPSBy) gps1;
        GPSBy g2 = (GPSBy) gps2;
        if (g1.LæsAfstand() == g2.LæsAfstand()) return 0;
        if (g1.LæsAfstand() > g2.LæsAfstand()) return 1;
        else return -1;
    }
}
