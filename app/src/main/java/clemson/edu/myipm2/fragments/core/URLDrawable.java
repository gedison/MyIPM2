package clemson.edu.myipm2.fragments.core;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by gedison on 7/30/2017.
 */

public class URLDrawable extends BitmapDrawable {
    protected Drawable drawable;

    public void setDrawable(Drawable d){

        drawable = d;

    }

    @Override
    public void draw(Canvas canvas) {
        if(drawable != null) {
            drawable.draw(canvas);
        }
    }
}