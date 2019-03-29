package clemson.edu.myipm2.database.dao;

import android.content.Context;

import clemson.edu.myipm2.database.DBAdapter;

/**
 * Created by gedison on 6/18/2017.
 */

public class SituationDAO {

    private Context mContext;

    public SituationDAO(Context context){
        mContext = context;
    }

    public String getSituationString(String fruitId, String affectionTypeId){

        DBAdapter dbAdapter = new DBAdapter(mContext);

        String sql = "SELECT title, content, placement FROM situation " +
                "WHERE situation.fruitID = \""+fruitId+"\" " +
                "AND situation.affectionTypeId =  \""+affectionTypeId+"\" "+
                "ORDER BY placement";

        String ret = "";

        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        for(String[] result : results){
            if(!result[0].isEmpty() && !result[0].equals("null"))ret+="<b>"+result[0]+"</b><br>";
            if(!result[1].isEmpty() && !result[1].equals("null"))ret+=result[1]+"<br><br>";
        }
        return ret;
    }
}
