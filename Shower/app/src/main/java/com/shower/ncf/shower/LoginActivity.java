package com.shower.ncf.shower;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.VideoView;

import com.dou361.ijkplayer.widget.IjkVideoView;
import com.shower.ncf.shower.madapter.AdapterLogin;
import com.shower.ncf.shower.minfo.MInfo;
import com.shower.ncf.shower.mview.DialogLogin;

import java.util.ArrayList;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * Created by Administrator on 2016/12/5.
 */

public class LoginActivity extends MyActivity implements AdapterView.OnItemClickListener, DialogLogin.MLoginCallBack, DialogInterface.OnDismissListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnErrorListener {

    IjkVideoView login_vv;
    ListView login_list;
    ImageView login_img;

    //List
    ArrayList<MInfo> list_login;
    MInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initFilm();;
    }

    private void initView(){
        login_vv = (IjkVideoView) findViewById(R.id.login_vv);
        login_list = (ListView) findViewById(R.id.login_list);
        login_img = (ImageView) findViewById(R.id.login_bg);
    }

    private void initFilm(){
        login_vv.setVideoPath(getFilesDir().getPath() + "/login.mov");
        login_vv.setOnPreparedListener(this);
        login_vv.setOnErrorListener(this);
        login_vv.setOnCompletionListener(this);

        login_list.setOnItemClickListener(this);
        getLoginList();

    }

    private void getLoginList(){
        list_login = new ArrayList<MInfo>();
        mInfo = new MInfo();
        mInfo.setLogin_name("主人");
        list_login.add(mInfo);
        mInfo = new MInfo();
        mInfo.setLogin_name("客人");
        list_login.add(mInfo);
        login_list.setAdapter(new AdapterLogin(this,list_login));
    }

//    @Override
//    public void onPrepared(MediaPlayer mp) {
//        login_img.setVisibility(View.GONE);
//    }
//
//    @Override
//    public boolean onError(MediaPlayer mp, int what, int extra) {
//        return false;
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        login_vv.seekTo(0);
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < list_login.size()){
            login_vv.stopPlayback();
            finish();
            return;
        }
        DialogLogin dialogLogin = new DialogLogin(this);
        dialogLogin.setOnCheckClickListener(this);
        dialogLogin.setOnDismissListener(this);
        dialogLogin.show();
    }

    @Override
    public void callBack(String name) {
        if (name == null || "".equals(name)){
            return;
        }

        MInfo info = new MInfo();
        info.setLogin_name(name);
        list_login.add(info);
        ((AdapterLogin)login_list.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        login_list.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        login_vv.stopPlayback();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            login_vv.stopPlayback();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        login_vv.seekTo(0);
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        login_img.setVisibility(View.GONE);
        iMediaPlayer.start();
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }
}
