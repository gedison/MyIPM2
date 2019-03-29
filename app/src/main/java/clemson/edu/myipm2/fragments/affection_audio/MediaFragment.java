package clemson.edu.myipm2.fragments.affection_audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.io.IOException;

import clemson.edu.myipm2.R;
import clemson.edu.myipm2.database.dao.AudioDAO;
import clemson.edu.myipm2.fragments.core.OnAffectionChangedListener;
import clemson.edu.myipm2.utility.SharedPreferencesHelper;


public class MediaFragment extends Fragment implements OnAffectionChangedListener, OnPreparedListener,MediaController.MediaPlayerControl, OnSeekBarChangeListener, Runnable {

    private MediaPlayer mediaPlayer;

    private View root;
    private Button playButton;
    private SeekBar seekBar;
    private TextView time;
    public String filename="";

    private Boolean set = false;
    private Boolean started = false;
    private Boolean first = true;
    public boolean isRunning=false;

    public MediaFragment() {}

    public static MediaFragment newInstance(AudioDAO.Audio audio) {
        MediaFragment fragment = new MediaFragment();
        Bundle args = new Bundle();
        args.putString("fileName", audio.getUrl());
        args.putString("author", audio.getAuthor());
        args.putString("title", audio.getTitle());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.media_fragment, container, false);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);

        seekBar = (SeekBar)root.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        time = (TextView)root.findViewById(R.id.time);


        playButton =(Button) root.findViewById(R.id.myButton);
        playButton.setBackgroundResource(R.drawable.pause);
        playButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if(set){
                    if(mediaPlayer.isPlaying())pause();
                    else start();
                }}});

        System.out.println("Created media fragment");
        if(this.getArguments()!=null){
            System.out.println("Not null");
            this.filename = this.getArguments().getString("fileName");
            String fileName = this.getArguments().getString("fileName");
            String author = this.getArguments().getString("author");
            String title = this.getArguments().getString("title");
            this.setDataSource(fileName, title, author, false);
        }else{
            System.out.println("Null");
            root.setVisibility(View.GONE);
        }

        mediaPlayer.setOnCompletionListener(new OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp) {
                playButton.setBackgroundResource(R.drawable.play);
                mediaPlayer.seekTo(0);
                seekBar.setProgress(0);
                time.setText(convertMilliseconds((mediaPlayer.getDuration())));
            }
        });

        return root;
    }

    public String convertMilliseconds(int ms){
        ms/=1000;
        int seconds = ms%60;
        int minutes = ms/60;

        String ret = minutes+":";
        if(seconds<10)ret+="0";
        ret+=""+seconds;

        return ret;
    }

    public Boolean isSet(){
        return set;
    }

    public void reset(){
        set=false;
        pause();
        mediaPlayer.reset();
    }

    public void setDataSource(String path, String title, String author, Boolean shouldPlay){
        try {
            set=true;
            root.setVisibility(View.VISIBLE);
            ((TextView)root.findViewById(R.id.title)).setText(title);
            ((TextView)root.findViewById(R.id.author)).setText(author);

            System.out.println(path);
            path = getContext().getFilesDir()+"/"+path;
            mediaPlayer.setDataSource(path);


            mediaPlayer.prepare();
            time.setText(convertMilliseconds((mediaPlayer.getDuration())));

            if(shouldPlay){
                playButton.setBackgroundResource(R.drawable.pause);
                mediaPlayer.start();

                started=true;
                seekBar.setProgress(0);
                seekBar.setMax(mediaPlayer.getDuration());
                new Thread(this).start();
            }else{
                playButton.setBackgroundResource(R.drawable.play);
            }
        } catch (IllegalArgumentException e) {
            root.setVisibility(View.GONE);
            System.out.println("Illegal Argument Exception");
        } catch (SecurityException e) {
            root.setVisibility(View.GONE);
            System.out.println("Security Exception");
        } catch (IllegalStateException e) {
            root.setVisibility(View.GONE);
            System.out.println("Illegal State Exception");
        } catch (IOException e) {
            root.setVisibility(View.GONE);
            System.out.println("IO Exception");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void start() {
        if(!started){
            started=true;
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
            new Thread(this).start();
        }
        playButton.setBackgroundResource(R.drawable.pause);
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        playButton.setBackgroundResource(R.drawable.play);
        mediaPlayer.pause();
    }

    @Override
    public void onPause(){
        super.onPause();

        reset();

    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void onAffectionChanged() {
        reset();

        AudioDAO audioDAO = new AudioDAO(getContext());
        AudioDAO.Audio audio = audioDAO.getAudio(SharedPreferencesHelper.getAffectionId(getContext()));
        this.setDataSource(audio.getUrl(),audio.getAuthor(), audio.getTitle(), false);
    }

    public class MyMediaController extends MediaController {

        public MyMediaController(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyMediaController(Context context, boolean useFastForward) {
            super(context, useFastForward);
        }

        public MyMediaController(Context context) {
            super(context);
        }

        @Override
        public void show(int timeout) {
            super.show(0);
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) mediaPlayer.seekTo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    public void changeText(int myTime){
        time.setText(convertMilliseconds(myTime));
    }

    public void run() {
        int currentPosition= 0;
        int total = mediaPlayer.getDuration();
        while (getActivity()!=null && mediaPlayer!=null && currentPosition<total) {
            try {
                isRunning=true;
                Thread.sleep(1000);
                currentPosition= mediaPlayer.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }
            seekBar.setProgress(currentPosition);

            if(getActivity()==null)return;
            try{
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {changeText((mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition()));
                    }});
            }catch(NullPointerException e){return;}
        }
    }

}
