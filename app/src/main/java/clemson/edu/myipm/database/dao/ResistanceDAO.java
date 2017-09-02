package clemson.edu.myipm.database.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import clemson.edu.myipm.database.DBAdapter;

/**
 * Created by gedison on 6/18/2017.
 */

public class ResistanceDAO {

    private Context mContext;

    public ResistanceDAO(Context context){
        mContext = context;
    }

    public String getResistanceString(String fruitId, String affectionTypeId){

        DBAdapter dbAdapter = new DBAdapter(mContext);

        String sql = "SELECT title, content, placement FROM resistance " +
                "WHERE resistance.fruitID = \""+fruitId+"\" " +
                "AND resistance.affectionTypeId =  \""+affectionTypeId+"\" "+
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
