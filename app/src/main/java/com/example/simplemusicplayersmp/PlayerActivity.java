package com.example.simplemusicplayersmp;

import static com.example.simplemusicplayersmp.AlbumDetailsAdapter.albumFiles_MOA;
import static com.example.simplemusicplayersmp.MainActivity.musicFiles_MOA;
import static com.example.simplemusicplayersmp.MainActivity.repeatBool_MOA;
import static com.example.simplemusicplayersmp.MainActivity.shuffleBool_MOA;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{

    TextView song_name_MOA, artist_name_MOA, duration_played_MOA, duration_total_MOA, now_playing_MOA;
    ImageView cover_art_MOA, nextBtn_MOA, prevBtn_MOA, backBtn_MOA, shuffleBtn_MOA, repeatBtn_MOA, menuBtn_MOA;
    FloatingActionButton playPauseBtn_MOA;
    SeekBar seekBar_MOA;
    int position_MOA = -1;
    static ArrayList<MusicFiles> listSongs_MOA = new ArrayList<>();
    static ArrayList<Integer> shuffleOrder_MOA = new ArrayList<>();
    static Uri uri_MOA;
    static MediaPlayer mediaPlayer_MOA;
    private Handler handler_MOA = new Handler();
    private Thread playThread_MOA, prevThread_MOA, nextThread_MOA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        getIntentMethod();
        song_name_MOA.setText(listSongs_MOA.get(position_MOA).getTitle_MOA());
        artist_name_MOA.setText(listSongs_MOA.get(position_MOA).getArtist_MOA());
        mediaPlayer_MOA.setOnCompletionListener(this);
        seekBar_MOA.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer_MOA != null && fromUser) {
                    mediaPlayer_MOA.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer_MOA != null) {
                    int mCurrentPosition = mediaPlayer_MOA.getCurrentPosition() / 1000;
                    seekBar_MOA.setProgress(mCurrentPosition);
                    duration_played_MOA.setText(formattedTime(mCurrentPosition));
                }
                handler_MOA.postDelayed(this, 1000);
            }
        });
        shuffleBtn_MOA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffleBool_MOA) {
                    shuffleBool_MOA = false;
                    shuffleBtn_MOA.setImageResource(R.drawable.ic_shuffle_off);
                } else {
                    shuffleBool_MOA = true;
                    shuffleBtn_MOA.setImageResource(R.drawable.ic_shuffle_on);
                }
            }
        });
        repeatBtn_MOA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatBool_MOA) {
                    repeatBool_MOA = false;
                    repeatBtn_MOA.setImageResource(R.drawable.ic_repeat_off);
                } else {
                    repeatBool_MOA = true;
                    repeatBtn_MOA.setImageResource(R.drawable.ic_repeat_on);
                }
            }
        });
        backBtn_MOA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void prevThreadBtn() {
        prevThread_MOA = new Thread() {
            @Override
            public void run() {
                super.run();
                prevBtn_MOA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread_MOA.start();
    }

    private void prevBtnClicked() {
        if (mediaPlayer_MOA.isPlaying() && mediaPlayer_MOA.getCurrentPosition() < 2000) {
            mediaPlayer_MOA.stop();
            mediaPlayer_MOA.release();
            position_MOA = ((position_MOA - 1) < 0 ? (listSongs_MOA.size() - 1) : position_MOA - 1);
            uri_MOA = Uri.parse(listSongs_MOA.get(position_MOA).getPath_MOA());
            mediaPlayer_MOA = MediaPlayer.create(getApplicationContext(), uri_MOA);
            metaData(uri_MOA);
            song_name_MOA.setText(listSongs_MOA.get(position_MOA).getTitle_MOA());
            artist_name_MOA.setText(listSongs_MOA.get(position_MOA).getArtist_MOA());
            seekBar_MOA.setMax(mediaPlayer_MOA.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer_MOA != null) {
                        int mCurrentPosition = mediaPlayer_MOA.getCurrentPosition() / 1000;
                        seekBar_MOA.setProgress(mCurrentPosition);
                    }
                    handler_MOA.postDelayed(this, 1000);
                }
            });
            mediaPlayer_MOA.setOnCompletionListener(this);
            playPauseBtn_MOA.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer_MOA.start();
        } else if (mediaPlayer_MOA.isPlaying() && mediaPlayer_MOA.getCurrentPosition() > 2000) {
            mediaPlayer_MOA.seekTo(0);
            mediaPlayer_MOA.start();
        } else {
            mediaPlayer_MOA.stop();
            mediaPlayer_MOA.release();
            position_MOA = ((position_MOA - 1) < 0 ? (listSongs_MOA.size() - 1) : position_MOA - 1);
            uri_MOA = Uri.parse(listSongs_MOA.get(position_MOA).getPath_MOA());
            mediaPlayer_MOA = MediaPlayer.create(getApplicationContext(), uri_MOA);
            metaData(uri_MOA);
            song_name_MOA.setText(listSongs_MOA.get(position_MOA).getTitle_MOA());
            artist_name_MOA.setText(listSongs_MOA.get(position_MOA).getArtist_MOA());
            seekBar_MOA.setMax(mediaPlayer_MOA.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer_MOA != null) {
                        int mCurrentPosition = mediaPlayer_MOA.getCurrentPosition() / 1000;
                        seekBar_MOA.setProgress(mCurrentPosition);
                    }
                    handler_MOA.postDelayed(this, 1000);
                }
            });
            mediaPlayer_MOA.setOnCompletionListener(this);
            playPauseBtn_MOA.setBackgroundResource(R.drawable.ic_play);
        }

    }

    private void nextThreadBtn() {
        nextThread_MOA = new Thread() {
            @Override
            public void run() {
                super.run();
                nextBtn_MOA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread_MOA.start();
    }

    private void nextBtnClicked() {
        if (mediaPlayer_MOA.isPlaying())
        {
            mediaPlayer_MOA.stop();
            mediaPlayer_MOA.release();
            if (shuffleBool_MOA && !repeatBool_MOA)
            {
                position_MOA = getRandom(listSongs_MOA.size() - 1);
                // check if position is already in shuffleOrder
                while (shuffleOrder_MOA.contains(position_MOA))
                {
                    position_MOA = getRandom(listSongs_MOA.size() - 1);
                }
                shuffleOrder_MOA.add(position_MOA);
            }
            else if (!shuffleBool_MOA && !repeatBool_MOA)
            {
                position_MOA = ((position_MOA + 1) % listSongs_MOA.size());
            }
            else if (!shuffleBool_MOA && repeatBool_MOA)
            {
                position_MOA = ((position_MOA + 1) % listSongs_MOA.size());
            }
            uri_MOA = Uri.parse(listSongs_MOA.get(position_MOA).getPath_MOA());
            mediaPlayer_MOA = MediaPlayer.create(getApplicationContext(), uri_MOA);
            metaData(uri_MOA);
            song_name_MOA.setText(listSongs_MOA.get(position_MOA).getTitle_MOA());
            artist_name_MOA.setText(listSongs_MOA.get(position_MOA).getArtist_MOA());
            seekBar_MOA.setMax(mediaPlayer_MOA.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer_MOA != null) {
                        int mCurrentPosition = mediaPlayer_MOA.getCurrentPosition() / 1000;
                        seekBar_MOA.setProgress(mCurrentPosition);
                    }
                    handler_MOA.postDelayed(this, 1000);
                }
            });
            mediaPlayer_MOA.setOnCompletionListener(this);
            playPauseBtn_MOA.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer_MOA.start();
        }
        else
        {
            mediaPlayer_MOA.stop();
            mediaPlayer_MOA.release();
            if (shuffleBool_MOA && !repeatBool_MOA)
            {
                position_MOA = getRandom(listSongs_MOA.size() - 1);
            }
            else if (!shuffleBool_MOA && !repeatBool_MOA)
            {
                position_MOA = ((position_MOA + 1) % listSongs_MOA.size());
            }
            uri_MOA = Uri.parse(listSongs_MOA.get(position_MOA).getPath_MOA());
            mediaPlayer_MOA = MediaPlayer.create(getApplicationContext(), uri_MOA);
            metaData(uri_MOA);
            song_name_MOA.setText(listSongs_MOA.get(position_MOA).getTitle_MOA());
            artist_name_MOA.setText(listSongs_MOA.get(position_MOA).getArtist_MOA());
            seekBar_MOA.setMax(mediaPlayer_MOA.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer_MOA != null) {
                        int mCurrentPosition = mediaPlayer_MOA.getCurrentPosition() / 1000;
                        seekBar_MOA.setProgress(mCurrentPosition);
                    }
                    handler_MOA.postDelayed(this, 1000);
                }
            });
            mediaPlayer_MOA.setOnCompletionListener(this);
            playPauseBtn_MOA.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);
    }

    private void playThreadBtn() {
        playThread_MOA = new Thread() {
            @Override
            public void run() {
                super.run();
                playPauseBtn_MOA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread_MOA.start();

    }

    private void playPauseBtnClicked() {
        if (mediaPlayer_MOA.isPlaying())
        {
            playPauseBtn_MOA.setImageResource(R.drawable.ic_play);
            mediaPlayer_MOA.pause();
            seekBar_MOA.setMax(mediaPlayer_MOA.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer_MOA != null) {
                        int mCurrentPosition = mediaPlayer_MOA.getCurrentPosition() / 1000;
                        seekBar_MOA.setProgress(mCurrentPosition);
                    }
                    handler_MOA.postDelayed(this, 1000);
                }
            });
        }
        else
        {
            playPauseBtn_MOA.setImageResource(R.drawable.ic_pause);
            mediaPlayer_MOA.start();
            seekBar_MOA.setMax(mediaPlayer_MOA.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer_MOA != null) {
                        int mCurrentPosition = mediaPlayer_MOA.getCurrentPosition() / 1000;
                        seekBar_MOA.setProgress(mCurrentPosition);
                    }
                    handler_MOA.postDelayed(this, 1000);
                }
            });
        }
    }

    private String formattedTime(int mCurrentPosition) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    private void getIntentMethod() {
        position_MOA = getIntent().getIntExtra("position", -1);
        String sender = getIntent().getStringExtra("sender");
        if (sender != null && sender.equals("albumDetails"))
        {
            listSongs_MOA = albumFiles_MOA;
        }
        else
        {
            listSongs_MOA = musicFiles_MOA;
        }
        if (listSongs_MOA != null) {
            playPauseBtn_MOA.setImageResource(R.drawable.ic_pause);
            uri_MOA = Uri.parse(listSongs_MOA.get(position_MOA).getPath_MOA());
        }
        if (mediaPlayer_MOA != null) {
            mediaPlayer_MOA.stop();
            mediaPlayer_MOA.release();
            mediaPlayer_MOA = MediaPlayer.create(getApplicationContext(), uri_MOA);
            mediaPlayer_MOA.start();
        } else {
            mediaPlayer_MOA = MediaPlayer.create(getApplicationContext(), uri_MOA);
            mediaPlayer_MOA.start();
        }
        seekBar_MOA.setMax(mediaPlayer_MOA.getDuration() / 1000);
        metaData(uri_MOA);
    }

    private void initViews() {
        song_name_MOA = findViewById(R.id.song_name);
        artist_name_MOA = findViewById(R.id.song_artist);
        now_playing_MOA = findViewById(R.id.now_playing);
        duration_played_MOA = findViewById(R.id.durationPlayed);
        duration_total_MOA = findViewById(R.id.durationTotal);
        cover_art_MOA = findViewById(R.id.cover_art);
        nextBtn_MOA = findViewById(R.id.id_next);
        prevBtn_MOA = findViewById(R.id.id_prev);
        backBtn_MOA = findViewById(R.id.back_btn);
        menuBtn_MOA = findViewById(R.id.menu_btn);
        shuffleBtn_MOA = findViewById(R.id.id_shuffle);
        repeatBtn_MOA = findViewById(R.id.id_repeat);
        playPauseBtn_MOA = findViewById(R.id.play_pause);
        seekBar_MOA = findViewById(R.id.seekBar);
    }

    private void metaData (Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(listSongs_MOA.get(position_MOA).getDuration_MOA()) / 1000;
        duration_total_MOA.setText(formattedTime(durationTotal));
        byte[] art = retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if (art != null) {
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            ImageAnimation(this, cover_art_MOA, bitmap);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch != null)
                    {
                        ImageView gradient = findViewById(R.id.imageViewGradient);
                        RelativeLayout mContainer = findViewById(R.id.mContainer);
                        gradient.setBackgroundResource(R.drawable.gradient_bg);
                        mContainer.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), 0x00000000});
                        gradient.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), swatch.getRgb()});
                        mContainer.setBackground(gradientDrawableBg);
                        double luminance = 0.299 * Color.red(swatch.getRgb()) + 0.587 * Color.green(swatch.getRgb()) + 0.114 * Color.blue(swatch.getRgb());
                        if (luminance > 160) {
                            song_name_MOA.setTextColor(Color.BLACK);
                            artist_name_MOA.setTextColor(Color.BLACK);
                            duration_played_MOA.setTextColor(Color.BLACK);
                            duration_total_MOA.setTextColor(Color.BLACK);
                            now_playing_MOA.setTextColor(Color.BLACK);
                            backBtn_MOA.setImageResource(R.drawable.ic_chevron_left_black);
                            menuBtn_MOA.setImageResource(R.drawable.ic_menu_black);
                            seekBar_MOA.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);
                            seekBar_MOA.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.SRC_IN);
                        } else {
                            song_name_MOA.setTextColor(Color.WHITE);
                            artist_name_MOA.setTextColor(Color.WHITE);
                            duration_played_MOA.setTextColor(Color.WHITE);
                            duration_total_MOA.setTextColor(Color.WHITE);
                            now_playing_MOA.setTextColor(Color.WHITE);
                            backBtn_MOA.setImageResource(R.drawable.ic_chevron_left);
                            menuBtn_MOA.setImageResource(R.drawable.ic_menu);
                            seekBar_MOA.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorWhite), android.graphics.PorterDuff.Mode.MULTIPLY);
                            seekBar_MOA.getThumb().setColorFilter(getResources().getColor(R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
                        }
                    }
                    else
                    {
                        ImageView gradient = findViewById(R.id.imageViewGradient);
                        RelativeLayout mContainer = findViewById(R.id.mContainer);
                        gradient.setBackgroundResource(R.drawable.gradient_bg);
                        mContainer.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, 0x00000000});
                        gradient.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, 0xff000000});
                        mContainer.setBackground(gradientDrawableBg);
                        song_name_MOA.setTextColor(Color.WHITE);
                        artist_name_MOA.setTextColor(Color.WHITE);
                        duration_played_MOA.setTextColor(Color.WHITE);
                        duration_total_MOA.setTextColor(Color.WHITE);
                        now_playing_MOA.setTextColor(Color.WHITE);
                        backBtn_MOA.setImageResource(R.drawable.ic_chevron_left);
                        menuBtn_MOA.setImageResource(R.drawable.ic_menu);
                    }
                }
            });
        }
        else {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.music)
                    .into(cover_art_MOA);
            ImageView gradient = findViewById(R.id.imageViewGradient);
            RelativeLayout mContainer = findViewById(R.id.mContainer);
            gradient.setBackgroundResource(R.drawable.gradient_bg);
            mContainer.setBackgroundResource(R.drawable.main_bg);
            song_name_MOA.setTextColor(Color.WHITE);
            artist_name_MOA.setTextColor(Color.WHITE);
            duration_played_MOA.setTextColor(Color.WHITE);
            duration_total_MOA.setTextColor(Color.WHITE);
            now_playing_MOA.setTextColor(Color.WHITE);
            backBtn_MOA.setImageResource(R.drawable.ic_chevron_left);
            menuBtn_MOA.setImageResource(R.drawable.ic_menu);
            // get default image luminance
            Bitmap bitmapDefault = BitmapFactory.decodeResource(getResources(), R.drawable.music);
            double luminance = 0.299 * Color.red(bitmapDefault.getPixel(0, 0)) + 0.587 * Color.green(bitmapDefault.getPixel(0, 0)) + 0.114 * Color.blue(bitmapDefault.getPixel(0, 0));
            if (luminance > 160) {
                seekBar_MOA.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);
                seekBar_MOA.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                seekBar_MOA.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorWhite), android.graphics.PorterDuff.Mode.MULTIPLY);
                seekBar_MOA.getThumb().setColorFilter(getResources().getColor(R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }
    }

    public void ImageAnimation(Context context, ImageView imageView, Bitmap bitmap) {
        Animation animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animOut.setDuration(1000);
        animIn.setDuration(1000);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageView.setImageBitmap(bitmap);
                imageView.startAnimation(animIn);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                animIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(animIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animOut);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnClicked();

        if (mediaPlayer_MOA != null) {
            mediaPlayer_MOA = MediaPlayer.create(getApplicationContext(), uri_MOA);
            mediaPlayer_MOA.start();
            mediaPlayer_MOA.setOnCompletionListener(this);
        }
    }
}