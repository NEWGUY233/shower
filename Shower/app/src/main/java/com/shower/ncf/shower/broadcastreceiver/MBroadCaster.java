package com.shower.ncf.shower.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.shower.ncf.shower.WelcomeActivity;

/**
 * Created by Administrator on 2016/12/2.
 */

public class MBroadCaster extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {     // boot
            Intent intent2 = new Intent(context, WelcomeActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
        }

//        if (intent.getAction().equals(ACTION)) {
//            Intent mainIntent = new Intent(context, MainActivity.class);
//            mainIntent.putExtra("auto", true);
//            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            context.startActivity(mainIntent);
//            Toast.makeText(context, "开机启动完成!", Toast.LENGTH_LONG).show();
//        }
    }
}
