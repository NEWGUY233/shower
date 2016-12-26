package com.shower.ncf.shower.mview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shower.ncf.shower.R;
import com.shower.ncf.shower.minfo.MInfo;

import java.util.ArrayList;

//setting界面  light  fragment 里面的导航栏
public class MLinearLayout extends LinearLayout {
    ArrayList<MInfo> list;
    LayoutInflater inflater;
    OnLightItemClickListener listener;

    public MLinearLayout(Context context) {
        super(context);
    }
    public MLinearLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs, 0);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void addTextView(ArrayList<MInfo> list) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (list == null || list.size() == 0) {
            return;
        }
        this.list = list;
        for (int i = 0; i < list.size(); i++) {
            View v = inflater.inflate(R.layout.item_light_fragment, null);
            TextView line = (TextView) v.findViewById(R.id.item_light_line);
            TextView text = (TextView) v.findViewById(R.id.item_light_text);
            line.setBackgroundColor(getResources().getColor(R.color.light_color_nor));
            text.setTextColor(getResources().getColor(R.color.light_color_nor));
            text.setText(list.get(i).getLight_name());
            v.setTag(i);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("00000", " id = " + v.getTag());
                    MInfo info = MLinearLayout.this.list.get((Integer) v.getTag());
                    TextView line = (TextView) v.findViewById(R.id.item_light_line);
                    TextView text = (TextView) v.findViewById(R.id.item_light_text);
                    line.setBackgroundColor(info.getLight_color());
                    text.setTextColor(info.getLight_color());
                    text.setText(info.getLight_name());
                    setItemBackToGrey((Integer) v.getTag());
                    if (listener != null){
                        listener.onLightItemClick((Integer) v.getTag(),v,info);
                    }
                }
            });
            addView(v, i);

        }
    }

    private void setItemBackToGrey(int position){
       int childCount =  getChildCount();
        for (int i = 0 ; i < childCount ; i++){
            if ( i == position)
                continue;
            View v = getChildAt(i);
            TextView line = (TextView) v.findViewById(R.id.item_light_line);
            TextView text = (TextView) v.findViewById(R.id.item_light_text);
            line.setBackgroundColor(getResources().getColor(R.color.light_color_nor));
            text.setTextColor(getResources().getColor(R.color.light_color_nor));
            text.setText(list.get(i).getLight_name());

        }

//        getChildAt()
    }


    public void setListener(OnLightItemClickListener listener){
        this.listener = listener;
    }

    public interface OnLightItemClickListener{
        void onLightItemClick(int position,View v,MInfo info);
    }
}
