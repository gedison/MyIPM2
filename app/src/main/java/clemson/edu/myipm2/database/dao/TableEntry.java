package clemson.edu.myipm2.database.dao;

import java.io.Serializable;

/**
 * Created by gedison on 7/8/2017.
 */

public abstract class TableEntry implements Serializable {
    public abstract String getValueAtIndex(int i);
    public abstract String getDisplayAtIndex(int i);
    public abstract String getColorAtIndex(int i);
    public abstract String getId();
    public abstract String getOtherId();
    public abstract int compareTo(TableEntry b, int i, boolean doubleSort);
}