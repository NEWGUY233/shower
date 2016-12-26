package com.shower.ncf.shower;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shower.ncf.shower.fragment.FragmentLight;
import com.shower.ncf.shower.fragment.FragmentVideo;
import com.shower.ncf.shower.fragment.FragmentWater;
import com.shower.ncf.shower.minfo.MInfo;
import com.shower.ncf.shower.util.MyUtil;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/11/28.
 */

public class SettingActivity extends MyFragmentActivity implements View.OnClickListener {

    Context context;
    MyUtil util;

    //Fragment
    FragmentLight fragmentLight;
    FragmentWater fragmentWater;
    FragmentVideo fragmentVideo;

    FragmentTransaction ft;
    FragmentManager fm;

    //view
    TextView main_time;
    ImageView main_sound;
    TextView main_temp1;

    //底部按钮
    LinearLayout setting_light_lin;
    LinearLayout setting_water_lin;
    LinearLayout setting_stop_lin;
    LinearLayout setting_end_lin;
    LinearLayout setting_user_lin;
    LinearLayout setting_android_lin;

    ImageView setting_light_img;
    ImageView setting_water_img;
    ImageView setting_stop_img;
    ImageView setting_end_img;
    ImageView setting_user_img;
    ImageView setting_android_img;

    //右侧选项
    LinearLayout setting_right_first;
    LinearLayout setting_right_second;
    LinearLayout setting_right_third;

    TextView setting_first_text;
    TextView setting_second_text;
    TextView setting_third_text;



    //handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    main_time.setText(util.getTime());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shower_setting);

