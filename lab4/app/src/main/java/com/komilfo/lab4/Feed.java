package com.komilfo.lab4;

import java.io.Serializable;
import java.util.ArrayList;

class Feed implements Serializable {
    private String title = "";
    private String description = "";
    private String imageURL = "";
    private String link = "";
    private String RSSLink = "";
    private ArrayList<Article> articles = new ArrayList<>();

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRSSLink() {
        return RSSLink;
    }

    public void setRSSLink(String RSSLink) {
        this.RSSLink = RSSLink;
    }
}
