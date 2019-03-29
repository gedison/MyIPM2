package clemson.edu.myipm2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import clemson.edu.myipm2.database.InitDatabaseFromWebTask;
import clemson.edu.myipm2.database.OnInitFinishedListener;
import clemson.edu.myipm2.database.OnSyncFinishedListener;
import clemson.edu.myipm2.database.dao.AppDAO;
import clemson.edu.myipm2.database.dao.DownloadDAO;
import clemson.edu.myipm2.downloader.AudioDownloaderRunnable;
import clemson.edu.myipm2.downloader.ImageDownloaderRunnable;
import clemson.edu.myipm2.downloader.MyNotificationManager;
import clemson.edu.myipm2.fragments.downloader.FruitSelectorDownloadFragment;
import clemson.edu.myipm2.fragments.downloader.OnFruitSelectionDownloadListener;
import clemson.edu.myipm2.utility.FileUtil;
import clemson.edu.myipm2.utility.SharedPreferencesHelper;

public class DownloaderActivity extends AppCompatActivity implements OnFruitSelectionDownloadListener, OnSyncFinishedListener {

    ProgressDialog progressDialog;
    private Set<AppDAO.App> selectedItems = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(SharedPreferencesHelper.getDownloaderHelp(this))showDownloaderHelperAlert();
        createView();
    }

    void createView(){

        if(findViewById(R.id.fragment_container_top) != null){
            FruitSelectorDownloadFragment fruitSelectorDownloadFragment = FruitSelectorDownloadFragment.newInstance(2);
            fruitSelectorDownloadFragment.setArguments(getIntent().getExtras());

            if(getSupportFragmentManager().findFragmentById(R.id.fragment_container_top)!=null)
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_top, fruitSelectorDownloadFragment).commit();
            else
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_top, fruitSelectorDownloadFragment).commit();

        }
    }

    public void onStartSync(){
        new InitDatabaseFromWebTask(getApplicationContext(), this).execute();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Syncing database");
        progressDialog.setMessage("Syncing database please wait.");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    public void onSyncFinished() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        downloadImages();
    }


    public void onClick(View v){
        AppDAO appDAO = new AppDAO(getApplicationContext());

        boolean allAppsExist = true;
        for(AppDAO.App item : selectedItems){
            if(!appDAO.doesAppExist(item)){
                allAppsExist = false;
            }

        }

        if(!allAppsExist){
            onStartSync();
        }

        downloadImages();
    }

    public void downloadImages(){
        List<String> filesToDownloadTemp = new ArrayList<String>();
        DownloadDAO downloadDAO = new DownloadDAO(getApplicationContext());
        AppDAO appDAO = new AppDAO(getApplicationContext());

        for(AppDAO.App item : selectedItems){
            if(appDAO.hasAppBeenDownloaded(item)){
                String[] temp = downloadDAO.getFilesToDownload(item.getFruitId(),item.getAffectionTypeId());
                for(String file : temp){
                    if(!FileUtil.hasFileBeenDownloaded(this, file)){
                        filesToDownloadTemp.add(file);
                    }
                }
                appDAO.setAppToDownloaded(item);
            }else{
                appDAO.removeApp(item);
            }
        }

        String[] filesToDownload = new String[filesToDownloadTemp.size()];
        for(int i=0; i<filesToDownloadTemp.size(); i++)filesToDownload[i] = filesToDownloadTemp.get(i);

        if(filesToDownload.length>0) {
            MyNotificationManager myNotificationManager = MyNotificationManager.getInstance(getApplicationContext());
            myNotificationManager.startNotification(filesToDownload.length);

            new Thread(
                    new ImageDownloaderRunnable(getApplicationContext(), 250, 250, filesToDownload)
            ).start();

            new Thread(
                    new AudioDownloaderRunnable(getApplicationContext(), filesToDownload)
            ).start();


            createAlertDialog();
        }else{
            finish();
        }
    }

    public void onFruitSelection(AppDAO.App item) {
        if(selectedItems.contains(item))selectedItems.remove(item);
        else selectedItems.add(item);
    }

    public void showDownloaderHelperAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.download_help_contents).setTitle(R.string.download_help_title)
                .setNegativeButton("Okay", null);
        builder.create();
        builder.show();
    }

    public void createAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.download_contents).setTitle(R.string.download_title)
                .setPositiveButton("Okay", null);
        builder.create();
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
        builder.show();
    }

}