        initView();
        timeThread();
        setRightBtnType(4);
        initFragment();
    }

    private void initView(){
        context = this;
        main_time = (TextView) findViewById(R.id.main_time);
        main_sound = (ImageView) findViewById(R.id.main_sound);
        main_temp1 = (TextView) findViewById(R.id.main_temp1);

        setting_light_lin = (LinearLayout) findViewById(R.id.setting_light_lin);
        setting_light_img = (ImageView) findViewById(R.id.setting_light_img);

        setting_water_lin = (LinearLayout) findViewById(R.id.setting_water_lin);
        setting_water_img = (ImageView) findViewById(R.id.setting_water_img);

        setting_stop_lin = (LinearLayout) findViewById(R.id.setting_stop_lin);
        setting_stop_img = (ImageView) findViewById(R.id.setting_stop_img);

        setting_end_lin = (LinearLayout) findViewById(R.id.setting_end_lin);
        setting_end_img = (ImageView) findViewById(R.id.setting_end_img);

        setting_user_lin = (LinearLayout) findViewById(R.id.setting_user_lin);
        setting_user_img = (ImageView) findViewById(R.id.setting_user_img);

        setting_android_lin = (LinearLayout) findViewById(R.id.setting_android_lin);
        setting_android_img = (ImageView) findViewById(R.id.setting_android_img);

        //setting right
        setting_right_first = (LinearLayout) findViewById(R.id.setting_right_first);
        setting_right_second = (LinearLayout) findViewById(R.id.setting_right_second);
        setting_right_third = (LinearLayout) findViewById(R.id.setting_right_third);

        setting_first_text = (TextView) findViewById(R.id.setting_first_text);
        setting_second_text = (TextView) findViewById(R.id.setting_second_text);
        setting_third_text = (TextView) findViewById(R.id.setting_third_text);

        //监听
        setting_light_lin.setOnClickListener(this);
        setting_water_lin.setOnClickListener(this);
        setting_stop_lin.setOnClickListener(this);
        setting_end_lin.setOnClickListener(this);
        setting_user_lin.setOnClickListener(this);
        setting_android_lin.setOnClickListener(this);

        setting_right_first.setOnClickListener(this);
        setting_right_second.setOnClickListener(this);
        setting_right_third.setOnClickListener(this);

        main_sound.setOnClickListener(this);

        util = new MyUtil();
        main_time.setText(util.getTime());

    }

    private void initFragment(){
        //fragment
        fragmentLight = new FragmentLight();
        fragmentWater = new FragmentWater();
        fragmentVideo = new FragmentVideo();

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
//        ft.add(R.id.setting_fragment,fragmentLight);
//        ft.add(R.id.setting_fragment,fragmentWater);
        ft.add(R.id.setting_player,fragmentVideo);
//        ft.hide(fragmentLight);
//        ft.hide(fragmentWater);
        ft.commit();

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

    private void setTypeOfBtn(int i){
        setting_light_lin.setBackgroundColor(0x0fff);
        setting_light_img.setImageResource(R.drawable.setting_light_off);
        setting_water_lin.setBackgroundColor(0x0fff);
        setting_water_img.setImageResource(R.drawable.setting_water_off);
        setting_stop_lin.setBackgroundColor(0x0fff);
        setting_stop_img.setImageResource(R.drawable.setting_stop_off);
        setting_end_lin.setBackgroundColor(0x0fff);
        setting_end_img.setImageResource(R.drawable.setting_end_off);
        setting_user_lin.setBackgroundColor(0x0fff);
//        setting_user_img.setImageResource(R.drawable.setting_user_off);
        setting_android_lin.setBackgroundColor(0x0fff);
//        setting_android_img.setImageResource(R.drawable.setting_android_off);
        if(fm == null){
            fm = getSupportFragmentManager();
        }
        ft = fm.beginTransaction();
        switch (i){
            case 1:
                setting_light_lin.setBackground(getResources().getDrawable(R.drawable.setting_choose));
                setting_light_img.setImageResource(R.drawable.setting_light_on);
                if (!fragmentLight.isAdded()){
                    ft.add(R.id.setting_fragment,fragmentLight);
                }
                if (fragmentWater.isAdded()){
                    ft.hide(fragmentWater);
                }
                ft.show(fragmentLight);
                break;
            case 2:
                setting_water_lin.setBackground(getResources().getDrawable(R.drawable.setting_choose));
                setting_water_img.setImageResource(R.drawable.setting_water_on);
                if (!fragmentWater.isAdded()){
                    ft.add(R.id.setting_fragment,fragmentWater);
                }
                if (fragmentLight.isAdded()){
                    ft.hide(fragmentLight);
                }
                ft.show(fragmentWater);
                break;
            case 3:
                setting_stop_lin.setBackground(getResources().getDrawable(R.drawable.setting_choose));
                setting_stop_img.setImageResource(R.drawable.setting_stop_on);
                break;
            case 4:
                setting_end_lin.setBackground(getResources().getDrawable(R.drawable.setting_choose));
                setting_end_img.setImageResource(R.drawable.setting_end_on);
                break;
            case 5: break;
            case 6: break;

        }
        ft.commit();
    }

    boolean setting_right = false;

    public void setRightBtnType(int type){
        setting_right_first.setBackground(getResources().getDrawable(R.drawable.setting_first_base));
        setting_right_second.setBackground(getResources().getDrawable(R.drawable.setting_second_base));
        setting_right_third.setBackground(getResources().getDrawable(R.drawable.setting_third_base));

        setting_first_text.setText("梦幻雨林");
        setting_first_text.setTextColor(getResources().getColor(R.color.setting_right_text_base));

        setting_second_text.setText("激情瀑布");
        setting_second_text.setTextColor(getResources().getColor(R.color.setting_right_text_base));

        setting_third_text.setText("舒缓溪流");
        setting_third_text.setTextColor(getResources().getColor(R.color.setting_right_text_base));

        switch (type){
            case 1:
                if (!setting_right){
                    setting_first_text.setText("梦幻雨林");
                    setting_right_first.setBackground(getResources().getDrawable(R.drawable.setting_first_click_1));
                }else {
                    setting_first_text.setText("花洒模式");
                    setting_right_first.setBackground(getResources().getDrawable(R.drawable.setting_first_click_2));
                }
                setting_right = !setting_right;
                setting_first_text.setTextColor(getResources().getColor(R.color.setting_right_text_click));
                break;
            case 2:
                setting_right = false;
                setting_right_second.setBackground(getResources().getDrawable(R.drawable.setting_second_click));
                setting_second_text.setTextColor(getResources().getColor(R.color.setting_right_text_click));
                break;
            case 3:
                setting_right = false;
                setting_third_text.setTextColor(getResources().getColor(R.color.setting_right_text_click));
                setting_right_third.setBackground(getResources().getDrawable(R.drawable.setting_third_click));
            break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_light_lin:
                setTypeOfBtn(1);
                break;
            case R.id.setting_water_lin:
                setTypeOfBtn(2);
                break;
            case R.id.setting_stop_lin:
                setTypeOfBtn(3);
                break;
            case R.id.setting_end_lin:
                setTypeOfBtn(4);
                break;
            case R.id.setting_user_lin:
                setTypeOfBtn(5);
                Intent intent1 = new Intent(SettingActivity.this,LoginActivity.class);
                fragmentVideo.release();
                startActivity(intent1);
                break;
            case R.id.setting_android_lin:
                setTypeOfBtn(6);
                Intent intent = new Intent(Intent.ACTION_MAIN,null);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                break;
            case R.id.setting_right_first:
                setRightBtnType(1);
                break;
            case R.id.setting_right_second:
                setRightBtnType(2);
                break;
            case R.id.setting_right_third:
                setRightBtnType(3);
                break;

        }


    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            fragmentVideo.onEnd();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fragmentVideo != null)
        fragmentVideo.pauseMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        fragmentVideo.onActivityResult(requestCode,resultCode,data);
    }
}

