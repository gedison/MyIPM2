package clemson.edu.myipm.fragments.affection_gallery;

import clemson.edu.myipm.database.dao.GalleryDAO;


/**
 * Created by gedison on 6/11/2017.
 */

public interface OnImageSelectionListener {
    void onImageSelection(GalleryDAO.GalleryImage item);
}
