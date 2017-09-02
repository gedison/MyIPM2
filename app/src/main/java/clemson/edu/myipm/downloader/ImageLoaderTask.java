package clemson.edu.myipm.downloader;

/**
 * Created by gedison on 7/16/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import clemson.edu.myipm.R;

/**
 * Created by gedison on 2/24/2016.
 *
 * Loads an image from a URL
 */

public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private Context myContext;
    private int width, height;


    public ImageLoaderTask(ImageView imageView, Context myContext, int width, int height) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.myContext = myContext;
        this.width=width;
        this.height=height;
    }

    protected Bitmap doInBackground(String... params) {

        String[] temp = params[0].split("/");
        String fileName = temp[temp.length-1];
        System.out.println(fileName+" trying to load this");
        return decodeSampledBitmapFromURL(fileName, width, height);
    }

    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }else{
            if(bitmap==null)System.out.println("Bitmap was null");
            if(imageViewReference == null)System.out.println("View was null");
            System.out.println("Something was null");
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;

        System.out.println(height+" "+width);
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

    public Bitmap decodeSampledBitmapFromURL(String fileName, int reqWidth, int reqHeight) {
        try {
            File file = new File(myContext.getFilesDir(), fileName);
            InputStream inputStream = new FileInputStream(file);

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            inputStream = new FileInputStream(file);
            return BitmapFactory.decodeStream(inputStream, null, options);
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("IO EXCEPTIOn");
            return BitmapFactory.decodeResource(myContext.getResources(), R.drawable.gear);
        }
    }


}

