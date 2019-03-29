package clemson.edu.myipm2.database.dao;

import android.content.Context;

import clemson.edu.myipm2.database.DBAdapter;

/**
 * Created by gedison on 6/18/2017.
 */

public class AudioDAO {

    private Context mContext;

    public AudioDAO(Context context){
        mContext = context;
    }


    public Audio getAudio(String affectionId){

        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT audio.audioURL, audio.title, audio.author " +
                "FROM audio WHERE affectionID = \""+affectionId+"\"";


        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        return (results.length>0 && results[0]!= null) ? new Audio(results[0]) : new Audio();
    }

    public boolean doesAffectionHaveAudio(String affectionId){
        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT audio.audioURL, audio.title, audio.author " +
                "FROM audio WHERE affectionID = \""+affectionId+"\"";


        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        return (results.length>0 && results[0]!= null);
    }

    public class Audio{
        private String url="", title="", author="";

        Audio(){}

        Audio(String[] bundle){
            String[] temp = bundle[0].split("/");
            url = temp[temp.length-1];
            title = bundle[1];
            author = bundle[2];
        }

        public String getUrl(){return url;}
        public String getTitle(){return title;}
        public String getAuthor(){return author;}
    }

}
