package com.shower.ncf.shower.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shower.ncf.shower.R;
import com.shower.ncf.shower.minfo.MInfo;
import com.shower.ncf.shower.mview.MLinearLayout;

import java.util.ArrayList;


public class FragmentLight extends Fragment implements MLinearLayout.OnLightItemClickListener {
    //view
    View contentView;
    MLinearLayout light_ml;
    ImageView light_img;
//    LinearLayout light_ml;
    ArrayList<MInfo> light_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_light_layout,container,false);
        initData();
        init();
        return contentView;
    }

    private void initData(){
        light_list = new ArrayList<MInfo>();
        MInfo info = new MInfo();
        info.setLight_name("淡橙");
        info.setLight_color(getResources().getColor(R.color.light_color_orange));
        info.setLight_img(R.drawable.light_img_orange);
        light_list.add(info);

        info = new MInfo();
        info.setLight_name("淡蓝");
        info.setLight_color(getResources().getColor(R.color.light_color_blue));
        info.setLight_img(R.drawable.light_img_blue);
        light_list.add(info);

        info = new MInfo();
        info.setLight_name("淡绿");
        info.setLight_color(getResources().getColor(R.color.light_color_green));
        info.setLight_img(R.drawable.light_img_green);
        light_list.add(info);
    }

    private void init(){
        light_img = (ImageView) contentView.findViewById(R.id.light_img);
        light_ml = (MLinearLayout) contentView.findViewById(R.id.light_ml);
        light_ml.setListener(this);
        light_ml.addTextView(light_list);
    }


    @Override
    public void onLightItemClick(int position, View v, MInfo info) {
        Log.i("00000","light num = " + position + "name =  " + info.getLight_name());
        light_img.setImageResource(info.getLight_img());
    }
}
