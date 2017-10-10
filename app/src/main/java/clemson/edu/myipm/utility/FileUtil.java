package clemson.edu.myipm.utility;

import android.content.Context;

import java.io.File;

public class FileUtil {

    public static boolean hasFileBeenDownloaded(Context context, String filename){
        boolean ret = false;
        String[] temp = filename.split("/");
        filename = temp[temp.length-1];
        File file = new File(context.getFilesDir(), filename);
        return file.exists();
    }
}
