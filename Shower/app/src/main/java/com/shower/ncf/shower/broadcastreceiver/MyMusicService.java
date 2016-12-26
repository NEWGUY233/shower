package com.shower.ncf.shower.broadcastreceiver;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shower.ncf.shower.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */

public class MyMusicService extends Service implements MediaPlayer.OnCompletionListener {
    private List<HashMap<String, String>> path;
    public MediaPlayer mp;

    public final IBinder binder = new MyBinder();

    private int position = 0;

    private ImageView video_img;

    public void setVideo_img(ImageView video_img) {
        this.video_img = video_img;
    }


    public class MyBinder extends Binder {
        public MyMusicService getService() {
            return MyMusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        path = getMusic();
        mp = new MediaPlayer();
        mp.setOnCompletionListener(this);
        return binder;
    }

    String[] projection1 = { MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATA ,
            MediaStore.Audio.Media.ALBUM_ID };

    private  List<HashMap<String, String>> getMusic(){
        String orderBy = MediaStore.Audio.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return getContentProvider(uri,projection1, orderBy);
    }


    public List<HashMap<String, String>> getContentProvider(Uri uri,String[] projection, String orderBy) {
        // TODO Auto-generated method stub
        List<HashMap<String, String>> listImage = new ArrayList<HashMap<String, String>>();
        Cursor cursor = getContentResolver().query(uri, projection, null,
                null, orderBy);

        if (null == cursor) {
            return listImage;
        }
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            for(int i=0;i<projection.length;i++){
                map.put(projection[i], cursor.getString(i));
                System.out.println(projection[i]+":"+cursor.getString(i));
            }
            listImage.add(map);
        }
        cursor.close();

        return listImage;
    }

    private String getAlbumArt(String album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur = this.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + album_id),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        return album_art;
    }

    public void startMusic(){
        try {
            mp.reset();
            Log.i("01010010","path = " + path.get(position).get(projection1[2]));
            String pic = getAlbumArt(path.get(position).get(projection1[3]));
            Log.i("01010010","pic = " + pic);
            if (pic != null && !"".equals(pic))
            Glide.with(this).load(pic).fitCenter().into(video_img);
            else
                Glide.with(this).load(R.color.Grey_500).fitCenter().into(video_img);
            mp.setDataSource(path.get(position).get(projection1[2]));
            mp.prepare();
            mp.start();
            mp.pause();
        } catch (IOException e) {
            Log.i("01010010",".........IOException ");
            if (position <= path.size()){
                position++;
                startMusic();
            }
            e.printStackTrace();
        }catch (Exception e){

        }
    }

    public void start(){
        mp.start();
    }

    public boolean isPlaying(){
        return mp.isPlaying();
    }

    public void pause(){
        mp.pause();
    }

    public void next(){
        try {
            mp.reset();
//            FileInputStream fis = null;
            position++;
            if (position >= path.size())
                position = 0;
            mp.setDataSource(path.get(position).get(projection1[2]));
            mp.prepare();
            mp.seekTo(0);
            start();
            String pic = getAlbumArt(path.get(position).get(projection1[3]));
            Log.i("01010010","pic = " + pic);
            if (pic != null && !"".equals(pic))
                Glide.with(this).load(pic).fitCenter().into(video_img);
            else
                Glide.with(this).load(R.color.Grey_500).fitCenter().into(video_img);
        } catch (IOException e) {
            if (position <= path.size()){
                next();
            }
            e.printStackTrace();
        }

    }

    public void upon(){
        try {
            mp.reset();
//            FileInputStream fis = null;
            position--;
            if (position < 0)
                position = path.size()-1;
            mp.setDataSource(path.get(position).get(projection1[2]));
            mp.prepare();
            mp.seekTo(0);
            start();
            String pic = getAlbumArt(path.get(position).get(projection1[3]));
            Log.i("01010010","pic = " + pic);
            if (pic != null && !"".equals(pic))
                Glide.with(this).load(pic).fitCenter().into(video_img);
            else
                Glide.with(this).load(R.color.Grey_500).fitCenter().into(video_img);
        } catch (IOException e) {
            if (position <= path.size()){
                upon();
            }
            e.printStackTrace();
        }
    }


    public void stop(){
        mp.stop();
        mp.release();
    }



    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i("01010010", "onCompletion: done???????");
        next();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
