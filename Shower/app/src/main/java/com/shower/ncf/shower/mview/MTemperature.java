package com.shower.ncf.shower.mview;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shower.ncf.shower.R;



public class MTemperature extends LinearLayout implements View.OnTouchListener {
    LayoutInflater inflater;
    View contentView;
    ImageView temp_btn;
    ImageView temp_img;

    public MTemperature(Context context) {
        super(context);
        initView();
    }

    public MTemperature(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public MTemperature(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView(){
        inflater = LayoutInflater.from(getContext());
        contentView = inflater.inflate(R.layout.water_temp_view,null);
        temp_btn = (ImageView) contentView.findViewById(R.id.btn);
        temp_img = (ImageView) contentView.findViewById(R.id.img);
        addView(contentView);

        contentView.setOnTouchListener(this);
    }

    float downY = 0;
    float moveY = 0;
    float upY = 0;
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
//                changeLocation(0);
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = event.getY();
                changeLocation(downY,moveY);
                downY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                upY = event.getY();
                changeLocation(downY,upY);
                break;
        }


        return true;
    }

    private void changeLocation(float downY,float moveY){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) temp_btn.getLayoutParams();

        int raw = 0;
        if (moveY == downY){
            raw = (int) (downY - temp_btn.getHeight()/2);
            Log.i("00000","up : " + "moveY = " + moveY + " ;down = " + downY);
        }else {
            raw = (int) (moveY - downY);
            raw = params.topMargin + raw;

            Log.i("00000","move : " + "moveY = " + moveY + " ;down = " + downY);
        }
        if (raw >= getImgHeight()){
            raw = (int) getImgHeight();
        }else if(raw <= 0){
            raw = 0;
        }
        Log.i("00000","raw = " + raw);
        params.topMargin = raw;
        temp_btn.requestLayout();
        Log.i("00000","temp = " + getTemp());

    }

    private float getImgHeight(){
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) temp_img.getLayoutParams();
        //UI不对称，后面减去的值是误差值
        float height = temp_img.getHeight() - 10;
//        Log.i("00000","temp_img height = " + height);
        return height;
    }

    public void seekTo(int temp){
//        if (temp > 100 || temp < 0){
//            return;
//        }
//
//        int w = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        temp_img.measure(w, h);
//        int height = temp_img.getMeasuredHeight();
//
//        Log.i("00000","temp_img = " + height);
//        float t = height * (100 - temp) / 100;
//
//        changeLocation(t,t);
    }

    public int getTemp(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) temp_btn.getLayoutParams();
        //当前进度
        int j = (contentView.getHeight() - params.topMargin - temp_btn.getHeight()) *100;
        //总进度
        int k = contentView.getHeight() - temp_btn.getHeight();
        if (j/k > 100){
            return 100;
        }else if (j/k < 0){
            return 0;
        }
        return  j/k;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
