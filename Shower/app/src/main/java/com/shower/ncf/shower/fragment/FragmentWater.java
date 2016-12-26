package com.shower.ncf.shower.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shower.ncf.shower.R;
import com.shower.ncf.shower.mview.MTemperature;

/**
 * Created by Administrator on 2016/11/29.
 */

public class FragmentWater extends Fragment {
    //view
    View contentView;
    MTemperature mTemperatureView;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_water_layout,container,false);
        mTemperatureView = (MTemperature) contentView.findViewById(R.id.water_fragment_temp);
        return contentView;
    }



}
