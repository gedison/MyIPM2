package clemson.edu.myipm.downloader;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import clemson.edu.myipm.R;

/**
 * Created by gedison on 7/22/2017.
 */

public class MyNotificationManager {

    private static MyNotificationManager instance;
    private NotificationCompat.Builder mBuilder;
    private int numberOfDownloads;
    private int numberDownloaded = 0;
    private Context context;

    private MyNotificationManager(){
    }

    public static MyNotificationManager getInstance(Context context){
        if(instance == null)instance = new MyNotificationManager();
        instance.context = context;
        return instance;
    }

    public void startNotification(int numberOfDownloads){
        this.numberOfDownloads = numberOfDownloads;

        NotificationManager mNotifyManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.main_icon);
        mBuilder.setProgress(100, 0, false);
        mNotifyManager.notify(0, mBuilder.build());
    }


    public void incrementBuilder(){
        NotificationManager mNotifyManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        numberDownloaded++;
        int progress = (int) (((float)numberDownloaded/numberOfDownloads)*100);
        mBuilder.setProgress(100, progress, false);
        mNotifyManager.notify(0, mBuilder.build());

        if(numberDownloaded == numberOfDownloads)removeNotification();
    }

    private void removeNotification(){
        NotificationManager mNotifyManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder.setContentText("Download complete").setProgress(0,0,false);
        mNotifyManager.notify(0, mBuilder.build());
    }
}
