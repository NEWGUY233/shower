package com.shower.ncf.shower.mview;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shower.ncf.shower.R;

/**
 * Created by Administrator on 2016/12/23.
 */

public class MVoiceControl extends RelativeLayout {


    public MVoiceControl(Context context) {
        super(context);
        initView(context);
    }

    public MVoiceControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public MVoiceControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        sound = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) voice_progress.getLayoutParams();
        lp.height = sound * h / max;
        voice_progress.setLayoutParams(lp);
    }

    AudioManager audio ;

    int sound;
    int max;

    RelativeLayout include;
    ImageView voice_progress;
    ImageView video_thumb;
    ImageView voice_bg;
    View contentView;

    void initView(Context c){
        contentView = LayoutInflater.from(c).inflate(R.layout.view_video_voice,null);
        voice_progress = (ImageView) contentView.findViewById(R.id.voice_second);
        video_thumb = (ImageView) contentView.findViewById(R.id.video_thumb);
        voice_bg = (ImageView) contentView.findViewById(R.id.voice_bg);

        audio = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        max = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        addView(contentView);
    }


    float downY = 0;
    float moveY = 0;
    float upY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
        voice_progress.setLayoutParams(lp);

        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) video_thumb.getLayoutParams();
        lp1.bottomMargin = voice_progress.getHeight() - video_thumb.getHeight()/2;


        video_thumb.setLayoutParams(lp1);

//        int i = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        audio.setStreamVolume(AudioManager.STREAM_MUSIC,
                max *voice_progress.getHeight()/voice_bg.getHeight() ,0);
    }
}
