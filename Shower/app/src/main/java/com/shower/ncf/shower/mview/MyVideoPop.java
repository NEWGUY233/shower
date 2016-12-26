package com.shower.ncf.shower.mview;

import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shower.ncf.shower.R;

/**
 * Created by Administrator on 2016/12/23.
 */

public class MyVideoPop extends PopupWindow implements View.OnTouchListener {
    Context c;
    View contentView;
    RelativeLayout include;
    ImageView voice_progress;
    ImageView video_thumb;
    ImageView voice_bg;
    AudioManager audio ;

    int sound;
    int max;

    public MyVideoPop(Context c){
        this.c = c;
        View contentView = LayoutInflater.from(c).inflate(R.layout.view_video_pop,null);
//        initView(contentView);
        setContentView(contentView);

        audio = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        max = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        setWidth(100);
        setHeight(200);


    }

    void initView(View contentView){
        voice_progress = (ImageView) contentView.findViewById(R.id.voice_second);
        video_thumb = (ImageView) contentView.findViewById(R.id.video_thumb);
        voice_bg = (ImageView) contentView.findViewById(R.id.voice_bg);
        include.setOnTouchListener(this);

    }
    float downY = 0;
    float moveY = 0;
    float upY = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = event.getY();
                changeLocation(downY,moveY);
                downY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                upY = event.getY();
                changeLocation(downY,upY);
                break;

        }
        return true;
    }

    private void changeLocation(float downY,float moveY){

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) voice_progress.getLayoutParams();

        int ht = (int) (voice_progress.getHeight() + (downY - moveY));
        if (ht > voice_bg.getHeight())
            ht = voice_bg.getHeight();
        if (ht < 0)
            ht = 0;
        lp.height = ht;


        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) video_thumb.getLayoutParams();
        lp1.bottomMargin = voice_progress.getHeight() - video_thumb.getHeight()/2;

        voice_progress.setLayoutParams(lp);
        video_thumb.setLayoutParams(lp1);

        int i = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        audio.setStreamVolume(AudioManager.STREAM_MUSIC,
                i *voice_progress.getHeight()/voice_bg.getHeight() ,0);
    }

    public void showPopUp(View v) {
        LinearLayout layout = new LinearLayout(c);
        layout.setBackgroundColor(Color.GRAY);
        TextView tv = new TextView(c);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT));
        tv.setTextColor(Color.WHITE);
        layout.addView(tv);


//        setFocusable(true);
//        setOutsideTouchable(true);
//        setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]-getHeight());

//        sound = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) voice_progress.getLayoutParams();
//        lp.width = sound * voice_bg.getHeight() / max;
//        voice_progress.setLayoutParams(lp);

    }

}
