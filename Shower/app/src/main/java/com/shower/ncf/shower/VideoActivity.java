package com.shower.ncf.shower;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dou361.ijkplayer.widget.IjkVideoView;
import com.shower.ncf.shower.madapter.AdapterVideoList;
import com.shower.ncf.shower.minfo.MInfo;
import com.shower.ncf.shower.mview.MyVideoPop;
import com.shower.ncf.shower.util.MShared;
import com.shower.ncf.shower.util.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Administrator on 2016/12/16.
 */

public class VideoActivity extends MyActivity implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, View.OnClickListener, AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener {
    ArrayList<HashMap<String,String>> list_video;
    int current = 0;
    int seekTo = 0;

    MyThread myThread;
    boolean isPlaying = true;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    notifyProgress();
                    video_time.setText(new MyUtil().getVideoTime(videoView.getCurrentPosition(),totalTime));
                    break;
                case 1:
                    video_seek.setMax(100);
                    video_time.setText("00:00:00/" + new MyUtil().changeTime(totalTime));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        getVideo();
        initView();
    }

    private void getVideo(){
        Intent intent = getIntent();
        if (intent == null){
            list_video = new ArrayList<HashMap<String,String>>();
            return;
        }
        current = intent.getIntExtra("current",0);
        seekTo = intent.getIntExtra("seekTo",0);
        Bundle b = intent.getBundleExtra("video_list");
        if (b == null){
            return;
        }
        MInfo mInfo = (MInfo) b.getSerializable("video_list");
        if (mInfo == null){
            return;
        }
        list_video = mInfo.getArrayList();
//        list_video = intent.getSerializableExtra("video_list")).getArrayList();
//        list_video = intent.;

    }

    //View
    LinearLayout video_ll;
    ProgressBar video_pro;
    SeekBar video_seek;

    TextView video_time;
    ImageView video_star;
    RelativeLayout video_img;
    ImageView video_full;
    ImageView video_menu;
    ImageView video_volume;

    ListView video_list;

    private void initView(){
        video_ll = (LinearLayout) findViewById(R.id.video_ll);
        video_pro = (ProgressBar) findViewById(R.id.video_waiting);
        video_seek = (SeekBar) findViewById(R.id.video_seek);
        video_time = (TextView) findViewById(R.id.video_time);
        video_star = (ImageView) findViewById(R.id.video_stop_start);
        video_full = (ImageView) findViewById(R.id.video_full);
        video_menu = (ImageView) findViewById(R.id.video_menu);
        video_volume = (ImageView) findViewById(R.id.video_volume);

        video_list = (ListView) findViewById(R.id.video_list);

        video_list.setVisibility(View.GONE);

        video_img = (RelativeLayout) findViewById(R.id.video_img);

        setViewListener();

        initVideo();
        setVideoPath(current);
    }

    private void setViewListener(){
        video_img.setOnClickListener(this);
        video_full.setOnClickListener(this);
        video_menu.setOnClickListener(this);
        video_volume.setOnClickListener(this);

        video_list.setOnItemClickListener(this);
    }

    //Adapter
    AdapterVideoList adapterVideoList;
    MyVideoPop pop;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.video_img:
                if (isPlaying){
                    Glide.with(this).load(R.drawable.video_start).into(video_star);
                    videoView.pause();
                }else {
                    Glide.with(this).load(R.drawable.video_stop).into(video_star);
                    videoView.start();
                }
                isPlaying = !isPlaying;
                break;
            case R.id.video_full:
                if (current >= 0){
                    Bundle b  = new Bundle();
                    b.putString("name",list_video.get(current).get(MShared.Video_DATA));
                    b.putInt("seek", videoView.getCurrentPosition());
                    Intent intent = new Intent();
                    intent.putExtras(b);
                    setResult(0,intent);
                }
                finish();
                break;
            case R.id.video_menu:
                if (video_list.getVisibility() == View.VISIBLE){
                    video_list.setVisibility(View.GONE);
                }else {
                    if (adapterVideoList == null){
                        adapterVideoList = new AdapterVideoList(this,list_video);
                        adapterVideoList.setCurrent(current);
                        video_list.setAdapter(adapterVideoList);
                    }

                    video_list.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.video_volume:
                if (pop == null)
                    pop = new MyVideoPop(this);

                if (pop.isShowing()){
                    pop.dismiss();
                }else{
                    pop.showPopUp(video_volume);
                }



                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (videoView !=null)
            videoView.stopPlayback();
        Glide.with(this).load(R.drawable.video_stop).into(video_star);
        initVideo();
        current = position;
        setVideoPath(current);
        video_list.setVisibility(View.GONE);
    }

    //view
    IjkVideoView videoView;
    private void initVideo(){
        video_ll.removeAllViews();

        videoView = new IjkVideoView(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        videoView.setOnErrorListener(this);

        video_ll.addView(videoView);
    }


    //
    long totalTime;
    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        totalTime = iMediaPlayer.getDuration();
        handler.sendEmptyMessage(1);
        video_pro.setVisibility(View.GONE);
        iMediaPlayer.start();
        iMediaPlayer.seekTo(seekTo);
        seekTo = 0;
        try {
            myThread = new MyThread(handler);
            myThread.start();
        }catch (Exception e){

        }
        isPlaying = true;
        Glide.with(this).load(R.drawable.video_stop).into(video_star);

        video_seek.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        myThread.setFinished(true);
        current++;
        seekTo = 0;
        initVideo();
        setVideoPath(current);
        isPlaying = false;
        Glide.with(this).load(R.drawable.video_start).into(video_star);
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        myThread.setFinished(true);
        current++;
        seekTo = 0;
        initVideo();
        setVideoPath(current);
        isPlaying = false;
        Glide.with(this).load(R.drawable.video_start).into(video_star);
        if (adapterVideoList != null){
            adapterVideoList.setCurrent(-1);
            adapterVideoList.notifyDataSetChanged();
        }
        return true;
    }

    private void setVideoPath(int current){
        if (current >= list_video.size()){
            if (adapterVideoList != null){
                adapterVideoList.setCurrent(-1);
                adapterVideoList.notifyDataSetChanged();
            }
            return;
        }
        if (adapterVideoList != null){
            adapterVideoList.setCurrent(current);
            adapterVideoList.notifyDataSetChanged();
        }
        video_pro.setVisibility(View.VISIBLE);
        String path = list_video.get(current).get(MShared.Video_DATA);
        videoView.setVideoPath(path);
    }

    //message = 0
    private void notifyProgress(){
        int i =0;
        try {
            i = (int) (videoView.getCurrentPosition() / (videoView.getDuration() /  100));
        }catch (Exception e){

        }
        video_seek.setProgress(i);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.i("01010010","onStartTrackingTouch");
        myThread.setFinished(true);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.i("01010010","onStopTrackingTouch");
//        seekBar.getProgress()
        videoView.seekTo(videoView.getDuration()/100*seekBar.getProgress());
        myThread = new MyThread(handler);
        myThread.start();
    }

    class MyThread extends Thread{
        Handler handler;
        public MyThread(Handler handler){
            this.handler = handler;
        }
        @Override
        public void run() {
            while (!isFinished){
                handler.sendEmptyMessage(0);
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        private boolean isFinished = false;
        public void setFinished(boolean isFinished){
            this.isFinished = isFinished;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        myThread.setFinished(true);

        if (videoView!=null)
            videoView.stopPlayback();
        if (video_ll!=null)
            video_ll.removeAllViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getRepeatCount() == 0) {
//            if (current >= 0){
//                Bundle b  = new Bundle();
//                b.putString("name",list_video.get(current).get(MShared.Video_DATA));
//                b.putInt("seek", videoView.getCurrentPosition());
//                Intent intent = new Intent();
//                intent.putExtras(b);
//                setResult(0,intent);
//            }
//            finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
