package clemson.edu.myipm.downloader;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import clemson.edu.myipm.R;

/**
 * Created by gedison on 2/24/2016.
 *
 * Loads an image from a URL
 */
public class ImageDownloaderTask extends AsyncTask<String, Void, Void> {
    //private final WeakReference<ImageView> imageViewReference;
    private Context myContext;
    private int width, height;
    private  NotificationCompat.Builder mBuilder;



    public ImageDownloaderTask(NotificationCompat.Builder mBuilder, Context myContext, int width, int height) {
        this.myContext = myContext;
        this.width=width;
        this.height=height;
        this.mBuilder = mBuilder;
    }



    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromURL(String imageURL, int reqWidth, int reqHeight) {
        try {
            URL url = new URL(imageURL);
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
        } catch (MalformedURLException e) {
            return BitmapFactory.decodeResource(myContext.getResources(), R.drawable.gear);
        } catch (IOException e) {
            return BitmapFactory.decodeResource(myContext.getResources(), R.drawable.gear);
        }
    }


    @Override
    protected Void doInBackground(String... files) {
        NotificationManager mNotifyManager =  (NotificationManager) myContext.getSystemService(Context.NOTIFICATION_SERVICE);
        int count = 0;

        mBuilder.setContentText("Downloading Images");
        for(String fileName : files){
            if(!(fileName.contains("mp3") || fileName.contains("MP3"))) {
                count++;
                mBuilder.setProgress(100, (int)(((float)count/files.length)*100), false);
                mNotifyManager.notify(0, mBuilder.build());

                System.out.println(fileName);
                Bitmap bitmap = decodeSampledBitmapFromURL(fileName, width, height);

                String[] temp = fileName.split("/");
                fileName = temp[temp.length-1];
                System.out.println("Saving: "+fileName);

                File file = new File(myContext.getFilesDir(), fileName);
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 65, out);
                    System.out.println(file.getAbsoluteFile()+" Save Success");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(fileName+" Save Failed");
                } finally {
                    try {if (out != null)out.close();
                    } catch (IOException e) {e.printStackTrace();}
                }
                
                System.out.println(file+" done");
            }
        }

        return null;
    }
}
