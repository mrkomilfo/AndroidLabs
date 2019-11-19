package com.example.lab3;

import android.icu.util.LocaleData;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;

public class Note implements Comparable<Note> {
    private int id;
    private String header;
    private String content;
    private ArrayList<String> tags;
    private Date date;

    public Note (int id, String header, String content)
    {
        this.id = id;
        this.header = header;
        this.content = content;
        this.date = Calendar.getInstance().getTime();
    }

    public Note (int id, String header, String content, Date date)
    {
        this.id = id;
        this.header = header;
        this.content = content;
        this.date = date;
    }

    public Note (int id, String header, String content, String tags)
    {
        this.id = id;
        this.header = header;
        this.content = content;
        this.tags = new ArrayList<>(Arrays.asList(tags.split("\\s+")));
    }

    public Note (int id, String header, String content, String tags, Date date)
    {
        this.id = id;
        this.header = header;
        this.content = content;
        this.tags = new ArrayList<>(Arrays.asList(tags.split("\\s+")));
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String tagsInString()
    {
        StringBuilder str = new StringBuilder();
        for (String tag: this.tags) {
            str.append(tag).append(" ");
        }
        if (str.length()>0) {
            str.deleteCharAt(str.length()-1);
        }
        return str.toString();
    }

    @Override
    public int compareTo(Note n) {
        return this.header.compareTo(n.header);
    }
}
