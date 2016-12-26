package com.shower.ncf.shower.minfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/29.
 */

public class MInfo implements Serializable {

    public MInfo(){

    }

    private String login_name;

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }
//    private String login_name;

    private String light_name;
    private int light_color;
    private int light_img;


    public String getLight_name() {
        return light_name;
    }

    public void setLight_name(String light_name) {
        this.light_name = light_name;
    }


    public int getLight_color() {
        return light_color;
    }

    public void setLight_color(int light_color) {
        this.light_color = light_color;
    }

    public int getLight_img() {
        return light_img;
    }

    public void setLight_img(int light_img) {
        this.light_img = light_img;
    }

    private ArrayList<HashMap<String,String>> arrayList;

    public ArrayList<HashMap<String, String>> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<HashMap<String, String>> arrayList) {
        this.arrayList = arrayList;
    }
}
