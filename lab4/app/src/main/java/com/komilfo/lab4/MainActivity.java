package com.komilfo.lab4;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class MainActivity extends NetworkActivity {
    ListView articlesView;
    TextView feedName;
    AutoCompleteTextView rssEdit;
    SwipeRefreshLayout refreshLayout;
    Feed feed;
    ChannelHistory history;
    ImageButton rssButton;
    boolean focus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        articlesView = findViewById(R.id.list_view);

        articlesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                Article article = (Article) articlesView.getAdapter().getItem(position);
                intent.putExtra("Link", article.getLink());
                startActivity(intent);
            }
        });
        refreshLayout = findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFeed();
            }
        });
        refreshLayout.setRefreshing(true);

        history = new ChannelHistory(this);
        feedName = findViewById(R.id.rss_name);
        rssEdit = findViewById(R.id.rss_edit);
        rssEdit.setText(rssurl.get());
        rssEdit.setAdapter(new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                new ArrayList<>(history.get())));

        rssEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm == null)
                    return;
                focus = hasFocus;
                if(hasFocus){
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }else{
                    imm.hideSoftInputFromWindow(articlesView.getWindowToken(), 0);
                    apply();
                }
            }
        });
        loadFeed();
        rssButton = findViewById(R.id.rss_button);
        rssEdit.clearFocus();
    }

    @Override
    void onGetFeed(Feed feed){
        this.feed = feed;
        feedName.setText(feed.getTitle());
        rssurl.set(rssEdit.getText().toString());
        history.put(rssEdit.getText().toString());
        rssEdit.setAdapter(new ArrayAdapter<>(this
                , R.layout.support_simple_spinner_dropdown_item
                , new ArrayList<>(history.get())));
        int width = articlesView.getWidth();
        articlesView.setAdapter(new ArticleAdapter(
                this, R.layout.feed_layout, feed.getArticles(), width));
        refreshLayout.setRefreshing(false);
    }

    @Override
    void onError(Exception e){
        refreshLayout.setRefreshing(false);
        super.onError(e);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rssEdit.clearFocus();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        rssEdit.clearFocus();
    }

    void loadFeed(){
        if(isOnline()){
            new DownloadTask(this, rssEdit.getText().toString()).execute();
        }
        else if(feed == null){
            new LoadTask(this).execute();
            offlineToast.show();
        }
    }

    void apply(){
        rssEdit.clearFocus();
        rssEdit.setVisibility(View.INVISIBLE);
        feedName.setVisibility(View.VISIBLE);
        rssButton.setImageResource(R.drawable.ic_rss);
        if(!rssurl.get().equals(rssEdit.getText().toString())) {
            new DownloadTask(this, rssEdit.getText().toString()).execute();
            refreshLayout.setRefreshing(true);
        }
    }

    public void rssOnClick(View view) {
        if (focus) {
            rssEdit.clearFocus();
        }
        else{
            feedName.setVisibility(View.INVISIBLE);
            rssEdit.setVisibility(View.VISIBLE);
            rssEdit.requestFocus();
            rssEdit.selectAll();
            rssButton.setImageResource(R.drawable.ic_check);
        }
    }
}
