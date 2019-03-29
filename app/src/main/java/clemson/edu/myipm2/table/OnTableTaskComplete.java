package clemson.edu.myipm2.table;

import clemson.edu.myipm2.database.dao.TableEntry;

public interface OnTableTaskComplete {

    void onTableTaskComplete(TableEntry[] tableEntries);
}
