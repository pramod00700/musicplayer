package com.example.isangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class Playsong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    TextView textView;
   ImageView play,pause,prev,next;
   GifImageView gf;
   MediaPlayer mediaPlayer;
   ArrayList<File>songs;

   String textContent;
    int position;
    SeekBar skbr;
    Thread updateseek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView=findViewById(R.id.textView);
        play=findViewById(R.id.play);
        prev=findViewById(R.id.prev);
        next=findViewById(R.id.next);
        gf=findViewById(R.id.gif);
//        play=findViewById(R.id.play);
        skbr=findViewById(R.id.skbr);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        songs=(ArrayList)bundle.getParcelableArrayList("songlist");
        textContent=intent.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);
        position=intent.getIntExtra("position",0);
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        skbr.setMax(mediaPlayer.getDuration());

        skbr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skbr.getProgress());

            }


        });
        updateseek=new Thread()
        {
            @Override
            public void run() {
               int current=0;
               try {
                   while (current<mediaPlayer.getDuration())
                   {
                       current=mediaPlayer.getCurrentPosition();
                       skbr.setProgress(current);
                       sleep(800);
                   }

               }
               catch (Exception e)
               {
                 e.printStackTrace();
               }
            }
        };
        updateseek.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.play);
                    gf.setImageResource(R.drawable.still);

                }
                else
                {
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.pause);
                    gf.setImageResource(R.drawable.appmusic1);
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    if(position!=0)
                    {
                        position-=1;
                    }
                    else
                    {
                        position=songs.size()-1;
                    }
                    Uri uri=Uri.parse(songs.get(position).toString());
                    mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.pause);
                    textContent=songs.get(position).getName().toString();
                    textView.setText(textContent);
                    skbr.setProgress(0);
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    if(position!=songs.size()-1)
                    {
                        position+=1;
                    }
                    else
                    {
                        position=0;
                    }
                    Uri uri=Uri.parse(songs.get(position).toString());
                    mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.pause);
                    textContent=songs.get(position).getName().toString();
                    textView.setText(textContent);
                    skbr.setProgress(0);

                }

            }
        });



    }
}