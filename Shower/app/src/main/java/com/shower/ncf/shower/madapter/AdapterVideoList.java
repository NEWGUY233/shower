package com.shower.ncf.shower.madapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shower.ncf.shower.R;
import com.shower.ncf.shower.util.MShared;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/19.
 */

public class AdapterVideoList extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String,String>> list_video;
    Holder holder;


    public AdapterVideoList(Context context,ArrayList<HashMap<String,String>> list_video){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list_video = list_video;
        if (this.list_video == null)
            this.list_video = new ArrayList<HashMap<String,String>>();
    }

    @Override
    public int getCount() {
        return list_video.size();
    }

    @Override
    public Object getItem(int position) {
        return list_video.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || convertView.getTag() == null){
            holder = new Holder();
            convertView =  inflater.inflate(R.layout.video_list_item,null);
            holder.video_name = (TextView) convertView.findViewById(R.id.video_list_item);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        HashMap<String,String> map = list_video.get(position);
        holder.video_name.setText(map.get(MShared.Video_NAME));
        if (position == current){
            holder.video_name.setBackgroundColor(context.getResources().getColor(R.color.video_list_item));
        }else {
            holder.video_name.setBackgroundColor(context.getResources().getColor(R.color.transparency));
        }
        return convertView;
    }

    public class Holder{
        TextView video_name;
    }


    int current = 0;
    public void setCurrent(int current){
        this.current = current;
    }

}
