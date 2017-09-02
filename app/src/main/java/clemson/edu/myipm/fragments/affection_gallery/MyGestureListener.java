package clemson.edu.myipm.fragments.affection_gallery;

import android.graphics.Point;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by gedison on 9/8/2015.
 */

public class MyGestureListener extends GestureDetector.SimpleOnGestureListener implements ScaleGestureDetector.OnScaleGestureListener{

    boolean zoomed=false;
    float myDeltaX, myDeltaY;
    ImageView myImage;

    boolean getY = true;
    float centerZoomedY;
    int screenX, screenY;
    boolean imageAnimating=false;

    float currentX, currentY;

    public MyGestureListener(ImageView imageView, Point size){
        this.myImage=imageView;
        screenX=size.x;
        screenY=size.y;
    }

    public void zoomImage(){

        int zoomFactor = 2;
        int width = myImage.getLayoutParams().width;
        int height = myImage.getLayoutParams().height;

        if(zoomed){
            myImage.setLayoutParams(new LinearLayout.LayoutParams(width / zoomFactor, ViewGroup.LayoutParams.WRAP_CONTENT));
            if(getY)centerZoomedY = myImage.getY();
            myImage.setX((screenX - width) / 2);
            myImage.setY(centerZoomedY);
        }else{
            myImage.setLayoutParams(new LinearLayout.LayoutParams(width*zoomFactor, height*zoomFactor));
        }zoomed=!zoomed;
    }


    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(zoomed && !imageAnimating && !isScaling) {

            if (getY) {
                centerZoomedY = myImage.getY();
                getY = false;
            }

            boolean scrollMovementWithinBounds = false;
            int variance = 20;
            if(
                    currentX <= (distanceX+variance)
                    && currentX>=(distanceX-variance)
                    && currentY <= (distanceY+variance)
                    && currentY>=(distanceY-variance)
            )scrollMovementWithinBounds = true;

            currentX = distanceX;
            currentY = distanceY;

            if (scrollMovementWithinBounds) {
                distanceX = (distanceX * (18 / 10));
                distanceY = (distanceY * (18 / 10));

                float newX = myImage.getX() - distanceX;
                float newY = myImage.getY() - distanceY;

                if (screenY < myImage.getHeight()
                        && newY > -((myImage.getHeight() - screenY) + (screenY / 4))
                        && newY < (screenY / 4)) {
                    myImage.setY(newY);
                    myDeltaY += distanceY;
                }

                if (screenX < myImage.getWidth()
                        && newX > -((myImage.getWidth() - screenX) + (screenX / 4))
                        && newX < (screenX / 4)) {
                    myImage.setX(newX);
                    myDeltaX += distanceX;
                }
            }
        }return true;
    }

    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        final float distanceTimeFactor = 0.4f;
        if(!imageAnimating && !isScaling) {

            if (screenX < myImage.getWidth()) {
                myDeltaX = (distanceTimeFactor * velocityX / 2);

                float newX = myImage.getX() + myDeltaX;
                if (!(newX > -((myImage.getWidth() - screenX) + (screenX / 4))))
                    myDeltaX = -((myImage.getWidth() - screenX) + (screenX / 4)) - myImage.getX();
                else if (!(newX < (screenX / 4))) myDeltaX = (screenX / 4) - myImage.getX();
            } else myDeltaX = 0;

            if (screenY < myImage.getHeight()) {
                myDeltaY = (distanceTimeFactor * velocityY / 2);

                float newY = myImage.getY() + myDeltaY;
                if (!(newY > -((myImage.getHeight() - screenY) + (screenY / 4))))
                    myDeltaY = -((myImage.getHeight() - screenY) + (screenY / 4)) - myImage.getY();
                else if (!(newY < (screenY / 4))) myDeltaY = (screenY / 4) - myImage.getY();
            } else myDeltaY = 0;


            Animation animation = new TranslateAnimation(0, myDeltaX, 0, myDeltaY);
            animation.setDuration((long) (1000 * distanceTimeFactor));
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    imageAnimating = true;
                }

                public void onAnimationEnd(Animation animation) {
                    myImage.clearAnimation();

                    currentX = 0;
                    currentY = 0;
                    myImage.setX(myImage.getX() + myDeltaX);
                    myImage.setY(myImage.getY() + (myDeltaY));
                    imageAnimating = false;
                }

                public void onAnimationRepeat(Animation animation) {
                }

            });

            myImage.startAnimation(animation);
        }return true;
    }

    public boolean onDoubleTap(MotionEvent e) {
        zoomImage();
        return true;
    }

    double startSpan = 0;
    int startWidth=0, startHeight=0;
    boolean isScaling = false;

    public void scaleImageBy(double scaleBy){
        if((myImage.getWidth()<(screenX/10) || myImage.getHeight()<(screenY/10)) && scaleBy<0)return;

        myImage.setLayoutParams(new LinearLayout.LayoutParams(
                (int) (myImage.getWidth()+scaleBy),
                (int) (myImage.getHeight()+scaleBy))
        );

        if(zoomed && scaleBy<0){
            int centerX = (screenX - myImage.getWidth()) / 2;
            if(myImage.getX()>centerX)myImage.setX(myImage.getX() + (float)scaleBy);
            if(myImage.getX()<centerX)myImage.setX(myImage.getX() - (float)scaleBy);

            int centerY = (screenY - myImage.getHeight())/2;
            if(myImage.getY()>centerY)myImage.setY(myImage.getY() + (float) scaleBy);
            if(myImage.getY()<centerY)myImage.setY(myImage.getY() - (float) scaleBy);
        }
    }

    public void finishScaleGesture(double currentSpan){
        int zoomFactor = 2;

        if((!zoomed && currentSpan<=startSpan)||(zoomed && currentSpan>=startSpan)){
            myImage.setLayoutParams(new LinearLayout.LayoutParams(startWidth, startHeight));
        }

        else if(!zoomed && currentSpan>startSpan){
            myImage.setLayoutParams(new LinearLayout.LayoutParams(startWidth * zoomFactor, startHeight * zoomFactor));
            zoomed=true;
        }

        else if (zoomed && currentSpan < startSpan) {
            myImage.setLayoutParams(new LinearLayout.LayoutParams(startWidth / zoomFactor, startHeight / zoomFactor));
            myImage.setX((screenX - myImage.getWidth()) / 2);
            myImage.setY((screenY - myImage.getHeight())/2);
            zoomed=false;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               isScaling=false;
            }
        },500);
    }


    public boolean onScale(ScaleGestureDetector detector) {
        scaleImageBy(detector.getCurrentSpan()-detector.getPreviousSpan());
        return true;
    }


    public boolean onScaleBegin(ScaleGestureDetector detector) {
        if(imageAnimating)return false;
        isScaling=true;
        startHeight=myImage.getHeight();
        startWidth=myImage.getWidth();
        startSpan = detector.getCurrentSpan();
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector detector) {
        finishScaleGesture(detector.getCurrentSpan());
    }
}