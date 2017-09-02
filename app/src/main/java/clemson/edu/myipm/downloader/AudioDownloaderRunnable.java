package clemson.edu.myipm.downloader;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gedison on 7/22/2017.
 */

public class AudioDownloaderRunnable implements Runnable {

    private Context myContext;

    private String[] files;

    public AudioDownloaderRunnable(Context myContext, String[] files) {
        this.myContext = myContext;
        this.files = files;
    }


    @Override
    public void run(){
        MyNotificationManager myNotificationManager = MyNotificationManager.getInstance(myContext);



        for(String fileName : files){
            if(fileName.contains("mp3") || fileName.contains("MP3")) {
                myNotificationManager.incrementBuilder();

                InputStream inputStream = null;
                OutputStream output = null;

                try {
                    URL url = new URL(fileName);
                    inputStream = new BufferedInputStream(url.openStream());
                    String[] temp = fileName.split("/");
                    fileName = temp[temp.length-1];
                    System.out.println("Saving: "+fileName);

                    File file = new File(myContext.getFilesDir(), fileName);
                    output = new FileOutputStream(file);
                    byte data[] = new byte[1024];
                    int count=0;
                    while ((count = inputStream.read(data)) != -1)output.write(data, 0, count);

                    System.out.println(fileName+" Save Worked");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    System.out.println("Malformed URL Exception: "+fileName);
                    System.out.println(fileName+" Save Failed");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("IO Exception: "+fileName);
                    System.out.println(fileName+" Save Failed");
                } finally {

                    if(inputStream!=null){
                        try {
                            inputStream.close();
                        }catch(IOException e){

                        }
                    }

                    if(output!=null){
                        try {
                            output.flush();
                            output.close();
                        }catch (IOException e){

                        }
                    }
                }
            }
        }
    }
}

