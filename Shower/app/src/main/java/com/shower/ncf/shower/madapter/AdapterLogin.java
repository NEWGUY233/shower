package com.shower.ncf.shower.madapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shower.ncf.shower.R;
import com.shower.ncf.shower.minfo.MInfo;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/11/30.
 */

public class AdapterLogin extends BaseAdapter {
    Context c;
    ArrayList<MInfo> list;
    Holder holder;
    public AdapterLogin(Context c){
        this.c = c;
    }

    public AdapterLogin(Context c, ArrayList<MInfo> list){
        this.c = c;
        this.list = list;
        if (list == null)
            this.list = new ArrayList<MInfo>();
    }

    @Override
    public int getCount() {
        return list.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || convertView.getTag() == null){
            holder = new Holder();
            convertView =  LayoutInflater.from(c).inflate(R.layout.item_login,null);
            holder.name = (TextView) convertView.findViewById(R.id.item_login_text);
            holder.img = (ImageView) convertView.findViewById(R.id.item_login_img);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        if (list.size() != position){
            holder.name.setText(list.get(position).getLogin_name());
            holder.img.setImageResource(R.drawable.login_list_item_arrow);
        }else {
            holder.name.setText("添加用户");
            holder.img.setImageResource(R.drawable.login_list_item_add);
        }

        return convertView;
    }

    class Holder{
        TextView name;
        ImageView img;
    }
}
