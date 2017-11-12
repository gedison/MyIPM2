package clemson.edu.myipm.database.dao;

import android.content.Context;
import clemson.edu.myipm.database.DBAdapter;

/**
 * Created by gedison on 7/8/2017.
 */

public class AffectionTradeDAO {

    private Context mContext;

    public AffectionTradeDAO(Context context){
        mContext = context;
    }

    public TableEntry[] getAffectionTradeDataWithTypeWithAffection(int typeId, String affectionId){
        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT trade.name, trade.activeName, trade.rate, trade.phi, trade.rei, " +
                "trade.maxSpray, trade.maxProduct, trade.id, active.color, active.id, active.codeName, aquaticAlgae," +
                "aquaticInvertebrates, avianAcute,avianReproductive,earthworm,fishChronic," +
                "smallMammalAcute,dermalCancer,dermalAcute,inhalation,consumerCancer," +
                "humanDietary,pollinatorOffCrop,pollinatorNoBloom,pollinatorInBloom "+
                "FROM trade " +
                "INNER JOIN affection_trade on trade.id = affection_trade.tradeID " +
                "INNER JOIN active on trade.activeID = active.id " +
                "WHERE affection_trade.affectionID = '"+affectionId+"' " +
                "AND trade.typeID = '"+typeId+"' ORDER BY trade.name";




        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        TableEntry[] ret = new TableEntry[results.length];
        for(int i = 0; i<results.length; i++) ret[i] = new AffectionTrade(results[i]);
        return ret;
    }

    public TableEntry[] getAffectionTradeDataWithTypeWithAffectionWithActiveId(int typeId, String affectionId, int activeId){
        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT trade.name, trade.activeName, trade.rate, trade.phi, trade.rei, " +
                "trade.maxSpray, trade.maxProduct, trade.id, active.color, active.id, active.codeName, aquaticAlgae," +
                "aquaticInvertebrates, avianAcute,avianReproductive,earthworm,fishChronic," +
                "smallMammalAcute,dermalCancer,dermalAcute,inhalation,consumerCancer," +
                "humanDietary,pollinatorOffCrop,pollinatorNoBloom,pollinatorInBloom "+
                "FROM trade " +
                "INNER JOIN affection_trade on trade.id = affection_trade.tradeID " +
                "INNER JOIN active on trade.activeID = active.id " +
                "WHERE affection_trade.affectionID = '"+affectionId+"' " +
                "AND trade.activeId = '"+activeId+"' "+
                "AND trade.typeID = '"+typeId+"' ORDER BY trade.name";


        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        TableEntry[] ret = new TableEntry[results.length];
        for(int i = 0; i<results.length; i++) ret[i] = new AffectionTrade(results[i]);
        return ret;
    }



}
