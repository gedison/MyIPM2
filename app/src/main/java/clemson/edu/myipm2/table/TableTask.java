package clemson.edu.myipm2.table;

import android.content.Context;
import android.os.AsyncTask;

import clemson.edu.myipm2.database.dao.AffectionActiveDAO;
import clemson.edu.myipm2.database.dao.AffectionTradeDAO;
import clemson.edu.myipm2.database.dao.TableEntry;

public class TableTask extends AsyncTask<Void, Void, TableEntry[]> {

    int tableType, activeId, typeID;
    String affectionId;
    Context context;
    OnTableTaskComplete taskInterface;

    public TableTask(Context context, OnTableTaskComplete taskInterface, int tableType, int activeId, String affectionId,int typeID){
        this.tableType = tableType;
        this.activeId = activeId;
        this.affectionId = affectionId;
        this.typeID = typeID;
        this.context = context;
        this.taskInterface = taskInterface;
    }

    @Override
    protected TableEntry[] doInBackground(Void... voids) {
        TableEntry [] data = null;
        if(tableType == 0){
            AffectionActiveDAO affectionActiveDAO = new AffectionActiveDAO(context);
            if(activeId!=-1) {
                data = affectionActiveDAO.getAffectionActiveDataWithTypeWithAffectionWithActiveId(typeID, affectionId, activeId);
            }else{
                data = affectionActiveDAO.getAffectionActiveDataWithTypeWithAffection(typeID, affectionId);
            }
        }
        else {
            AffectionTradeDAO affectionTradeDAO = new AffectionTradeDAO(context);
            if(activeId!=-1)
                data = affectionTradeDAO.getAffectionTradeDataWithTypeWithAffectionWithActiveId(typeID, affectionId, activeId);

            else
                data = affectionTradeDAO.getAffectionTradeDataWithTypeWithAffection(typeID, affectionId);
        }

        return data;
    }

    @Override
    protected void onPostExecute(TableEntry[] tableEntries) {
        super.onPostExecute(tableEntries);
        taskInterface.onTableTaskComplete(tableEntries);
    }
}
