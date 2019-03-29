package clemson.edu.myipm2.database.dao;

import android.content.Context;

import clemson.edu.myipm2.database.DBAdapter;

/**
 * Created by gedison on 6/18/2017.
 */

public class OverviewDAO {

    private Context mContext;

    public OverviewDAO(Context context){
        mContext = context;
    }

    public String getOverviewText(String affectionId){
        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT summary FROM overview WHERE affectionID = \""+affectionId+"\"";

        String ret = "";
        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        for(String[] result : results) ret+=result[0]+"<br><br>";

        return ret;
    }

}
