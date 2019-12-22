package com.komilfo.lab4;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

class ChannelHistory {
    private SharedPreferences preferences;
    private Set<String> defaultSet;

    ChannelHistory(Activity activity){
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        defaultSet = new HashSet<>();
        defaultSet.add(activity.getResources().getString(R.string.default_url));
    }

    Set<String> get(){
        return preferences.getStringSet("history", defaultSet);
    }

    void put(String url){
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> set = get();
        set.add(url);
        if (set.size() > 5)
            set.remove(set.iterator().next());
        editor.putStringSet("history", set);
        editor.apply();
    }
}