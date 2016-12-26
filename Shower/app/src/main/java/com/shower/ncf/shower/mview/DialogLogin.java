package com.shower.ncf.shower.mview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.shower.ncf.shower.R;

/**
 * Created by Administrator on 2016/11/30.
 */

public class DialogLogin extends Dialog implements View.OnClickListener {
    Context c;

    public DialogLogin(Context context) {
        super(context);
        this.c = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    View dialog_view;
    EditText textView_name;
    ImageView imageView_check;

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(c);
        dialog_view = inflater.inflate(R.layout.dialog_login,null);

        textView_name = (EditText) dialog_view.findViewById(R.id.dialog_login_name);
        imageView_check = (ImageView) dialog_view.findViewById(R.id.dialog_login_check);

        imageView_check.setOnClickListener(this);

        Window dialogWindow = getWindow();
        dialogWindow.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.dialog_login));
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = c.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.9); // 高度设置为屏幕的0.6
        lp.height = (int) (d.widthPixels * 0.3);
        dialogWindow.setAttributes(lp);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialog_view);

    }

    MLoginCallBack mCallBack;
    public void setOnCheckClickListener(MLoginCallBack mCallBack){
        this.mCallBack = mCallBack;
    }

    @Override
    public void onClick(View v) {
        mCallBack.callBack(textView_name.getText().toString());
        textView_name.setText("");
        dismiss();
    }

    public interface  MLoginCallBack{
        public void callBack(String name);
    }


}
