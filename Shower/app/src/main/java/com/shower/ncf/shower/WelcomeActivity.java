package com.shower.ncf.shower;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.dou361.ijkplayer.widget.IjkVideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import tv.danmaku.ijk.media.player.IMediaPlayer;


public class WelcomeActivity extends MyActivity implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnCompletionListener {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        intent = new Intent(WelcomeActivity.this, MainActivity.class);
//        startActivity(intent);
        initStuff();
    }

    File file;
    File login;
    File start;
//    File out;

    //view
    ImageView welcome_bg;
    IjkVideoView welcome_start;

    private void initStuff(){
        welcome_bg = (ImageView) findViewById(R.id.welcome_bg);
        welcome_start = (IjkVideoView) findViewById(R.id.welcome_start);
//        welcome_start.setVisibility(View.GONE);

        file = this.getFilesDir();
//        file = Environment.getExternalStorageDirectory();
        if (!file.exists()) {
            try {
                //按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        login = new File(file + "/login.mov");
        start = new File(file + "/start.mov");
//        out = new File(file + "/getout.mov");

        if (!login.exists()) {
            try {
                //在指定的文件夹中创建文件
                login.createNewFile();
            } catch (Exception e) {
            }
        }
        if (!start.exists()) {
            try {
                //在指定的文件夹中创建文件
                start.createNewFile();
            } catch (Exception e) {
            }
        }

//        if (!out.exists()) {
//            try {
//                //在指定的文件夹中创建文件
//                out.createNewFile();
//            } catch (Exception e) {
//            }
//        }

        try {
            InputStream is = getResources().getAssets().open("start.mov");
            OutputStream os = new FileOutputStream(start);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveFile();
//        saveoutFile();
        welcome_start.setVideoPath(start.getAbsolutePath());

        //设置监听
        welcome_start.setOnPreparedListener(this);
        welcome_start.setOnErrorListener(this);
        welcome_start.setOnCompletionListener(this);

//        pauseBg();
    }

    public void saveFile(){
        new Thread(){
            @Override
            public void run() {
                try {
                    InputStream is = getResources().getAssets().open("login.mov");
                    OutputStream os = new FileOutputStream(login);
                    int bytesRead = 0;
                    byte[] buffer = new byte[8192];
                    while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    os.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

//    public void saveoutFile(){
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    InputStream is = getResources().getAssets().open("getout.mov");
//                    OutputStream os = new FileOutputStream(out);
//                    int bytesRead = 0;
//                    byte[] buffer = new byte[8192];
//                    while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
//                        os.write(buffer, 0, bytesRead);
//                    }
//                    os.close();
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();;
//    }


    private void pauseBg() {
        if (intent == null) {
            intent = new Intent(WelcomeActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

//    @Override
//    public void onPrepared(MediaPlayer mp) {
//        Log.i("00000","player ok");
//        welcome_bg.setVisibility(View.GONE);
////        welcome_start.setVisibility(View.VISIBLE);
//        welcome_start.start();
//    }
//
//    @Override
//    public boolean onError(MediaPlayer mp, int what, int extra) {
//        pauseBg();
//        return false;
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        pauseBg();
//    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        Log.i("00000","player ok");
        welcome_bg.setVisibility(View.GONE);
//        welcome_start.setVisibility(View.VISIBLE);
        welcome_start.start();
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        pauseBg();
        return false;
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        pauseBg();
    }
}
