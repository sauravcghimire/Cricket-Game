package com.appsinfinity.fingercricket.models;

/**
 * Created by Saurav on 8/6/2014.
 */
public class NewsDto {
    private String link;
    private String title;
    private String description;
    private String date;


    public NewsDto() {
    }



    public NewsDto(String link, String title, String description, String date) {
        this.link = link;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
