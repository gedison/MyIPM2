package clemson.edu.myipm2.database.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import clemson.edu.myipm2.database.DBAdapter;

/**
 * Created by gedison on 6/18/2017.
 */

public class GalleryDAO {

    private Context mContext;

    public GalleryDAO(Context context){
        mContext = context;
    }

    public List<GalleryImage> getImages(String affectionId){
        List<GalleryImage> images = new ArrayList<>();
        DBAdapter dbAdapter = new DBAdapter(mContext);
        String sql = "SELECT gallery.id, gallery.placement, gallery.imageURL " +
                "FROM gallery WHERE affectionID = \""+affectionId+"\" ORDER BY gallery.placement";


        String[][] results = dbAdapter.getMultidimensionalArrayOfStringsFromCursor(dbAdapter.runSelectQuery(sql, true));
        for(String[] result : results){
           images.add(new GalleryImage(result));
        }return images;
    }

    public class GalleryImage{
        private String imageURL;

        public GalleryImage(String[] bundle){
            imageURL = bundle[2];
        }

        public String getImageURL(){
            return imageURL;
        }
    }

}
