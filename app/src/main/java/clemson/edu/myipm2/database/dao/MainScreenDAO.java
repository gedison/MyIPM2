package clemson.edu.myipm2.database.dao;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import clemson.edu.myipm2.database.DBAdapter;


/**
 * Created by gedison on 6/11/2017.
 */

public class MainScreenDAO {
    private Context mContext;

    public MainScreenDAO(Context context){
        mContext = context;
    }

    public List<MainScreenItem> getMainScreenItems(){
        List<MainScreenItem> items = new ArrayList<>();

        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT DISTINCT app.fruitName, app.typeName, fruit.color, app.fruitID, app.affectionTypeID " +
                "FROM app " +
                "INNER JOIN fruit ON fruit.id = app.fruitID " +
                "INNER JOIN downloads ON downloads.appID = app.id " +
                "ORDER BY app.fruitName";



        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        for(String[] result : results) items.add(new MainScreenItem(result));
        return items;
    }

    public MainScreenItem getCurrentMainScreenItem(String fruitId){
        List<MainScreenItem> items = new ArrayList<>();

        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT DISTINCT app.fruitName, app.typeName, fruit.color, app.fruitID, app.affectionTypeID " +
                "FROM app " +
                "INNER JOIN fruit ON fruit.id = app.fruitID " +
                "INNER JOIN downloads ON downloads.appID = app.id " +
                "WHERE app.fruitID = '"+fruitId+"'";



       // System.out.println(sql);

        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        for(String[] result : results) items.add(new MainScreenItem(result));
        return (items.size() > 0) ? items.get(0): new MainScreenItem();
    }



    public class MainScreenItem{

        private String mName, mColor, mType, mFruitId, mAffectionTypeId;

        MainScreenItem(){
            mName = "";
            mColor="";
            mType = "";
            mFruitId = "";
            mAffectionTypeId = "";
        }
        MainScreenItem(String[] bundle) {
            mName = bundle[0];
            mType =  bundle[1];
            mColor = bundle[2];
            mFruitId = bundle[3];
            mAffectionTypeId = bundle[4];
        }

        public String toString(){
            return getName();
        }

        public String getFruitId(){
            return mFruitId;
        }

        public String getAffectionTypeId(){
            return mAffectionTypeId;
        }

        public String getName(){
            return mName +" ("+mType+")";
        }

        public String getFruitName(){
            return mName;
        }

        public String getColor(){
            return mColor;
        }

    }

}
