package clemson.edu.myipm.database.dao;

import android.content.Context;

import clemson.edu.myipm.database.DBAdapter;

/**
 * Created by gedison on 6/18/2017.
 */

public class MoreDAO {

    private Context mContext;

    public MoreDAO(Context context){
        mContext = context;
    }

    public More getMore(String affectionId){
        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT more.name, more.symptoms, more.chemical, more.fungicide, more.biological " +
                "FROM more WHERE affectionID = \""+affectionId+"\"";


        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        if(results.length>0)return(new More(results[0]));
        else return (new More());
    }

    public class More{
        private String name="", symptoms="", chemical="", fungicide="", biological="";

        public More(){

        }

        public More(String[] bundle){
            name = bundle[0];
            symptoms = bundle[1];
            chemical = bundle[2];
            fungicide = bundle[3];
            biological = bundle[4];
        }

        public String getName(){
            return name;
        }

        public String getTitleByIndex(int index){
            switch(index){
                case 0: return name;
                case 1: return "<b>Chemical Control</b>";
                case 2: return "<b>Specific Resistance Issues</b>";
                case 3: return "<b>Non-Chemical Control</b>";
                default: return name;
            }
        }

        public String getSymptoms(){

            return symptoms;
        }

        public String getChemical(){
            return chemical;
        }

        public String getFungicide(){
            return fungicide;
        }

        public String getBiological(){
            return biological;
        }
    }

}
