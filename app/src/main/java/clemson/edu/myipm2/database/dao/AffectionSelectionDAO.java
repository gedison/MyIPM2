package clemson.edu.myipm2.database.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import clemson.edu.myipm2.database.DBAdapter;

/**
 * Created by gedison on 6/18/2017.
 */

public class AffectionSelectionDAO {

    private Context mContext;

    public AffectionSelectionDAO(Context context){
        mContext = context;
    }

    public List<Affection> getAffections(String fruitId, String affectionTypeId){
        List<Affection> items = new ArrayList<>();

        DBAdapter dbAdapter = new DBAdapter(mContext);

        String sql = "SELECT t1.affectionID, t1.name, t1.imageUrl FROM " +
                "(SELECT affection.name, gallery.affectionID, gallery.imageURL, gallery.placement " +
                "FROM gallery " +
                "INNER JOIN affection ON gallery.affectionID = affection.id " +
                "WHERE affection.fruitID = \""+fruitId+"\" " +
                "AND affection.affectionTypeId =  \""+affectionTypeId+"\" " +
                "ORDER BY affection.name) t1 " +
                "JOIN (SELECT gallery.affectionID, MIN(gallery.placement) AS minPlacement " +
                "FROM gallery GROUP BY gallery.affectionID) t2 " +
                "ON t2.affectionID = t1.affectionID AND t2.minPlacement = t1.placement  GROUP BY t1.affectionID ORDER BY t1.name;";

        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        for(String[] result : results) items.add(new Affection(result));
        return items;
    }

    public class Affection{
        private String affectionId;
        private String affectionName;
        private String imageURL;

        public Affection(String[] bundle){
            affectionId = bundle[0];
            affectionName = bundle[1];
            imageURL = bundle[2];
        }

        public String getAffectionId(){
            return affectionId;
        }

        public String getName(){
            return affectionName;
        }

        public String getImageURL() { return imageURL;}

    }
}
