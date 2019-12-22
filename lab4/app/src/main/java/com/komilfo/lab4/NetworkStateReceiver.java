package com.komilfo.lab4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStateReceiver extends BroadcastReceiver {
    private NetworkActivity activity;
    NetworkStateReceiver(NetworkActivity activity){
        this.activity = activity;
    }

    public void onReceive(Context context, Intent intent) {
        activity.onNetworkChange(getState());
    }

    public boolean getState(){
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo ni = manager.getActiveNetworkInfo();
        return ni != null && ni.getState() == NetworkInfo.State.CONNECTED;
    }
}