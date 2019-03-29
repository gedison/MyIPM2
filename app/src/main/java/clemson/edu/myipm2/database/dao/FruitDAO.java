package clemson.edu.myipm2.database.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import clemson.edu.myipm2.database.DBAdapter;


/**
 * Created by gedison on 6/11/2017.
 */

public class FruitDAO {
    private Context mContext;

    public FruitDAO(Context context){
        mContext = context;
    }


    public Fruit getFruitWithId(String fruitId){
        List<Fruit> items = new ArrayList<>();

        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT fruit.name, fruit.color "+
                "FROM fruit " +
                "WHERE fruit.id = '"+fruitId+"'; ";
        
        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        for(String[] result : results) items.add(new Fruit(result));
        return (items.size() > 0) ? items.get(0): new Fruit();
    }



    public class Fruit{

        private String mName, mColor;

        Fruit(){
            mName = "";
            mColor="";
        }

        Fruit(String[] bundle) {
            mName = bundle[0];
            mColor = bundle[1];

        }

        public String getName(){
            return mName;
        }

        public String toString(){
            return mName;
        }

        public String getColor(){
            return mColor;
        }

    }

}
