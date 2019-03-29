package clemson.edu.myipm2.fragments.affection_gallery;

import clemson.edu.myipm2.database.dao.GalleryDAO;


/**
 * Created by gedison on 6/11/2017.
 */

public interface OnImageSelectionListener {
    void onImageSelection(GalleryDAO.GalleryImage item);
}
