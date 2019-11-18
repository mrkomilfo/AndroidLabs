package com.example.lab3;

import android.icu.util.LocaleData;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Note {
    private long id;
    private String header;
    private String content;
    private ArrayList<String> tags;
    private LocalDate date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Note (long id, String header, String content)
    {
        this.id = id;
        this.header = header;
        this.content = content;
        this.date = LocalDate.now();
    }

    public Note (long id, String header, String content, LocalDate date)
    {
        this.id = id;
        this.header = header;
        this.content = content;
        this.date = date;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String tagsInString()
    {
        StringBuilder str = new StringBuilder();
        for (String tag: this.tags) {
            str.append(tag).append(" ");
        }
        if (str.length()>0)
        {
            str.substring(0,str.length()-1);
        }
        return str.toString();
    }
}
