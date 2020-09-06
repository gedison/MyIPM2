package clemson.edu.myipm2.downloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import clemson.edu.myipm2.R;

/**
 * Created by gedison on 7/22/2017.
 */

public class ImageDownloaderRunnable implements Runnable {
    private Context myContext;
    private int width, height;
    private String[] files;

    public ImageDownloaderRunnable(Context myContext, int width, int height, String[] files) {
        this.myContext = myContext;
        this.width=width;
        this.height=height;
        this.files = files;
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
            return null;//BitmapFactory.decodeResource(myContext.getResources(), R.drawable.gear);
        } catch (IOException e) {
            return null;//BitmapFactory.decodeResource(myContext.getResources(), R.drawable.gear);
        }
    }

    @Override
    public void run(){
        MyNotificationManager myNotificationManager = MyNotificationManager.getInstance(myContext);

        for(String fileName : files){
            if(!(fileName.contains("mp3") || fileName.contains("MP3"))) {

                myNotificationManager.incrementBuilder();
                System.out.println(fileName);
                Bitmap bitmap = decodeSampledBitmapFromURL(fileName, width, height);
                if(bitmap == null){
                    continue;
                }

                String[] temp = fileName.split("/");
                fileName = temp[temp.length-1];
               // System.out.println("Saving: "+fileName);

                File file = new File(myContext.getFilesDir(), fileName);
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                 //   System.out.println(file.getAbsoluteFile()+" Save Success");
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
    }
}
