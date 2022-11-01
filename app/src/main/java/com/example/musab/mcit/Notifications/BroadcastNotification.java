package com.example.musab.mcit.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by MUSAB on 9/9/2017.
 */

public class BroadcastNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle=intent.getExtras();
        if(intent.getAction().equalsIgnoreCase("com.example.Broadcast")){
            //  Toast.makeText(context,bundle.getString("msg"),Toast.LENGTH_SHORT).show();
            NewMessageNotification myNotfiy=new NewMessageNotification();
            myNotfiy.notify(context,bundle.getString("msgTitle"),
                                    bundle.getString("msgType"),
                                    bundle.getString("msgContent"),
                                                                123);
        }else if(intent.getAction().equalsIgnoreCase("com.example.Vote")){
            NewMessageNotification myNotfiy=new NewMessageNotification();
            myNotfiy.notify(context,bundle.getString("msgTitle"),
                    bundle.getString("msgType"),
                    bundle.getString("msgContent"),
                                              124);
        }
        // Toast.makeText(context,bundle.getString("msg"),Toast.LENGTH_SHORT).show();
    }
}
