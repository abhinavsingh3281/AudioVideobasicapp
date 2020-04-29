package com.example.audiovide;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,SeekBar.OnSeekBarChangeListener,MediaPlayer.OnCompletionListener{

    //Main class is going to be listener
    private VideoView myVideoView;
    private Button btnPlayVideo;
    private MediaController mediaController;
    private Button btnPlayMusic,btnPauseMusic;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBarVolume,seekBarMove;

    private Timer timer;

    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myVideoView = findViewById(R.id.myVideoView);
        btnPlayVideo = findViewById(R.id.btnPlayVideo);



        mediaController=new MediaController(MainActivity.this);

        btnPlayMusic=findViewById(R.id.btnPlayMusic);
        btnPauseMusic=findViewById(R.id.btnPauseMusic);


        seekBarVolume=findViewById(R.id.seekBarVolume);

        seekBarMove=findViewById(R.id.seekBarMove);


        audioManager=(AudioManager) getSystemService(AUDIO_SERVICE);

        int maximumVolumeOfUser=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //as we are using music that's why w are using stream music

        //max volume decides the maximum volume of every user device


        int currentVolumeOfUser=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBarVolume.setMax(maximumVolumeOfUser);
        seekBarVolume.setProgress(currentVolumeOfUser);


        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {

                    Toast.makeText(MainActivity.this,Integer.toString(progress),Toast.LENGTH_SHORT).show();

                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {



            }
        });




        mediaPlayer=MediaPlayer.create(this,R.raw.music);



        seekBarMove.setOnSeekBarChangeListener(MainActivity.this);//declaring listner if its not there it will not work

        seekBarMove.setMax(mediaPlayer.getDuration());


        mediaPlayer.setOnCompletionListener(MainActivity.this);

        btnPlayVideo.setOnClickListener(MainActivity.this) ;

        btnPlayMusic.setOnClickListener(MainActivity.this);

        btnPauseMusic.setOnClickListener(MainActivity.this);


    }

    @Override
    public void onClick(View buttonView) {

        switch(buttonView.getId()){


            case R.id.btnPlayVideo:

                Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);//to accesse video
                myVideoView.setVideoURI(videoUri);

                myVideoView.setMediaController(mediaController);

                mediaController.setAnchorView(myVideoView);
                myVideoView.start();

                break;

            case R.id.btnPlayMusic:

                mediaPlayer.start();
                timer=new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {

                    //we have to schedule timer at fixed rate to new Timer task
                    //new thread is created
                    //so that other statements work properly

                    @Override
                    public void run() {
                        //update our seek bar

                        seekBarMove.setProgress(mediaPlayer.getCurrentPosition());

                    }
                },0,1000);


                break;

            case R.id.btnPauseMusic:

                mediaPlayer.pause();


                timer.cancel();



                break;
        }




    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
           // Toast.makeText(MainActivity.this,Integer.toString(progress),Toast.LENGTH_SHORT).show();

            mediaPlayer.seekTo(progress);

            //by this we will be able to change the seek bar according to the user
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mediaPlayer.pause();

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.start();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        timer.cancel();

        Toast.makeText(MainActivity.this,"Music is ended",Toast.LENGTH_SHORT).show();
    }
}
