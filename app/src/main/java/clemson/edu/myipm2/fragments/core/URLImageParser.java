package clemson.edu.myipm2.fragments.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import clemson.edu.myipm2.R;

/**
 * Created by gedison on 7/30/2017.
 */

public class URLImageParser implements Html.ImageGetter{
    private Context c;
    protected TextView container;

    public URLImageParser(TextView t, Context c) {
        this.c = c;
        this.container = t;
    }

    public Drawable getDrawable(String source) {
        URLDrawable urlDrawable = new URLDrawable();
        new DrawableLoaderTask(urlDrawable, c, 300, 300).execute(source);
        return urlDrawable;
    }



    private class DrawableLoaderTask extends AsyncTask<String, Void, Drawable> {
        private URLDrawable urlDrawable;
        private Context myContext;
        private int width, height;

        public DrawableLoaderTask(URLDrawable d, Context myContext, int width, int height) {
            urlDrawable = d;
            this.myContext = myContext;
            this.width=width;
            this.height=height;
        }

        protected Drawable doInBackground(String... params) {

            String[] temp = params[0].split("/");
            String fileName = temp[temp.length-1];
            System.out.println(fileName+" trying to load this");
            return decodeSampledBitmapFromURL(fileName, width, height);
        }

        protected void onPostExecute(Drawable result) {
            urlDrawable.setBounds(0, 0, result.getIntrinsicWidth(), result.getIntrinsicHeight());
            urlDrawable.drawable = result;
            URLImageParser.this.container.invalidate();
            container.setText(container.getText());
        }

        public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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

        public Drawable decodeSampledBitmapFromURL(String fileName, int reqWidth, int reqHeight) {
            try {
                File file = new File(myContext.getFilesDir(), fileName);
                InputStream inputStream = new FileInputStream(file);

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream, null, options);

                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
                options.inJustDecodeBounds = false;
                inputStream = new FileInputStream(file);
                Bitmap mBitmap = BitmapFactory.decodeStream(inputStream, null, options);

                BitmapDrawable mDrawable = new BitmapDrawable(myContext.getResources(), mBitmap);
                mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
                return mDrawable;

            } catch (IOException e) {
                e.printStackTrace();

                Bitmap mBitmap = BitmapFactory.decodeResource(myContext.getResources(), R.drawable.gear);
                BitmapDrawable mDrawable = new BitmapDrawable(myContext.getResources(), mBitmap);
                mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
                return mDrawable;
            }
        }


    }


}
