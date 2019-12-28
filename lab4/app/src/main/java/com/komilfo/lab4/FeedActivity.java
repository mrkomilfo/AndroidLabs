package com.komilfo.lab4;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class FeedActivity extends NetworkActivity {

    TextView title;
    TextView desc;
    TextView link;
    TextView rssLink;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Feed feed = (Feed) Objects.requireNonNull(getIntent().getExtras()).getSerializable("Feed");
        if (feed == null)
        {
            Toast.makeText(getApplicationContext(),"feed not found",Toast.LENGTH_SHORT).show();
            return;
        }
        RSSURL rssURL = new RSSURL(this);
        title = findViewById(R.id.titleTextView);
        desc = findViewById(R.id.descTextView);
        link = findViewById(R.id.linkTextView);
        rssLink = findViewById(R.id.rssLinkTextView);

        title.setText("Title: " + feed.getTitle());
        desc.setText("Description: " + feed.getDescription());
        link.setText("Link: " + feed.getLink());
        rssLink.setText("RSS link: " + rssURL.get());
    }
}
