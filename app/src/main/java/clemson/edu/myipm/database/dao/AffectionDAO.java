package clemson.edu.myipm.database.dao;

import android.content.Context;

import clemson.edu.myipm.database.DBAdapter;

/**
 * Created by gedison on 6/18/2017.
 */

public class AffectionDAO {

    private Context mContext;

    public AffectionDAO(Context context){
        mContext = context;
    }

    public String getAffectionName(String affectionId){
        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT affection.name FROM affection WHERE affection.id = '"+affectionId+"';";

        String ret = "";
        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        return results[0][0];
    }

}
