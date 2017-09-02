package clemson.edu.myipm.database.dao;

import android.content.Context;
import android.database.DatabaseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import clemson.edu.myipm.database.DBAdapter;

import static clemson.edu.myipm.database.dao.AffectionActive.EFFICACY_VALUES;

/**
 * Created by gedison on 6/18/2017.
 */

public class SearchDAO {

    private Context mContext;

    public SearchDAO(Context context){
        mContext = context;
    }

    public List<SearchResult> getResultsFromSearch(String stringToSearchFor){

        stringToSearchFor = DatabaseUtils.sqlEscapeString("%"+stringToSearchFor+"%");

        List<SearchResult> searchResults = new ArrayList<>();
        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT distinct trade.name as tradename, active.name as activename," +
                "affection_trade.affectionID, affection.name as affectionname, affection.fruitID, " +
                "fruit.name as fruitname, fruit.color, affection_active.efficacy, trade.rate, " +
                "active.typeID, active.id, affection.affectionTypeID " +
                "FROM trade " +
                "INNER JOIN active ON trade.activeID = active.id " +
                "INNER JOIN affection_trade ON trade.id = affection_trade.tradeID " +
                "INNER JOIN affection_active ON " +
                "affection_trade.affectionID = affection_active.affectionID " +
                "AND trade.activeID = affection_active.activeID " +
                "INNER JOIN affection ON affection_trade.affectionID = affection.id " +
                "INNER JOIN fruit on affection.fruitID = fruit.id " +
                "WHERE ((trade.activeID IN " +
                "(SELECT id FROM active WHERE active.name LIKE "+stringToSearchFor+")) " +
                "OR (trade.id IN (SELECT id FROM trade WHERE trade.name LIKE "+stringToSearchFor+"))) " +
                "AND fruit.id IN (SELECT fruitID FROM app WHERE app.id IN (SELECT appID FROM downloads))" +
                "ORDER BY trade.name, affection.fruitID ASC, affection.name;";

        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        String currentTradeName= "";
        int index = 0;
        for(String[] result : results){
            if(!currentTradeName.equals(result[0])){
                currentTradeName = result[0];
                searchResults.add(new SearchResult());
                index = searchResults.size()-1;
            }

            searchResults.get(index).addResult(result);
        }return searchResults;
    }


    public class AffectionFruit{

        String tradeName, activeId, activeName, affectionName, fruitId, fruitName, fruitColor, affectionId, efficacy, rate, typeId, affectionTypeId;

        public AffectionFruit(String[] bundle){
            this.tradeName = bundle[0];
            this.activeName = bundle[1];
            this.affectionName = bundle[3];
            this.fruitId = bundle[4];
            this.fruitName = bundle[5];
            this.fruitColor = bundle[6];
            this.affectionId = bundle[2];
            this.efficacy = bundle[7];
            this.rate = bundle[8];
            this.typeId = bundle[9];
            this.activeId = bundle[10];
            this.affectionTypeId = bundle[11];
        }

        public String getFruitId(){return fruitId;}
        public String getAffectionId(){return affectionId;}
        public String getActiveId(){return activeId;}
        public String getTypeId(){return typeId;}
        public String getFruitColor(){
            return fruitColor;
        }
        public String getAffectionTypeId(){return affectionTypeId;}

        public String toString(){
            return affectionName+" ("+
                    EFFICACY_VALUES[Integer.parseInt(efficacy)]+"; "+
                    (rate.equals("-1") ? "N/A" : rate)+")";
        }
    }

    public class SearchResult{
        String tradeName = "", activeName = "";
        public Map<String, List<AffectionFruit>> affectionFruitMap = new HashMap<>();

        public String toString(){
            return tradeName +" ("+activeName+")";
        }

        public Set<String> getKeys(){
            return affectionFruitMap.keySet();
        }

        public List<AffectionFruit> getAffectionFruitForFruit(String fruitName){
            if(affectionFruitMap.containsKey(fruitName)) return affectionFruitMap.get(fruitName);
            else return new ArrayList<AffectionFruit>();
        }

        public void addResult(String[] bundle){

            if(tradeName.isEmpty())tradeName = bundle[0];
            if(activeName.isEmpty())activeName = bundle[1];

            System.out.println(tradeName+ " " +activeName);

            if(!affectionFruitMap.containsKey(bundle[5]))affectionFruitMap.put(bundle[5], new ArrayList<AffectionFruit>());
            affectionFruitMap.get(bundle[5]).add(new AffectionFruit(bundle));
        }
    }

}
