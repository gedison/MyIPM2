package clemson.edu.myipm.downloader;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import clemson.edu.myipm.R;

import static android.R.id.input;

/**
 * Created by gedison on 2/24/2016.
 *
 * Loads an image from a URL
 */
public class AudioDownloaderTask extends AsyncTask<String, Void, Void> {
    private Context myContext;
    private NotificationCompat.Builder mBuilder;

    public AudioDownloaderTask(NotificationCompat.Builder mBuilder, Context myContext) {
        this.myContext = myContext;
        this.mBuilder = mBuilder;
    }

    @Override
    protected Void doInBackground(String... files) {
        NotificationManager mNotifyManager =  (NotificationManager) myContext.getSystemService(Context.NOTIFICATION_SERVICE);

        int otherCount = 0;
        for(String fileName : files) {
            if (!(fileName.contains("mp3") || fileName.contains("MP3")))otherCount++;
        }

        mBuilder.setContentText("Downloading Audio");
        for(String fileName : files){
            if(fileName.contains("mp3") || fileName.contains("MP3")) {
                System.out.println(fileName);
                otherCount++;
                mBuilder.setProgress(100, ((int)(((float)otherCount/files.length)*100)), false);
                mNotifyManager.notify(0, mBuilder.build());


                try {
                    URL url = new URL(fileName);
                    InputStream inputStream = new BufferedInputStream(url.openStream());
                    String[] temp = fileName.split("/");
                    fileName = temp[temp.length-1];
                    System.out.println("Saving: "+fileName);

                    File file = new File(myContext.getFilesDir(), fileName);
                    OutputStream output = new FileOutputStream(file);
                    byte data[] = new byte[1024];
                    int count=0;
                    while ((count = inputStream.read(data)) != -1)output.write(data, 0, count);

                    output.flush();
                    output.close();
                    inputStream.close();

                    System.out.println(fileName+" Save Worked");
                } catch (MalformedURLException e) {
                    System.out.println(e);
                    System.out.println("Malformed URL Exception: "+fileName);
                    System.out.println(fileName+" Save Failed");
                } catch (IOException e) {
                    System.out.println(e);
                    System.out.println("IO Exception: "+fileName);
                    System.out.println(fileName+" Save Failed");
                }
            }
        }

        mBuilder.setContentText("Download complete").setProgress(0,0,false);
        mNotifyManager.notify(0, mBuilder.build());

        return null;
    }
}
