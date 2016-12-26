package com.shower.ncf.shower.fragment;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dou361.ijkplayer.widget.IjkVideoView;
import com.shower.ncf.shower.R;
import com.shower.ncf.shower.VideoActivity;
import com.shower.ncf.shower.broadcastreceiver.MyMusicService;
import com.shower.ncf.shower.minfo.MInfo;
import com.shower.ncf.shower.util.MShared;
import com.shower.ncf.shower.util.MyUtil;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * Created by Administrator on 2016/12/2.
 */

public class FragmentVideo extends Fragment implements View.OnClickListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnCompletionListener {

    View contentView;
    IjkVideoView video_video;
    TextView video_name;
    TextView video_timeline;
    ProgressBar video_progress;

    //music btn
    TextView music_left;
    ImageView music_middle;
    TextView music_right;

    //
    LinearLayout video_ll;
    ImageView video_img;


    //list
    ArrayList<HashMap<String, String>> list_video;
    List<HashMap<String, String>> list_music;

    //
    int current = 0;
    boolean condition = false;
    String timeLine;
    long totalTime;

    //util
    MyUtil util;

    //handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    video_timeline.setText(timeLine);
                    video_progress.setMax(100);
                    int i =0;
                        i = (int) (video_video.getCurrentPosition() / (totalTime /  100));
                    video_progress.setProgress(i);
                    break;
            };
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_video_layout,container,false);

        initView();
        setMusic();
        return contentView;
    }

    private void initView(){
        video_name = (TextView) contentView.findViewById(R.id.video_name);
        video_timeline = (TextView) contentView.findViewById(R.id.video_timeline);
        video_progress = (ProgressBar) contentView.findViewById(R.id.video_progress);

        music_left = (TextView) contentView.findViewById(R.id.music_left);
        music_middle = (ImageView) contentView.findViewById(R.id.music_middle);
        music_right = (TextView) contentView.findViewById(R.id.music_right);

        video_ll = (LinearLayout) contentView.findViewById(R.id.vv_l);
        video_img = (ImageView) contentView.findViewById(R.id.video_img);

        video_ll.setOnClickListener(this);

        list_video = getFilm();

        setNewVideo();
        setVideo();
    }

    private void setMusicClick(){
        music_left.setOnClickListener(this);
        music_middle.setOnClickListener(this);
        music_right.setOnClickListener(this);

    }

    private void setVideo(){
        isSetPath = true;
       if (current == list_video.size()){
           video_video.pause();
           return;
       }

        HashMap<String, String> map = list_video.get(current);
        String name = map.get(projection[1]);
        video_name.setText(name);
        video_video.setVideoPath(map.get(projection[2]));
        Log.i("01010010","path = " + map.get(projection[2]));

        current++;
    }

    String []projection = { MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATA};

    private ArrayList<HashMap<String, String>> getFilm(){

        String orderBy = MediaStore.Video.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        return getContentProvider(uri,projection, orderBy);

    }


    public ArrayList<HashMap<String, String>> getContentProvider(Uri uri,String[] projection, String orderBy) {
        // TODO Auto-generated method stub
        ArrayList<HashMap<String, String>> listImage = new ArrayList<HashMap<String, String>>();
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null,
                null, orderBy);

        if (null == cursor) {
            return listImage;
        }
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            for(int i=0;i<projection.length;i++){
                map.put(projection[i], cursor.getString(i));
                System.out.println(projection[i]+":"+cursor.getString(i));
                Log.i("01010010"," string = " + cursor.getString(i));
            }
            String path = map.get(MediaStore.Video.Media.DATA);
            File file = new File(path);
            if (file.exists())
            listImage.add(map);
        }
        cursor.close();

        return listImage;
    }

    public void release(){
        video_video.stopPlayback();
    }

    public void replay(){
        video_video.start();
    }

    public void pauseMusic(){
        try{
            myMusicService.pause();
            Glide.with(this).load(R.drawable.music_middle).fitCenter().into(music_middle);
        }catch (Exception e){

        }
    }

    private void startTimer(final long totalTime){
        new Thread(){
            @Override
            public void run() {
                if (util == null){
                    util = new MyUtil();
                }
                while(condition){
                    timeLine = util.getVideoTime(video_video.getCurrentPosition(),totalTime);
                    FragmentVideo.this.totalTime = totalTime;
                    handler.sendEmptyMessage(1);
                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    Intent intentService;
    private void setMusic(){
        intentService = new Intent(getContext(), MyMusicService.class);
        getContext().startService(intentService);
        getContext().bindService(intentService,sc,getContext().BIND_AUTO_CREATE);

    }
    MyMusicService myMusicService;
    ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("01010010","service connected...............");
            myMusicService = ((MyMusicService.MyBinder)service).getService();
            myMusicService.setVideo_img(video_img);
            myMusicService.startMusic();
            setMusicClick();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    boolean isSetPath = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_left:
                myMusicService.upon();
                Glide.with(this).load(R.drawable.music_stop).fitCenter().into(music_middle);
            break;
            case R.id.music_middle:
                if (myMusicService.isPlaying()){
                    myMusicService.pause();
                    Glide.with(this).load(R.drawable.music_middle).fitCenter().into(music_middle);
                }else {
                    Glide.with(this).load(R.drawable.music_stop).fitCenter().into(music_middle);
                    myMusicService.start();
                }
                break;
            case R.id.music_right:
                Log.i("01010010","next................");
                myMusicService.next();
                Glide.with(this).load(R.drawable.music_stop).fitCenter().into(music_middle);
                break;
            case R.id.vv_l:
                Intent intent = new Intent(getContext(), VideoActivity.class);
                MInfo mInfo = new MInfo();
                mInfo.setArrayList(list_video);
                Bundle b = new Bundle();
                b.putSerializable("video_list",mInfo);
                intent.putExtra("video_list",b);
                intent.putExtra("current",current-1);
                intent.putExtra("seekTo",video_video.getCurrentPosition());
                startActivityForResult(intent,0);

                video_video.pause();
                myMusicService.pause();
                Glide.with(this).load(R.drawable.music_middle).fitCenter().into(music_middle);
                break;
        }
    }

    public void onEnd(){
        if (myMusicService != null){
            try{
                myMusicService.stop();
                getContext().stopService(intentService);
                getContext().unbindService(sc);
            }catch (Exception e){
            }

        }
        try {
            video_video.stopPlayback();
        }catch (Exception e){

        }

    }
    int seekTo = 0;
    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        condition = true;
        iMediaPlayer.start();
        iMediaPlayer.seekTo(seekTo);
        Log.i("01010010","start seek to= " + seekTo);
        getVideoPic();
        startTimer(iMediaPlayer.getDuration());
    }

    private void getVideoPic(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(500);
                    if (video_video!=null){
                        video_video.pause();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        condition = false;
        video_video.releaseWithoutStop();
        setNewVideo();
        setVideo();
        seekTo = 0;
        return true;
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        seekTo = 0;
        condition = false;
        video_video.releaseWithoutStop();
        setNewVideo();
        setVideo();
    }

    private void setNewVideo(){
        video_ll.removeAllViews();
        video_video = new IjkVideoView(getContext());
        video_ll.addView(video_video);

        video_video.setOnCompletionListener(this);
        video_video.setOnErrorListener(this);
        video_video.setOnPreparedListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        condition = false;
        if (data == null)
            return;
        Bundle b = data.getExtras();
        if (b == null)
            return;
        seekTo = b.getInt("seek",0);
        String path = b.getString("name","");

        Log.i("01010010","  seekto = " + seekTo  + " ; path = " + path);
        for (int i = 0;i < list_video.size();i++){
            if (path == null || "".equals(path))
                return;

            if (path.equals(list_video.get(i).get(MShared.Video_DATA))){
                current = i;
                break;
            }

        }
        setNewVideo();
        setVideo();
    }
}
