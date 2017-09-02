package clemson.edu.myipm.database.dao;

import android.content.Context;

import clemson.edu.myipm.database.DBAdapter;

/**
 * Created by gedison on 6/18/2017.
 */

public class GuideDAO {

    private Context mContext;

    public GuideDAO(Context context){
        mContext = context;
    }

    public String getGuideString(String fruitId, String affectionTypeId){

        DBAdapter dbAdapter = new DBAdapter(mContext);

        String sql = "SELECT title, content, imageURL, placement FROM guide " +
                "WHERE guide.fruitID = \""+fruitId+"\" " +
                "AND guide.affectionTypeId =  \""+affectionTypeId+"\" "+
                "ORDER BY placement";

        String ret = "";

        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        for(String[] result : results){
            if(!result[2].isEmpty() && !result[2].equals("null")){
                ret+="<img src=\""+result[2]+"\"/><br><br>";
            }
            if(!result[0].isEmpty() && !result[0].equals("null"))ret+="<b>"+result[0]+"</b><br>";
            if(!result[1].isEmpty() && !result[1].equals("null"))ret+=result[1]+"<br><br>";
        }
        return ret;
    }
}
