package com.shower.ncf.shower;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dou361.ijkplayer.widget.IjkVideoView;
import com.shower.ncf.shower.util.MyUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Administrator on 2016/11/25.
 */
public class MainActivity extends MyActivity implements View.OnClickListener {
    Context context;
    MyUtil util;

    //view
    TextView main_time;
    ImageView main_sound;
    TextView main_temp1;
    TextView main_temp2;

    LinearLayout main_used_bg;
    ImageView main_used_img;
    TextView main_used_text;

    LinearLayout main_auto_bg;
    ImageView main_auto_img;
    TextView main_auto_text;

    LinearLayout main_guest_bg;
    ImageView main_guest_img;
    TextView main_guest_text;

    TextView main_temp_l;
    TextView main_temp_r;

    //view
    LinearLayout main_main;
    ImageView main_img;
    AnimationDrawable animationDrawable;

    //handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    main_time.setText(util.getTime());
                    break;
                case 21:
                    animationDrawable.start();
                    break;
                case 22:
                    animationDrawable.stop();
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        timeThread();
    }

    public void initView() {
        context = this;
        main_time = (TextView) findViewById(R.id.main_time);
        main_sound = (ImageView) findViewById(R.id.main_sound);
        main_temp1 = (TextView) findViewById(R.id.main_temp1);
        main_temp2 = (TextView) findViewById(R.id.main_temp2);

        main_used_bg = (LinearLayout) findViewById(R.id.main_used_bg);
        main_used_img = (ImageView) findViewById(R.id.main_used_img);
        main_used_text = (TextView) findViewById(R.id.main_used_text);

        main_auto_bg = (LinearLayout) findViewById(R.id.main_auto_bg);
        main_auto_img = (ImageView) findViewById(R.id.main_auto_img);
        main_auto_text = (TextView) findViewById(R.id.main_auto_text);

        main_guest_bg = (LinearLayout) findViewById(R.id.main_guest_bg);
        main_guest_img = (ImageView) findViewById(R.id.main_guest_img);
        main_guest_text = (TextView) findViewById(R.id.main_guest_text);

        main_temp_l = (TextView) findViewById(R.id.main_temp_l);
        main_temp_r = (TextView) findViewById(R.id.main_temp_r);

        main_main = (LinearLayout) findViewById(R.id.main_main);


        main_img = (ImageView) findViewById(R.id.main_img);
        main_img.setImageResource(R.drawable.out_animation);
        animationDrawable = (AnimationDrawable) main_img.getDrawable();
        animationDrawable.stop();
        animationDrawable.setOneShot(true);

        main_sound.setOnClickListener(this);
        main_used_bg.setOnClickListener(this);
        main_guest_bg.setOnClickListener(this);
        main_auto_bg.setOnClickListener(this);
        main_temp_l.setOnClickListener(this);
        main_temp_r.setOnClickListener(this);

        util = new MyUtil();
        main_time.setText(util.getTime());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_sound:
                break;
            case R.id.main_used_bg:
                setTypeOfShower(1);
                break;
            case R.id.main_guest_bg:
                setTypeOfShower(3);
                break;
            case R.id.main_auto_bg:
                setTypeOfShower(2);
                break;
            case R.id.main_temp_l:
                setTypeOfTemperature(1);
                break;
            case R.id.main_temp_r:
                setTypeOfTemperature(2);
                break;
        }
    }

    private void setTypeOfShower(int i) {
        main_used_bg.setBackground(getResources().getDrawable(R.drawable.main_choose1));
        main_auto_bg.setBackground(getResources().getDrawable(R.drawable.main_choose1));
        main_guest_bg.setBackground(getResources().getDrawable(R.drawable.main_choose1));

        main_used_img.setImageDrawable(getResources().getDrawable(R.drawable.main_person_n));
        main_auto_img.setImageDrawable(getResources().getDrawable(R.drawable.main_computer_n));
        main_guest_img.setImageDrawable(getResources().getDrawable(R.drawable.main_teacup_n));

        main_used_text.setTextColor(getResources().getColor(R.color.white));
        main_auto_text.setTextColor(getResources().getColor(R.color.white));
        main_guest_text.setTextColor(getResources().getColor(R.color.white));

        switch (i) {
            case 1:
                main_used_text.setTextColor(getResources().getColor(R.color.main_color));
                main_used_img.setImageDrawable(getResources().getDrawable(R.drawable.main_person_s));
                main_used_bg.setBackground(getResources().getDrawable(R.drawable.main_choose3));
                break;
            case 2:
                main_auto_text.setTextColor(getResources().getColor(R.color.main_color));
                main_auto_img.setImageDrawable(getResources().getDrawable(R.drawable.main_computer_s));
                main_auto_bg.setBackground(getResources().getDrawable(R.drawable.main_choose3));
                break;
            case 3:
                main_guest_text.setTextColor(getResources().getColor(R.color.main_color));
                main_guest_img.setImageDrawable(getResources().getDrawable(R.drawable.main_teacup_s));
                main_guest_bg.setBackground(getResources().getDrawable(R.drawable.main_choose3));
                break;
        }
    }

    private void setTypeOfTemperature(int i) {
        main_temp_l.setBackgroundColor(0x0fff);
        main_temp_r.setBackgroundColor(0x0fff);

        switch (i){
            case 1:
                main_temp_l.setBackground(getResources().getDrawable(R.drawable.main_text_s_l));
                break;
            case 2:
                main_temp_r.setBackground(getResources().getDrawable(R.drawable.main_text_s_r));
                break;
        }

        Intent intent = new Intent(MainActivity.this,SettingActivity.class);
        startActivity(intent);

    }

    private void timeThread(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                while(true){
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    boolean isPrepared = false;

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            setAnimation();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    void setAnimation(){
        main_main.setVisibility(View.GONE);

        new Thread(){
            @Override
            public void run() {
                handler.sendEmptyMessage(21);
                try {
                    sleep(1024);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(22);

            }


        }.start();



    }

}
